package com.example.eecs448_assigment1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.Arrays;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, intent.getStringExtra("msg"),Toast.LENGTH_SHORT).show();
                Toast.makeText(context, intent.getStringExtra("time"),Toast.LENGTH_SHORT).show();
            }
        });
    //  throw new UnsupportedOperationException("Not yet implemented");
    }
}
