package com.example.eecs448_assigment1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class CurrentTimeService extends Service {

    private Handler mHandler = new Handler();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    public CurrentTimeService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(CurrentTimeService.this,"Service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getCurrentTimeToastText();
        return super.onStartCommand(intent, flags, startId);
    }

    private void getCurrentTimeToastText() {
        mHandler.postDelayed(CurrentTimeToastTextRunnable, 3000);
    }

    private Runnable CurrentTimeToastTextRunnable = new Runnable () {
        public void run() {
            Toast.makeText(CurrentTimeService.this, "Current Time: " + sdf.format(System.currentTimeMillis()), Toast.LENGTH_SHORT).show();
            //TODO - here I could update the textView ??? or not the best thing to do, consider here is the service; only controller is able to do it ?
            getCurrentTimeToastText();
        }
    };


    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(CurrentTimeToastTextRunnable);
        stopSelf();
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
