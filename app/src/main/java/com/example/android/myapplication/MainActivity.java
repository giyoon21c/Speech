package com.example.android.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private CountRecorder mCountRecorder;

    private final CountRecorder.Callback mCountCallback = new CountRecorder.Callback() {
        @Override
        public void onCountStart() {
         }

        @Override
        public void onCount(int num) {
            L.m(""+num);
         }

        @Override
        public void onCountEnd() {
         }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startCountRecorder();
    }

    private void startCountRecorder() {
        mCountRecorder = new CountRecorder(mCountCallback);
        mCountRecorder.start();
    }
}