package com.example.android.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CountRecorder mCountRecorder;
    public TextView mStatusUpdate;
    private Handler handler;
    private Button mButtonStartCallback;
    private Button mButtonStopCallback;

    private Button mButtonStartService;
    private Button mButtonStopService;

    private Intent serviceIntent;


    private final CountRecorder.Callback mCountCallback = new CountRecorder.Callback() {
        @Override
        public void onCountStart() {
         }

        @Override
        public void onCount(int num) {
            L.m(""+num);
            updateUI(num);
         }

        @Override
        public void onCountEnd() {
         }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler(); // Initialize the Handler from the Main Thread
                                 // One must use a Handler to modify a view from the main thread.

        mButtonStartCallback = (Button) findViewById(R.id.buttonStartCallback);
        mButtonStartCallback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startCallback();
            }
        });

        mButtonStopCallback = (Button) findViewById(R.id.buttonStopCallback);
        mButtonStopCallback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopCallback();
            }
        });


        // explicit-intent
        serviceIntent = new Intent(getApplicationContext(), MyService.class);
        mButtonStartService = (Button) findViewById(R.id.buttonStartService);
        mButtonStartService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startService(serviceIntent);
            }
        });

        mButtonStopService = (Button) findViewById(R.id.buttonStopService);
        mButtonStopService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //stopService();
                Log.i("SERVICE", "stopping service")
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void startCallback() {
        startCountRecorder();
    }

    protected void stopCallback() {
        mCountRecorder.stop();
    }

    protected void updateUI(final int num) {
        handler.post(new Runnable() {
            public void run() {
                //log("NEW TAG!");
                mStatusUpdate = (TextView) findViewById(R.id.textViewThreadCount);
                mStatusUpdate.setText(""+num);
            }
        });
    }

    private void startCountRecorder() {
        mCountRecorder = new CountRecorder(mCountCallback);
        mCountRecorder.start();
    }
}