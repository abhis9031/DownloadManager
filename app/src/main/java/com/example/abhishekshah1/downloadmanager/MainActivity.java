package com.example.abhishekshah1.downloadmanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends Activity implements FetchDataThread.Callback {

    byte array[];
    EditText editTexturl;
    TextView numberTextparts;
    static ProgressBar mainprogressBar;
    Button button;
    ListView listView;
    TextView Downloading;
    static Context context;
    TextView time;
    long starttime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedtime = 0L;
    int t = 1;
    int secs = 0;
    int mins = 0;
    int milliseconds = 0;
    Toast toast1;
    String totaltime;
    Handler handler = new Handler();
    NetworkInfo info;

    int id =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.context = this;
        setContentView(R.layout.activity_main);
        editTexturl = (EditText) findViewById(R.id.editTexturl);
        numberTextparts = (TextView) findViewById(R.id.numberTextparts);
        mainprogressBar = (ProgressBar) findViewById(R.id.mainprogressBar);
        button = (Button) findViewById(R.id.button);
        listView = (ListView) findViewById(R.id.listView);
        time = (TextView) findViewById(R.id.timer);
        Downloading = (TextView) findViewById(R.id.Downloading);
        mainprogressBar.setVisibility(View.VISIBLE);
        mainprogressBar.setMax(100);
        int id = 1;
        int number= NumberOfCpu.getNumCores();
        numberTextparts.setText("           =                   "+number);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            // internet is there.
            Toast toast = Toast.makeText(this, "You Are Connected To Interenet", Toast.LENGTH_LONG);
            toast.show();

        } else {
            // internet is not there.
            Toast toast = Toast.makeText(this, "You Are Not Connected To Internet", Toast.LENGTH_LONG);
            toast.show();
        }
        Toast toast = Toast.makeText(this, "Number Of Cores="+number, Toast.LENGTH_LONG);
        toast1 = Toast.makeText(this, "Either url is not valid or you are not connected to internet", Toast.LENGTH_LONG);
        toast.show();

            button.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    info = conMgr.getActiveNetworkInfo();
                    if (editTexturl.length()>5 && (info != null&&info.isConnected())) {
                        int number = NumberOfCpu.getNumCores();
                        String url1 = editTexturl.getText().toString();
                        //String parts = editTextparts.getText().toString();
                        int part = number;

                        if (t == 1) {
                            starttime = SystemClock.uptimeMillis();
                            handler.postDelayed(updateTimer, 0);
                            t = 0;
                        }

                        Background.abc(url1, part,updateTimer,handler);
                        startService(new Intent(MainActivity.this, Background.class));
                    }
                    else {
                        toast1.show();
                    }
                }
            });
    }
    public static void updateDownloadProgress(final int progress){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainprogressBar.setProgress(progress);
            }
        });
    }

    public Runnable updateTimer = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - starttime;
            updatedtime = timeSwapBuff + timeInMilliseconds;
            secs = (int) (updatedtime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            milliseconds = (int) (updatedtime % 1000);
            time.setText("" + mins + ":" + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            time.setTextColor(Color.RED);
            handler.postDelayed(this, 0);
            totaltime=mins+":"+secs+":"+milliseconds;
        }};
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void DataFetched(File file) {

    }

    @Override
    public void OnDownloadComplete() {
        starttime = 0L;
        timeInMilliseconds = 0L;
        timeSwapBuff = 0L;
        updatedtime = 0L;
        t = 1;
        secs = 0;
        mins = 0;
        milliseconds = 0;
        handler.removeCallbacks(updateTimer);

        Toast toast = Toast.makeText(this, "Total time taken="+totaltime, Toast.LENGTH_LONG);
        toast.show();


    }

}

