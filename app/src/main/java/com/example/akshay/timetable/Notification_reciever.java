package com.example.akshay.timetable;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.SystemUpdatePolicy;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;


import static android.content.Intent.getIntent;
import static java.security.AccessController.getContext;

/**
 * Created by Akshay on 14-03-2017.
 */

public class Notification_reciever extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {

        Lecture[] lectures = fileToObjectArray();
        
        
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent repeating_intent = new Intent(context, Repeating_activity.class);


        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hr = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        int i = 0;


        //comparable time-------------------------------------------------
        int actual_time = hr * 100 + min;


        while (i < lectures.length) {

            //comparable time-----------------------------
            int file_stime = (lectures[i].start_hour) * 100 + (lectures[i].start_min);
            int file_etime = (lectures[i].end_hour) * 100 + (lectures[i].end_min);


            if ((actual_time >= file_stime && actual_time <= file_etime) && lectures[i].day == day) {


                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setContentTitle(lectures[i].start_hour + ":" + "" + lectures[i].start_min + " - " +
                                lectures[i].end_hour + ":" + lectures[i].end_min + " - " + lectures[i].lecture)
                        .setContentText(/*final_al.get(i+1).start_hour+":"+""+final_al.get(i+1).start_min+" - "+
                                    final_al.get(i+1).end_hour+":"+final_al.get(i+1).end_min+" - "+final_al.get(i+1).lecture*/"wait")
                        .setAutoCancel(false).setPriority(2);

                notificationManager.notify(100, builder.build());


                break;
            }


            i++;
        }


    }


    public Lecture[] fileToObjectArray(){
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
        Log.d("Filemsg", "in Notification reciever class final list file to lectures[]"+lectures.length);

        return lectures;
    }


}
