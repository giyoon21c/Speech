package com.example.android.myapplication;

/**
 * Created on 5/28/17.
 */

public class CountRecorder {

    public static abstract class Callback {

        // when count is starting
        public void onCountStart() {

        }

        // add information in the data array for further process in the main activity
        public void onCount(int[] data, int size) {

        }

        // when counting is done
        public void onCountEnd() {

        }
    }

    private Thread mThread;
    
    public void start() {

        mThread = new Thread(new ProcessData());

    }
}
