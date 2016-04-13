package com.example.abhishekshah1.downloadmanager;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Background extends Service  implements FetchDataThread.Callback{

   static String url;
   static int part;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
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
    static Runnable updateTimer;
    static Handler handler;

    public static void abc(String url1,int parts,Runnable updateTimer1,Handler handler1)
    {
        url=url1;

        part=parts;
        updateTimer=updateTimer1;
        handler=handler1;

    }

    private static String TAG = "Service";
    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
  MainActivity obj=new MainActivity();


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "FirstService started");
        if (obj.array != null) {
            obj.Downloading.setText("DOWNLOADED");
        } else {
            mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(
                    getApplicationContext()).setSmallIcon(R.drawable.images).setContentTitle("My notification").setContentText("Hello World!").setOngoing(true);;
            new FetchDataThread().fetchDataStore(getApplicationContext(),url,Background.this,mNotifyManager,mBuilder);
        } return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        this.stopSelf();
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.d(TAG, "FirstService destroyed");
    }

    @Override
    public void DataFetched(File file) {
        if (file != null) {
            // Downloading.setText("DOWNLOADED");
            Toast toast = Toast.makeText(this, "Downloaded", Toast.LENGTH_LONG);
            toast.show();
            } else {
            Toast toast = Toast.makeText(this, "No Data Downloadable", Toast.LENGTH_LONG);
            toast.show();
        }

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