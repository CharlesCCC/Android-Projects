package com.example.eecs448_assigment1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CurrentTimeService extends Service {
    public CurrentTimeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
