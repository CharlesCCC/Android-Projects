package com.example.eecs448_assigment1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class ContactActivity extends AppCompatActivity {

    EditText mName;
    EditText mPhone;
    ListView mDBview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mName = findViewById(R.id.text_name);
        mPhone = findViewById(R.id.text_phone);
        mDBview = findViewById(R.id.dbview);

    }

    public void onInsert(View view) {

    }

    public void onShowAll(View view) {

    }

    public void onDeleteAll(View view) {

    }
}
