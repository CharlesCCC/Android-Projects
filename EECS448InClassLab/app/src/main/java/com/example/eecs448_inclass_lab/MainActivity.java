package com.example.eecs448_inclass_lab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnYes(View view) {
        Toast.makeText(this,"Correct!", Toast.LENGTH_SHORT).show();
    }

    public void No(View view) {
        Toast.makeText(this,"Wrong !", Toast.LENGTH_SHORT).show();
    }

    public void OnService(View view) {
        Intent intent = new Intent(this,ServiceActivity.class);
        startActivity(intent);
    }
}
