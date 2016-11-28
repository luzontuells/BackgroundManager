package com.example.a5alumno.backgroundmanager;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final static String TAG_MAIN_ACTIVITYY = "Main Activity";

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button threadBtn = (Button) this.findViewById(R.id.btnStartThread);
        threadBtn.setOnClickListener(this);

        final Button asyncBtn = (Button) this.findViewById(R.id.btnStartAsyncTask);
        asyncBtn.setOnClickListener(this);

        this.mProgressBar = (ProgressBar) this.findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnStartThread){
            //Empieza el Thread
            new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.i(MainActivity.TAG_MAIN_ACTIVITYY, "Thread started");

                    runOnUiThread(new Runnable() { //Hay que hacer esto para poder hacer el Toast
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Sleeping...",Toast.LENGTH_SHORT).show();
                        }
                    });

                    sleepForAWhile(20);

                }
            }).start();
        }else if (view.getId()==R.id.btnStartAsyncTask){
            //Empieza la AsyncTask
            new MyAsyncTask(this).execute(1);
        }
    }

    private void sleepForAWhile (int numSeconds) {
        long endTime = System.currentTimeMillis() + (numSeconds * 1000);

        while (System.currentTimeMillis() < endTime){
            synchronized (this) {
                try {
                    Log.i(MainActivity.TAG_MAIN_ACTIVITYY, "Sleeping...");
                    this.wait(endTime - System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, String>{

        private Context mContext;

        public MyAsyncTask(Context anyContext){ this.mContext = anyContext;}

        @Override
        protected String doInBackground(Integer[] params) { // Integer... params
            for (int idx = 1 ; idx <= 5; idx++){
                sleepForAWhile(params[0]);
                publishProgress(idx * 20);
            }
            return "AsyncTask finished";
        }
        @Override
        protected void onProgressUpdate(Integer... values){
            super.onProgressUpdate(values);
            Log.d(MainActivity.TAG_MAIN_ACTIVITYY, mProgressBar==null?"Is Null":"Not Null");
            mProgressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String retString){
            super.onPostExecute(retString);
            Toast.makeText(this.mContext, retString, Toast.LENGTH_SHORT).show();
//            mProgressBar.setProgress(0);
//            setProgressToZero();
        }

    }

    private void setProgressToZero() {
                    mProgressBar.setProgress(0);
    }
}





