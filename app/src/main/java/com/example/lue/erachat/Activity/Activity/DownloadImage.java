package com.example.lue.erachat.Activity.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Other.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lue on 04-09-2017.
 */

public class DownloadImage {

    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
    String date1 = dateFormat1.format(new Date());
    String path = Environment
            .getExternalStorageDirectory().toString()
            + "/2011.jpg"+date1;
    File file;

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                file = new File( path , "Demo Picture.jpg" ) ;
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream

                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {


        }

    }
    public void loadUrlImage(String url){

        new DownloadFileFromURL().execute(url);

    }
   public String loadPath(){
       return path;

   }
    private File getDir1() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date1 = dateFormat.format(new Date());
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "EraChat/" + date1);

    }
}
