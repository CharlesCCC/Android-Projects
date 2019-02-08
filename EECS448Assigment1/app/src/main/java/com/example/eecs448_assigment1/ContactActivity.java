package com.example.eecs448_assigment1;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class ContactActivity extends AppCompatActivity {

    EditText mName;
    EditText mPhone;
    TextView mDBview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mName = findViewById(R.id.text_name);
        mPhone = findViewById(R.id.text_phone);
        mDBview = findViewById(R.id.dbview);

        onShowAll(this.mDBview);
    }

    public void onInsert(View view) {
        ContentValues values = new ContentValues();
        values.put(ContactDB.ID, new Random().nextInt(100));
        values.put(ContactDB.NAME, mName.getText().toString());
        values.put(ContactDB.PHONE, mPhone.getText().toString());
        getApplicationContext().getContentResolver().insert(ContactContentProvider.CONTENT_URI,values);
        Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
        onShowAll(view);
    }

    public void onDeleteAll(View view) {
        int delcount = getContentResolver().delete(ContactContentProvider.CONTENT_URI,null,null);
        Toast.makeText(this,"Delete " + delcount + " contact(s) ",Toast.LENGTH_SHORT).show();
        onShowAll(view);
    }

    public void onShowAll(View view) {  // why Could not find method onShow(View) in a parent or ancestor Context for android:onClick attribute defined on view class ?
        Uri uri = ContactContentProvider.CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
        StringBuilder sb = new StringBuilder();
        while(cursor.moveToNext()){
            sb.append("ID:" + cursor.getString(0) + ",");
            sb.append("\n");
            sb.append("Name:" + cursor.getString(1) + ",");
            sb.append("\n");
            sb.append("Phone:" + cursor.getString(2) + ",");
            sb.append("\n");
            sb.append("\n");
        }
        mDBview.setText(sb.toString());
    }
}
