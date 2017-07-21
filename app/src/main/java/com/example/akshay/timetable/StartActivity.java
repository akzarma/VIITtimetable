package com.example.akshay.timetable;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class StartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner branch_spnr, teacher_spnr, user_spnr, div_spnr, batch_spnr, class_spnr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        branch_spnr = (Spinner) findViewById(R.id.spinner8);
        teacher_spnr = (Spinner) findViewById(R.id.spinner9);
        div_spnr = (Spinner) findViewById(R.id.spinner6);
        batch_spnr = (Spinner) findViewById(R.id.spinner10);
        class_spnr = (Spinner) findViewById(R.id.spinner5);
        user_spnr = (Spinner) findViewById(R.id.spinner7);

        Button next = (Button) findViewById(R.id.button3);


        user_spnr.setOnItemSelectedListener(this);
        div_spnr.setOnItemSelectedListener(this);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {





                if (user_spnr.getSelectedItem().toString().equals("Teacher")) {


                    User user = new User(user_spnr.getSelectedItem().toString(),teacher_spnr.getSelectedItem().toString(),
                            branch_spnr.getSelectedItem().toString());

                    String json = new Gson().toJson(user);

                    Log.d("CREATION", json);

                    FileWriter a = null;
                    try {
                        a = new FileWriter(Environment.getExternalStorageDirectory() + "/timetable/User.json");
                        a.write(json);
                        a.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {

                    User user = new User(user_spnr.getSelectedItem().toString(),class_spnr.getSelectedItem().toString(),
                            branch_spnr.getSelectedItem().toString(), div_spnr.getSelectedItem().toString(),
                            batch_spnr.getSelectedItem().toString());

                    String json = new Gson().toJson(user);

                    Log.d("CREATION", json);

                    FileWriter a = null;
                    try {
                        a = new FileWriter(Environment.getExternalStorageDirectory() + "/timetable/User.json");
                        a.write(json);
                        a.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }


            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.spinner7:
                if(user_spnr.getSelectedItem().toString().equals("Teacher")){
                    Snackbar.make(view, "Select Teacher Name and Branch", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    branch_spnr.setVisibility(View.VISIBLE);
                    teacher_spnr.setVisibility(View.VISIBLE);

                    div_spnr.setVisibility(View.GONE);
                    class_spnr.setVisibility(View.GONE);
                    batch_spnr.setVisibility(View.GONE);

                }else {
                    Snackbar.make(view, "Select Class, Branch, Div, Batch", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    branch_spnr.setVisibility(View.VISIBLE);
                    teacher_spnr.setVisibility(View.GONE);

                    div_spnr.setVisibility(View.VISIBLE);
                    class_spnr.setVisibility(View.VISIBLE);
                    batch_spnr.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.spinner6:
                if (div_spnr.getSelectedItem().equals("A")) {

                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                            R.array.A_array, android.R.layout.simple_spinner_item);
                    batch_spnr.setAdapter(adapter2);
                } else if (div_spnr.getSelectedItem().equals("B")) {


                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                            R.array.B_array, android.R.layout.simple_spinner_item);
                    batch_spnr.setAdapter(adapter2);
                } else if (div_spnr.getSelectedItem().equals("C")) {


                    ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this,
                            R.array.C_array, android.R.layout.simple_spinner_item);

                    batch_spnr.setAdapter(adapter2);
                }
                break;
        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
