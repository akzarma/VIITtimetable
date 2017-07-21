package com.example.akshay.timetable;


import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Akshay on 11-07-2017.
 */

public class Teacher_data {


    public static Lecture[] final_lectures = new Lecture[20];
    public static Lecture[] lectures;


    public void makeFile(String teacherName, String branch) {


        String[] classArray = {"SE", "TE", "BE"};
        String[] divArray = {"A", "B", "C"};



        int i = 0, j = 0;

        while (i < classArray.length) {
            j=0;
            while (j < divArray.length) {


                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://timetable-31fd9.appspot.com").child(classArray[i] + branch
                        + divArray[j] + ".json");

                try {

                    File storagePath = new File(Environment.getExternalStorageDirectory(), "/timetable/");
                    // Create direcorty if not exists
                    if (!storagePath.exists()) {
                        storagePath.mkdirs();
                        Log.d("Filemsg","File doesnt exist");
                    }

                    File localFile = new File(Environment.getExternalStorageDirectory() + "/timetable/", "dataFromNet.json");


                    storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Filemsg","File downloaded");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Filemsg","Download failed. Try again!");
                        }
                    });


                } catch (Exception ioe) {

                    Log.d("Filemsg","File creation failed" + ioe);

                }


                // now converting each file to json string and then array of object
                String jsonStr = null;
                Log.d("info","yaha");
                Log.d("info","ha");


                try {
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/timetable/dataFromNet.json"));

                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();

                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        jsonStr = sb.toString();

                    } catch (FileNotFoundException e) {
                        Log.d("info","datafromnet not found"+e);
                        e.printStackTrace();

                    } catch (IOException e) {
                        Log.d("info","datafromnet not found io"+e);
                        e.printStackTrace();
                    } finally {
                        br.close();
                    }

                } catch (Exception e) {
                    System.out.print(e);
                }


                Gson gson = new Gson();
                lectures = gson.fromJson(jsonStr, Lecture[].class);





                if(lectures!=null){
                    for(int final_i=0; final_i<20; final_i++) {
                        if (final_lectures[final_i] == null) {
                            for (int local_i = 0; local_i < lectures.length; local_i++) {
                                if(lectures[local_i]!=null){
                                    if ((lectures[local_i].teacher).equals(teacherName.toString())) {
                                        final_lectures[final_i] = lectures[local_i];
                                        final_i++;
                                    }
                                }

                            }
                            break;
                        }
                    }
                }
                //adding to final list of lecture of that tchr





                j++;
            }



            i++;
        }

    }
}
