package com.example.eecs448_assigment1;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Chronometer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void SendBroadcast(View view) {
        Intent intent = new Intent("com.eecs448.assignment1");
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());

        intent.putExtra("msg","Message recieved, Current time is: ");
        intent.putExtra("time", date);
        sendBroadcast(intent);
    }

    public void GotoServiceActivity(View view) {
        Intent intent = new Intent(this,TimeServiceActivity.class);
        startActivity(intent);
    }



}
