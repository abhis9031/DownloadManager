package com.example.abhishekshah1.downloadmanager;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Abhishek.Shah1 on 6/29/2015.
 */
public class FetchDataThread {
    public interface Callback {
        public void DataFetched(File file);
        public void OnDownloadComplete();

    }
    String Part="Part";
    int part=1;
    File file;

    NotificationManager nm1;
    NotificationCompat.Builder mbuilder1;
    private Callback mCallback = null;
    public void fetchDataStore(final Context context,final  String url, final Callback callback,NotificationManager nm, NotificationCompat.Builder mBuilder) {
            nm1=nm;
        mbuilder1=mBuilder;
        Thread thread2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpURLConnection connect1 = null;
                    try {
                        URL requestUrl = new URL(url);
                        connect1 = (HttpURLConnection) requestUrl.openConnection();
                        connect1.connect();
                        int responseCode = connect1.getResponseCode();

                        if (responseCode == 200) {
                                downloadFileFromServer(url,connect1);
                        }
                            if (callback != null) {

                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.DataFetched(file);
                                        callback.OnDownloadComplete();
                                    }
                                });
                            } else {

                            }
                        }
                    catch (Exception e)
                    {

                    }
                    }
            });
            thread2.start();
        }
    public void downloadFileFromServer(String urlString,HttpURLConnection connect1) throws MalformedURLException, IOException
    {
       InputStream in = null;
        FileOutputStream fout = null;

        try
        {
            String S=connect1.toString();
           S=S.substring(S.lastIndexOf("/"),S.length());
            URL url = new URL(urlString);
            File f=new File("/sdcard/Capture/");
            f.mkdirs();
            file = new File("/sdcard/Capture/"+S);
            file.createNewFile();
            BufferedInputStream bis = new BufferedInputStream(connect1.getInputStream());
           // ByteArrayBuffer baf = new ByteArrayBuffer(50);
           in = new BufferedInputStream(connect1.getInputStream());
            fout = new FileOutputStream(file);
            int lenghtOfFile = connect1.getContentLength();
            byte data[] = new byte[1024];
            int count;
            float onepercent=lenghtOfFile/100;
            float total = 0f;
            int x=0;
            int y=1024;
            int i =1;
            ProgressDialog mainprogressBar;

            while ((count = in.read(data)) != -1)
            {
                fout.write(data, 0, count);
                total += count;
                Log.d("Test", total +" "+(int) ((total/lenghtOfFile)*100)+" "+lenghtOfFile);
                if(total>=(onepercent*i)&&i<101)
                {
                    int progress = (int) ((total/lenghtOfFile)*100);
                    MainActivity.updateDownloadProgress(progress);
                    mbuilder1.setProgress(100, progress, false);
                    nm1.notify(1, mbuilder1.build());
                    i++;

                    }

            }
        }
        finally
        {
            if (in != null)
                in.close();
            if (fout != null)
                fout.close();
        }

        mbuilder1.setContentText("Download complete").setProgress(0, 0, false).setOngoing(false);
        nm1.notify(1, mbuilder1.build());
        System.out.println("Done");

    }
}

