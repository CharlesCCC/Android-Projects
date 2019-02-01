package com.example.eecs448_assigment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class TimeServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_service);
    }

    public void OnStartService(View view) {
        Intent intent = new Intent(this, CurrentTimeService.class);
        startService(intent);
    }

    public void OnStopService(View view) {
        Intent intent = new Intent(this, CurrentTimeService.class);
        stopService(intent);
    }
}
