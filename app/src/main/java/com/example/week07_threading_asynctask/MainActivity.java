package com.example.week07_threading_asynctask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Need to bind views that we created in XML
    private ProgressBar mProgressBar;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mTv = (TextView) findViewById(R.id.smryTv);

        mTv.setOnClickListener(this::enableAlert);

        //Created an object of the async task (dt) and execute on the listed values
        DownloadTask dt = new DownloadTask();
        dt.execute("https://google.com",
                "https://wikipedia.com",
                "https://www.farmingdale.edu");
    }

    //Function to create an alert
    private void enableAlert(View view) {
        //Create a new alert dialog builder object
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Add message on alert
        alert.setMessage("My First Alert");

        //Add positive button
        alert.setPositiveButton("Agree", (v,a) -> {
            Toast.makeText(MainActivity.this, "You Clicked Agree on the Alert", Toast.LENGTH_LONG).show();
        });

        alert.setNegativeButton("Disagree", (v,a) -> {
            Toast.makeText(MainActivity.this, "You Clicked Disagree on the Alert", Toast.LENGTH_LONG).show();
        });

        alert.create().show();

    }

    //Function to simulate download
    private boolean downloadURL(String URL){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    //Create our own class that is a child of AsyncTask, which makes new <simple> thread
    class DownloadTask extends AsyncTask<String, Integer, Integer>{

        //Things to do on the main thread before switching to background process
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Initialize progress bar to 0
            mProgressBar.setProgress(0);
        }

        //Background thread
        @Override
        protected Integer doInBackground(String... url) {
            //Initialize progress variable to 0
            int downloadSuccess = 0;
            for (int i=0; i < url.length; i++){
                if(downloadURL(url[i])){
                    downloadSuccess++;
                }
                //As it iterates through, publish download progress
                publishProgress((i+1)*100/url.length);
            }
            return downloadSuccess;
        }

        //Mechanism to publish information from background to main thread
        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            //Upon download complete, output "Done"
            mTv.setText("Done " + integer);
            if(integer > 2){
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        }
    }
}