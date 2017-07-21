package com.example.akshay.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.google.gson.Gson;


/**
 * Created by Akshay on 06-06-2017.
 */

public class Add_activity extends Activity {

    int i = 0;
    ArrayList<Lecture> al = new ArrayList<Lecture>();
    EditText branch_field;
    EditText year_field;
    EditText div_field;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_layout);
        final TextView tv = (TextView) findViewById(R.id.status);


        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                tv.setText("Uploading...");

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl("gs://timetable-31fd9.appspot.com").child(year_field.getText().toString() + branch_field.getText().toString() + div_field.getText().toString() + ".json");

                try {

                    Lecture[] lectures = al.toArray(new Lecture[al.size()]);

                    String json = new Gson().toJson(lectures);

                    Log.d("CREATION", json);

                    FileWriter a = new FileWriter(Environment.getExternalStorageDirectory() + "/timetable/dataAddActivity.json");
                    a.write(json);
                    a.close();


                    final Uri file = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/timetable/dataAddActivity.json"));

                    storageRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    tv.setText("Uploaded!!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    tv.setText("Failed to Upload.");

                                }
                            });


                } catch (IOException e) {
                    e.printStackTrace();
                }

                branch_field.setEnabled(true);
                year_field.setEnabled(true);
                div_field.setEnabled(true);


                i = 0;


            }


        });


        findViewById(R.id.add_act).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btn = (Button) findViewById(R.id.add_act);

                EditText startHour_field = (EditText) findViewById(R.id.add_t1);
                EditText startMinute_field = (EditText) findViewById(R.id.add_t2);
                EditText endHour_field = (EditText) findViewById(R.id.editText8);
                EditText endMinute_field = (EditText) findViewById(R.id.editText9);
                branch_field = (EditText) findViewById(R.id.editText5);
                year_field = (EditText) findViewById(R.id.editText4);
                EditText day_field = (EditText) findViewById(R.id.editText);
                EditText lecture_field = (EditText) findViewById(R.id.editText3);
                div_field = (EditText) findViewById(R.id.editText7);
                EditText batch_field = (EditText) findViewById(R.id.editText6);


                branch_field.setEnabled(false);
                year_field.setEnabled(false);
                div_field.setEnabled(false);

                Lecture temp = new Lecture();
                temp.start_hour = Integer.parseInt(startHour_field.getText().toString());
                temp.start_min = Integer.parseInt(startMinute_field.getText().toString());
                temp.end_hour = Integer.parseInt(endHour_field.getText().toString());
                temp.end_min = Integer.parseInt(endMinute_field.getText().toString());
                temp.branch = branch_field.getText().toString();
                temp.day = Integer.parseInt(day_field.getText().toString());
                temp.year = year_field.getText().toString();
                temp.lecture = lecture_field.getText().toString();
                temp.div = div_field.getText().charAt(0);
                temp.batch = batch_field.getText().toString();


                startHour_field.setText("");
                startMinute_field.setText("");
                endHour_field.setText("");
                endMinute_field.setText("");


                al.add(temp);

                tv.setText("Last Entry ->" + al.get(i).start_hour + ":" + al.get(i).start_min + " - " + al.get(i).end_hour + ":" + al.get(i).end_min
                        + " = " + al.get(i).lecture + " " + al.get(i).year + " " + al.get(i).branch + " " + al.get(i).batch);

                i++;

            }
        }));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }


}
