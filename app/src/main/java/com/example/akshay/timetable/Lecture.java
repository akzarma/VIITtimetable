package com.example.akshay.timetable;
import android.text.format.Time;

import java.io.Serializable;
/**
 * Created by Akshay on 06-06-2017.
 */

public class Lecture implements Serializable{
    public
    int start_hour;
    int start_min;
    int end_hour;
    int end_min;
    int day; //sunday=1
    String year;  //FE SE TE BE
    String branch;   //FE COMP IT ENTC CIVIL
    String lecture; //VTP-MIL
    char div; // A B C D.........
    String batch;   //B1 A1
}
