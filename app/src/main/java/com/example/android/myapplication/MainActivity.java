package com.example.android.myapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.android.myapplication.R.id.buttonStartCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private CountRecorder mCountRecorder;
    public TextView mStatusUpdate;
    private Handler handler;

    private Button mButtonStartCallback;
    private Button mButtonStopCallback;

    private Button mButtonStartAsyncTask;
    private Button mButtonStopAsyncTask;

    private Button mButtonStartService;
    private Button mButtonStopService;

    private Button mButtonBindService;
    private Button mButtonUnBindService;
    private Button mButtonGetRandomNumber;

    /*
       async task related declaration
     */
    private MyAsyncTask myAsyncTask;
    boolean mStopLoop = true;

    /*
       Service related declarations
     */
    private Intent serviceIntent;
    private MyService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;


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

        mButtonStartCallback = (Button) findViewById(buttonStartCallback);
        mButtonStartCallback.setOnClickListener(this);

        mButtonStopCallback = (Button) findViewById(R.id.buttonStopCallback);
        mButtonStopCallback.setOnClickListener(this);

        mButtonStartAsyncTask = (Button) findViewById(R.id.buttonStartAsyncTask);
        mButtonStartAsyncTask.setOnClickListener(this);

        mButtonStopAsyncTask = (Button) findViewById(R.id.buttonStopAsyncTask);
        mButtonStopAsyncTask.setOnClickListener(this);

        mButtonStartService = (Button) findViewById(R.id.buttonStartService);
        mButtonStartService.setOnClickListener(this);

        mButtonStopService = (Button) findViewById(R.id.buttonStopService);
        mButtonStopService.setOnClickListener(this);

        mButtonBindService = (Button) findViewById(R.id.buttonBindService);
        mButtonBindService.setOnClickListener(this);

        mButtonUnBindService = (Button) findViewById(R.id.buttonUnBindService);
        mButtonUnBindService.setOnClickListener(this);

        mButtonGetRandomNumber = (Button) findViewById(R.id.buttonGetRandomNumber);
        mButtonGetRandomNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStartCallback: startCallback();
                break;
            case R.id.buttonStopCallback: stopCallback();
                break;

            case R.id.buttonStartAsyncTask:
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute(0); // pass in the initial value
                break;
            case R.id.buttonStopAsyncTask:
                //mStopLoop = false;
                myAsyncTask.cancel(true);
                break;

            case R.id.buttonStartService:
                serviceIntent = new Intent(getApplicationContext(), MyService.class);
                startService(serviceIntent);
                break;
            case R.id.buttonStopService:
                Log.i("SERVICE", "stopping service");
                break;
            case R.id.buttonBindService:
                bindService();
                break;
            case R.id.buttonUnBindService:
                unbindService();
                break;
            case R.id.buttonGetRandomNumber:
                setRandomNumber();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*
       Using Callback to update the UI
     */
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

    /*
       Using AsyncTask to update the UI
     */
    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        private int counter;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            counter = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            counter = integers[0];
            while (mStopLoop) {
                try {
                    Thread.sleep(1000);
                    counter++;
                    publishProgress(counter);
                } catch (InterruptedException e) {
                    Log.i("ASYNCTASK", e.getMessage());
                }
            }
            return counter;
        }

        @Override
        protected void onProgressUpdate(Integer...values) {
            super.onProgressUpdate(values);
            mStatusUpdate = (TextView) findViewById(R.id.textViewThreadCount);
            mStatusUpdate.setText(""+values[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            counter = integer;
        }
    }

    /*
       Using Service to update the UI
     */
    private void bindService() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName,
                                               IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder =
                            (MyService.MyServiceBinder) iBinder;
                    myService = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isServiceBound = false;
                }
            };
        }
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void setRandomNumber() {
        if (isServiceBound) {
            mStatusUpdate = (TextView) findViewById(R.id.textViewThreadCount);
            mStatusUpdate.setText(""+myService.getRandomNumber());
        } else {
            mStatusUpdate.setText("Service not bound");
        }
    }
}

