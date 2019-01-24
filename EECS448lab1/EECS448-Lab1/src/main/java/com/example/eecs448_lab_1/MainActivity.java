package com.example.eecs448_lab_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void OnYes(View view) {
        Toast.makeText(this,"Correct",Toast.LENGTH_SHORT).show();
        TextView textView = findViewById(R.id.newtext);
        textView.setText("Correct Answer");
    }

    public void OnNo(View view) {
        TextView textView = findViewById(R.id.newtext);
        textView.setText("Wrong Answer");

        Toast.makeText(this,"Incorrect",Toast.LENGTH_SHORT).show();
    }

    public void OnSecondActivity(View view) {
        Intent intent = new Intent(this,ServiceActivity.class);
        startActivity(intent);
    }

    public void OnBroadcast(View view) {
        Intent intent = new Intent("com.example.coursedemo");
        intent.putExtra("msg", "Broadcast event!");
        sendBroadcast(intent);
    }

    public void OnDB(View view) {
        Intent intent = new Intent(this,DBActivity.class);
        startActivity(intent);
    }
}
