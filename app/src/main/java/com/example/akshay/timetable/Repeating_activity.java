package com.example.akshay.timetable;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Akshay on 14-03-2017.
 */

public class Repeating_activity extends Activity {

    TextView t1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repeating_activity_layout);
        t1=(TextView) findViewById(R.id.textView3);
        int i=0;
        while(i < MainActivity.final_al.size()){
            t1.setText(t1.getText()+"\n"+MainActivity.final_al.get(i).start_hour+":"+MainActivity.final_al.get(i).start_min+" - "
                    +MainActivity.final_al.get(i).end_hour+":"+MainActivity.final_al.get(i).end_min+" -> "
                    +MainActivity.final_al.get(i).lecture);
            i++;
        }
    }
}
