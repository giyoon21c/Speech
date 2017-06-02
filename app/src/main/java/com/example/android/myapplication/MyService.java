package com.example.android.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created on 5/29/17.
 */

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    // gets executed whenever a service is started...
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "service thread id: " + Thread.currentThread().getId());
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }
}
