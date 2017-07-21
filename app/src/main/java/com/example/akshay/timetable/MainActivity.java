package com.example.akshay.timetable;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import android.widget.Button;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;


import com.google.gson.Gson;

public class MainActivity extends Activity {


    public static Lecture[] lectures;

    Lecture[] final_lectures = new Lecture[80];

    String jsonStr;

    int count = 0;

    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = new ProgressDialog(MainActivity.this);
                pd.setTitle("Downloading VIIT TimeTable");
                pd.setMessage("Please wait while we setup your profile");
                pd.setCanceledOnTouchOutside(false);
                pd.show();


                int j = 0;
                while (j < 80) {
                    final_lectures[j] = null;
                    j++;
                }


                String json = fileToJson("User");


                Gson gson = new Gson();
                User user = gson.fromJson(json, User.class);


                if (user.userType.equals("Student")) {
                    onStudent(user.classSelected, user.branchSelected,
                            user.divSelected, user.batchSelected);
                } else if (user.userType.equals("Teacher")) {

                    try {
                        onTeacher(user.teacherName, user.branchSelected);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

        });



        Button reset = (Button)findViewById(R.id.button4);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(Environment.getExternalStorageDirectory(), "/timetable/final_list.json");
                File dataFile = new File(Environment.getExternalStorageDirectory(), "/timetable/dataFromNet.json");
                File userFile = new File(Environment.getExternalStorageDirectory(), "/timetable/User.json");
                f.delete();
                dataFile.delete();
                userFile.delete();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
            }
        });


        Button btn = (Button) findViewById(R.id.add_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add_activity.class));
            }
        });


    }

    public void downloadFileTeacher(String class_spnr, String branch_spnr, String div_spnr, final String teacher_spnr, final int max) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timetable-31fd9.appspot.com").child(class_spnr + branch_spnr
                + div_spnr + ".json");

        try {

            File storagePath = new File(Environment.getExternalStorageDirectory(), "/timetable/");
            // Create direcorty if not exists
            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            File localFile = new File(Environment.getExternalStorageDirectory() + "/timetable/", "dataFromNet.json");


            Log.d("Filemsg", "File not available");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Filemsg", "File downloaded Now calling filetojson()");
                    jsonStr = fileToJson("dataFromNet");


                    Gson gson = new Gson();
                    lectures = gson.fromJson(jsonStr, Lecture[].class);
                    Log.d("Filemsg", "after filetojson() it is loaded to lectures[]");


                    Log.d("lectures_length", String.valueOf(lectures.length));
                    try {
                        extractTeacher(teacher_spnr, max);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Filemsg", "Download failed. Try again!");
                }
            });


        } catch (Exception ioe) {

            Log.d("Filemsg", "File creation failed" + ioe);

        }


    }

    public void downloadFileStudent(String class_spnr, String branch_spnr, String div_spnr, final String batch_spnr) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://timetable-31fd9.appspot.com").child(class_spnr + branch_spnr
                + div_spnr + ".json");

        try {

            File storagePath = new File(Environment.getExternalStorageDirectory(), "/timetable/");
            // Create direcorty if not exists
            if (!storagePath.exists()) {
                storagePath.mkdirs();
            }

            File localFile = new File(Environment.getExternalStorageDirectory() + "/timetable/", "dataFromNet.json");


            Log.d("Filemsg", "File not available");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Filemsg", "File downloaded Now calling filetojson()");
                    jsonStr = fileToJson("dataFromNet");


                    Gson gson = new Gson();
                    lectures = gson.fromJson(jsonStr, Lecture[].class);
                    Log.d("Filemsg", "after filetojson() it is loaded to lectures[]");


                    Log.d("lectures_length", String.valueOf(lectures.length));

                    try {
                        extractStudent(batch_spnr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("Filemsg", "Download failed. Try again!");
                }
            });


        } catch (Exception ioe) {

            Log.d("Filemsg", "File creation failed" + ioe);

        }


    }


    public String fileToJson(String fileName) {
        String jsonStr = null;
        try {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/timetable/" + fileName + ".json"));

                Log.d("filetojson", "andar" + Environment.getExternalStorageDirectory() + "/timetable/" + fileName + ".json");
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                jsonStr = sb.toString();

            } catch (FileNotFoundException e) {
                Log.d("filetojson", "file is not there on sd card to convert file to json.");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                br.close();
            }

        } catch (Exception e) {
            Log.d("Error", e.toString());
        }


        return jsonStr;

    }


    public void onStudent(String class_spnr, String branch_spnr, String div_spnr, String batch_spnr) {


        downloadFileStudent(class_spnr, branch_spnr, div_spnr, batch_spnr);
        //file downloanded at /timetable/dataFromNet.json     // onSuccess it is converted to jsonStr and then loaded into lectures[]


    }

    public void onTeacher(String teacher_spnr, String branch_spnr) throws IOException {

        int final_i = 0;
        while (final_i < 80) {
            final_lectures[final_i] = null;
            final_i++;
        }


        String[] classArray = {"SE", "TE", "BE"};
        String[] divArray = {"A", "B", "C"};


        int i = 0, j = 0;
        count = 0;

        while (i < classArray.length) {
            j = 0;
            while (j < divArray.length) {

                downloadFileTeacher(classArray[i], branch_spnr, divArray[j], teacher_spnr, classArray.length * divArray.length);
                //file downloanded at /timetable/dataFromNet.json     // onSuccess it is converted to jsonStr and then loaded into lectures[]

                // onSuccess extracting teachers data to final_lecture[]


                j++;
            }


            i++;
        }


    }

    public void extractStudent(String batch_spnr) throws IOException {
        int i = 0, final_i = 0;

        while (i < lectures.length) {
            if (lectures[i].batch.equals("")) {
                final_lectures[final_i] = lectures[i];
                final_i++;
            } else if (lectures[i].batch.equals(batch_spnr)) {
                final_lectures[final_i] = lectures[i];
                final_i++;
            }
            i++;
        }

        alarmService(final_lectures);

        startActivity(new Intent(MainActivity.this, Repeating_activity.class));

    }

    public void extractTeacher(String teacher_spnr, int max) throws IOException {
        int final_i = 0, local_i = 0;


        if (lectures != null) {
            while (final_i < 80) {
                if (final_lectures[final_i] == null) {
                    while (local_i < lectures.length) {
                        if (lectures[local_i].teacher.equals(teacher_spnr)) {
                            final_lectures[final_i] = lectures[local_i];
                            Log.d("lectures1 local", lectures[local_i].teacher);
                            final_i++;
                        }
                        local_i++;
                    }
                    break;
                }
                final_i++;
            }
            //adding to final list of lecture of that tchr
        }

        count++;

        if (count == max) {
            alarmService(final_lectures);

            startActivity(new Intent(MainActivity.this, Repeating_activity.class));
        }


    }


    public void alarmService(Lecture[] lecture_array) throws IOException {

        pd.hide();
        int count = 0;

        while (lecture_array[count] != null) {
            count++;
        }

        Lecture[] final_list = new Lecture[count];

        int i = 0;
        while (i < count) {
            final_list[i] = lecture_array[i];
            i++;
        }
        Log.d("length", String.valueOf(final_list.length));

        //final list to json file
        String json = new Gson().toJson(final_list);

        Log.d("CREATION", json);

        FileWriter a = new FileWriter(Environment.getExternalStorageDirectory() + "/timetable/final_list.json");
        a.write(json);
        a.close();


        Calendar calendar = Calendar.getInstance();


        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), Notification_reciever.class);
        intent.putExtra("lectures", final_list);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 60000, pendingIntent);


    }


    public void onStart() {
        super.onStart();

        File f = new File(Environment.getExternalStorageDirectory(), "/timetable/final_list.json");

        File start = new File(Environment.getExternalStorageDirectory(), "/timetable/User.json");

        if (f.exists()) {
            Intent repeatIntent = new Intent(MainActivity.this, Repeating_activity.class);
            startActivity(repeatIntent);
            finish();
        } else if (!start.exists()) {
            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }
    }
}
