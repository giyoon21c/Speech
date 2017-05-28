package com.example.android.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

/**
 * Created on 5/28/17.
 */

public class CountRecorder {

    public static abstract class Callback {
        // when count is starting
        public void onCountStart() {
        }

        // add information in the data array for further process in the main activity
        public void onCount(int num) {
        }

        // when counting is done
        public void onCountEnd() {
        }
    }

    private Thread mThread;
    private final Callback mCallback;

    //constructor
    public CountRecorder(@NonNull Callback callback) {
        mCallback = callback;
    }

    public void start() {
        mThread = new Thread(new ProcessData());
        mThread.start();
    }

    private class ProcessData implements Runnable {
        int i;
        @Override
        public void run() {
            while (true) {
                if (i == 0) {
                    mCallback.onCountStart();
                } else {
                    if ((i % 10000) == 0) {
                        mCallback.onCount(i);
                    }
                }
                i++;
            }
        }
    }
}
