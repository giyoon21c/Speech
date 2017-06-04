package com.example.android.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created on 5/29/17.
 */

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN = 0;
    private final int MAX = 100;

    class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("ServiceDemo", "In onBind");
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("ServiceDemo", "In onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i("ServiceDemo", "Service destroyed!");

    }

    @Override
    // gets executed whenever a service is started...
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "service thread id: " + Thread.currentThread().getId());
        /*
          there is no external way to stop the service.
          you can only stop service inside service
         */
        //stopSelf();

        mIsRandomGeneratorOn = true;
        new Thread((Runnable) () -> {
            startRandomNumberGenerator();
        }).start();
        return START_STICKY;
    }

    public void startRandomNumberGenerator() {
        while (mIsRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.i("SERVICE", "Random number: " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.i("SERVICE", "Thread interrupted...");
            }
        }
    }

    public void stopRandomNumberGenerator() {
        mIsRandomGeneratorOn = false;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("SERVICE", "In onUnbind");
        return super.onUnbind(intent);
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }
}
