package com.example.akshay.timetable;

/**
 * Created by akzarma on 21/7/17.
 */

public class User {
    String branchSelected;
    String divSelected;
    String batchSelected;
    String userType;
    String teacherName;
    String classSelected;

    public User(String userType, String teacherName, String branchSelected){
        this.userType = userType;
        this.branchSelected = branchSelected;
        this.teacherName = teacherName;
    }
    public User(String userType, String classSelected, String branchSelected, String divSelected, String batchSelected){
        this.userType = userType;
        this.branchSelected = branchSelected;
        this.divSelected = divSelected;
        this.classSelected = classSelected;
        this.batchSelected = batchSelected;
    }
}
