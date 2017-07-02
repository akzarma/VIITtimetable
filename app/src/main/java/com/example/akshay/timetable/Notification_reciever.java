package com.example.akshay.timetable;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.SystemUpdatePolicy;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v4.app.NotificationCompat;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;


import static java.security.AccessController.getContext;

/**
 * Created by Akshay on 14-03-2017.
 */
public class Notification_reciever extends BroadcastReceiver {


    @Override
    public void onReceive(final Context context, Intent intent) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent repeating_intent = new Intent(context,Repeating_activity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context,100,repeating_intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Toast.makeText(context,"Intent called", Toast.LENGTH_SHORT).show();






        Calendar calendar= Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hr=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);

        int i=0;


        //comparable time-------------------------------------------------
        int actual_time = hr*100+min;


        while(i<MainActivity.final_al.size()){

            //comparable time-----------------------------
            int file_stime = (MainActivity.final_al.get(i).start_hour)*100+(MainActivity.final_al.get(i).start_min);
            int file_etime = (MainActivity.final_al.get(i).end_hour)*100+(MainActivity.final_al.get(i).end_min);


            if((actual_time >= file_stime && actual_time <= file_etime) && MainActivity.final_al.get(i).day==day){



                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(android.R.drawable.arrow_up_float)
                            .setContentTitle(MainActivity.final_al.get(i).start_hour+":"+""+MainActivity.final_al.get(i).start_min+" - "+
                                    MainActivity.final_al.get(i).end_hour+":"+MainActivity.final_al.get(i).end_min+" - "+MainActivity.final_al.get(i).lecture)
                            .setContentText(/*MainActivity.final_al.get(i+1).start_hour+":"+""+MainActivity.final_al.get(i+1).start_min+" - "+
                                    MainActivity.final_al.get(i+1).end_hour+":"+MainActivity.final_al.get(i+1).end_min+" - "+MainActivity.final_al.get(i+1).lecture*/"wait")
                            .setAutoCancel(false).setPriority(2);

                    notificationManager.notify(100, builder.build());






                break;
            }


            i++;
        }





















        /*
        Calendar calendar= Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hr=calendar.get(Calendar.HOUR_OF_DAY);
        int min=calendar.get(Calendar.MINUTE);







        if(hr>6 && hr<15) {

            int new_min,next_hr,next_new_min;







            if (hr == 7 || hr == 8 || hr == 9)
                new_min = 0;
            else
                new_min = 15;

            if (hr == 7)
                hr = 8;
            else if (hr == 12 || hr == 13)
                hr = 1;
            else if (hr == 14)
                hr = 2;


            next_hr=hr+1;

            if(hr==11)
                next_hr=1;



            if (next_hr == 7 || next_hr == 8 || next_hr == 9)
                next_new_min = 0;
            else
                next_new_min = 15;


            final String[] Next_Lec = new String[1];




            String current = null;

            switch(hr){
                case 8: current="8:00 - 9:00";break;
                case 9: current="9:00 - 10:00";break;
                case 10: current="10:15 - 11:15";break;
                case 11: current="11:15 - 12:15";break;
                case 1: current="01:15 - 02:15";break;
                case 2: current="02:15 - 03:15";break;
            }

            String next = null;

            switch(next_hr){
                case 8: next="8:00 - 9:00";break;
                case 9: next="9:00 - 10:00";break;
                case 10: next="10:15 - 11:15";break;
                case 11: next="11:15 - 12:15";break;
                case 1: next="01:15 - 02:15";break;
                case 2: next="02:15 - 03:15";break;
            }





            db_next = FirebaseDatabase.getInstance().getReference();
            fref_next = new Firebase("https://timetable-31fd9.firebaseio.com/Branch/Computer/Class/SE/Section/B/Day/" + day + "/" + next_hr + ":" + next_new_min + "/");

            fref_next.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Next_Lec[0] = dataSnapshot.getValue(String.class);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });



           // String next_lecture=Next_Lec[0];
            db = FirebaseDatabase.getInstance().getReference();
            firebasereference = new Firebase("https://timetable-31fd9.firebaseio.com/Branch/Computer/Class/SE/Section/B/Day/" + day + "/" + hr + ":" + new_min + "/");

            final String finalCurrent = current;
            final String finalNext = next;
            firebasereference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String a = dataSnapshot.getValue(String.class);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setContentIntent(pendingIntent)
                            .setSmallIcon(android.R.drawable.arrow_up_float)
                            .setContentTitle(finalCurrent +"   : " + a)
                            .setContentText(finalNext +"   : "+ Next_Lec[0])
                            .setAutoCancel(false).setPriority(2);

                    notificationManager.notify(100, builder.build());

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }*/



    }
}
