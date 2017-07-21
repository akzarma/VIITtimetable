package com.example.akshay.timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;


/**
 * Created by Akshay on 14-03-2017.
 */

public class Repeating_activity extends Activity {

    TextView t1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);

        final Lecture[] lectures = fileToObjectArray();

        t1 = (TextView) findViewById(R.id.textView3);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        int i = 0;
        while (i < lectures.length) {
            if (lectures[i].day == day) {
                t1.setText(t1.getText() + "\n---------------------------\n" + lectures[i].year + " " +
                        lectures[i].div + " " + lectures[i].batch + " " + lectures[i].start_hour + ":" + lectures[i].start_min + " - "
                        + lectures[i].end_hour + ":" + lectures[i].end_min + " -> "
                        + lectures[i].lecture + " " + lectures[i].teacher);
            }

            i++;
        }


        Button showWeek = (Button) findViewById(R.id.button8);

        showWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0,j=2;
                String day;


                while(j<8) {

                    if(j==2)
                        t1.setText(t1.getText() + "\n-------------\nMonday\n-------------");
                    else if(j==3)
                        t1.setText(t1.getText() + "\n-------------\nTuesday\n-------------");
                    else if(j==4)
                        t1.setText(t1.getText() + "\n-------------\nWednesday\n-------------");
                    else if(j==5)
                        t1.setText(t1.getText() + "\n-------------\nThursday\n-------------");
                    else if(j==6)
                        t1.setText(t1.getText() + "\n-------------\nFriday\n-------------");
                    else if(j==7)
                        t1.setText(t1.getText() + "\n-------------\nSaturday\n-------------");

                    i = 0;
                    while (i < lectures.length) {
                        if (lectures[i].day == j) {
                            t1.setText(t1.getText() + "\n" + lectures[i].year + " " +
                                    lectures[i].div + " " + lectures[i].batch + " " + lectures[i].start_hour + ":" + lectures[i].start_min + " - "
                                    + lectures[i].end_hour + ":" + lectures[i].end_min + " -> "
                                    + lectures[i].lecture + " " + lectures[i].teacher);
                        }
                        i++;
                    }
                    j++;
                }


            }
        });


        Button home = (Button) findViewById(R.id.button5);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                File f = new File(Environment.getExternalStorageDirectory(), "/timetable/final_list.json");
                f.delete();
                Intent startIntent = new Intent(Repeating_activity.this, MainActivity.class);
                startActivity(startIntent);
                finish();
            }
        });

        Button reset = (Button) findViewById(R.id.button7);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File f = new File(Environment.getExternalStorageDirectory(), "/timetable/final_list.json");
                File dataFile = new File(Environment.getExternalStorageDirectory(), "/timetable/dataFromNet.json");
                File userFile = new File(Environment.getExternalStorageDirectory(), "/timetable/User.json");
                f.delete();
                dataFile.delete();
                userFile.delete();
                startActivity(new Intent(Repeating_activity.this, MainActivity.class));
            }
        });


    }


    public Lecture[] fileToObjectArray() {
        String jsonStr = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory() + "/timetable/final_list.json"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Log.d("filetojson", "andar" + Environment.getExternalStorageDirectory() + "/timetable/final_list.json");
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            line = br.readLine();


            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }

            jsonStr = sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Lecture[] lectures = gson.fromJson(jsonStr, Lecture[].class);
        Log.d("Filemsg", "in Repeating activity class final list file to lectures[]");

        return lectures;
    }


}
