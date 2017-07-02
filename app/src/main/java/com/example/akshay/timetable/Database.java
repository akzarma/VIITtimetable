package com.example.akshay.timetable;

/**
 * Created by Akshay on 22-01-2017.
 */
import com.firebase.client.Firebase;

public class Database extends android.app.Application{
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
