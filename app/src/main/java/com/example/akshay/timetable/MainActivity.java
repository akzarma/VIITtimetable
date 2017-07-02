package com.example.akshay.timetable;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.core.Context;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static android.widget.Toast.LENGTH_SHORT;
import static java.security.AccessController.getContext;

public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

    TextView msg;
    public static ArrayList<Lecture> al = new ArrayList<Lecture>();
    public static ArrayList<Lecture> final_al = new ArrayList<Lecture>();
    Spinner class_spnr,div_spnr,batch_spnr,branch_spnr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        div_spnr = (Spinner) findViewById(R.id.spinner2);
        batch_spnr = (Spinner) findViewById(R.id.spinner3);
        class_spnr = (Spinner) findViewById(R.id.spinner);
        branch_spnr = (Spinner) findViewById(R.id.spinner5);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,
                R.array.div_array, android.R.layout.select_dialog_item);
        div_spnr.setAdapter(adapter1);
        div_spnr.setOnItemSelectedListener(this);










        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msg=(TextView)findViewById(R.id.msg);
                msg.setText("From now, you will get notifications between 7 AM to 3 PM ");

                TextView temp_data = (TextView)findViewById(R.id.downloaded_field);
                TextView status2 = (TextView)findViewById(R.id.textView2);








                File f = new File(Environment.getExternalStorageDirectory()+"/timetable/dataFromNet.bin");
                if(f.exists())
                {
                    msg.setText("File is already there.");
                }
                else {


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://timetable-31fd9.appspot.com").child(class_spnr.getSelectedItem().toString()+branch_spnr.getSelectedItem().toString()
                            +div_spnr.getSelectedItem().toString()+".bin");

                    try {
                        //File storagePath = new File(Environment.getExternalStorageDirectory(), "/data/data/com.example.akshay.timetable/files/");

                        File storagePath = new File(Environment.getExternalStorageDirectory(),"/timetable/");
                        // Create direcorty if not exists
                        if (!storagePath.exists()) {
                            storagePath.mkdirs();
                        }

                        File localFile = new File(Environment.getExternalStorageDirectory()+"/timetable/", "dataFromNet.bin");


                        msg.setText("File not available");
                        storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                msg.setText("File downloaded");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                msg.setText("Download failed. Try again!");
                            }
                        });


                    } catch (Exception ioe) {

                        msg.setText("File creation failed" + ioe);

                    }


                }




                    try {

                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Environment.getExternalStorageDirectory()+"/timetable/dataFromNet.bin"));
                            al = (ArrayList<Lecture>) ois.readObject();

                        ois.close();
                    }catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }




                //temp_data.setText(temp_data.getText() + al.get(1).lecture);





                //sorting al according to user batch
                // making final array list according to user choice

                final_al.clear();
                int i=0;
                while(i < al.size()){

                    if((al.get(i).batch.equals(batch_spnr.getSelectedItem().toString())) || (al.get(i).div == div_spnr.getSelectedItem().toString().charAt(0) &&
                        al.get(i).batch.equals(""))){
                        final_al.add(al.get(i));
                    }
                    i++;
                }

                status2.setText(al.size()+"<-al "+final_al.size()+"<-final_al");


                temp_data.setText(batch_spnr.getSelectedItem().toString());


                //sort final array list
                int k=0;
                while(k<final_al.size()) {
                    msg.setText(msg.getText() + " -> " + final_al.get(k).start_hour+"-"+final_al.get(k).end_hour);
                    k++;
                }













                Calendar calendar= Calendar.getInstance();



                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int hr=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);



                //status2.setText(""+(hr>=MainActivity.final_al.get(0).start_hour) + (hr<=MainActivity.final_al.get(1).end_hour)+MainActivity.final_al.get(1).end_hour + (MainActivity.final_al.get(0).day==day) +(min>= MainActivity.final_al.get(0).start_min));


                calendar.set(Calendar.HOUR_OF_DAY,7);
                calendar.set(Calendar.MINUTE,0);
                calendar.set(Calendar.SECOND,0);

                Intent intent= new Intent(getApplicationContext(),Notification_reciever.class);

                PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager=(AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),60000,pendingIntent);

            }
        });




        Button btn = (Button)findViewById(R.id.add_button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Add_activity.class));
            }
        });



    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



        if (div_spnr.getSelectedItem().equals("A")) {
            Toast.makeText(getApplicationContext(), "Division A",
                    Toast.LENGTH_SHORT).show();

            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.A_array, android.R.layout.select_dialog_item );
            batch_spnr.setAdapter(adapter2);
        } else  if (div_spnr.getSelectedItem().equals("B")) {
            Toast.makeText(getApplicationContext(), "Division B",
                    Toast.LENGTH_SHORT).show();

            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.B_array, android.R.layout.select_dialog_item);
            batch_spnr.setAdapter(adapter2);
        } else  if (div_spnr.getSelectedItem().equals("C")) {
            Toast.makeText(getApplicationContext(), "Division C",
                    Toast.LENGTH_SHORT).show();

            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                    R.array.C_array, android.R.layout.select_dialog_item);
            batch_spnr.setAdapter(adapter2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
