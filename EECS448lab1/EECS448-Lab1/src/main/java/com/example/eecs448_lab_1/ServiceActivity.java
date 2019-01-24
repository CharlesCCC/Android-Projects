package com.example.eecs448_lab_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.eecs448_lab_1.services.RandomNumberService;

public class ServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
    }

    public void OnStartService(View view) {
        Intent intent = new Intent(this, RandomNumberService.class);
        startService(intent);
    }
}
