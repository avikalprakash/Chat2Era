package com.example.lue.erachat.Activity.Other;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.ImageViewActivity;
import com.example.lue.erachat.Activity.Adaptor.ItemListAdaptor;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VideoViewActivity extends AppCompatActivity {
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    String VideoDataString="";
    String Id="";
    Dialog dialog;
    String myuser_id="";
    ImageView send,videoPriview;
    String videoData = "";
    DatabaseHandler databaseHandler;
    String SendernameTitleBar;
    String SenderImageTitleBar;
    byte [] vdo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        videoPriview=(ImageView)findViewById(R.id.videoView2);

        //
        databaseHandler=new DatabaseHandler(getApplicationContext());

        //Dialog
        dialog = new Dialog(VideoViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //sharedprf
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");


        //Intent
        Intent intent=getIntent();
        VideoDataString=intent.getStringExtra("VideoString");
        Id=intent.getStringExtra("ReciverId");

        if(intent.getStringExtra("SenderName")!=null){
            SendernameTitleBar=intent.getStringExtra("SenderName");
        }

        if(intent.getStringExtra("SenderImage")!=null){
            SenderImageTitleBar=intent.getStringExtra("SenderImage");
        }
         if(!VideoDataString.equals("")) {

             Uri uri= Uri.parse(VideoDataString);
             Log.d("cbuuchnu", ""+uri);
             String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
             Cursor cursor = managedQuery(uri, projection, null, null, null);

             cursor.moveToFirst();
             String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
             Log.d("File Name:", filePath);

             Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
             // Setting the thumbnail of the video in to the image view
             videoPriview.setImageBitmap(thumb);
             InputStream inputStream = null;
// Converting the video in to the bytes
             try {
                 inputStream = getContentResolver().openInputStream(uri);
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             }
             int bufferSize = 1024;
             byte[] buffer = new byte[bufferSize];
             ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
             int len = 0;
             try {
                 while ((len = inputStream.read(buffer)) != -1) {
                     byteBuffer.write(buffer, 0, len);
                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }
             System.out.println("converted!");


             //Converting bytes into base64
             videoData = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
             Log.d("VideoData**>  ", videoData);


           /*  String sinSaltoFinal2 = videoData.trim();
             String sinsinSalto2 = sinSaltoFinal2.replaceAll("\n", "");
             Log.d("VideoData**>  ", sinsinSalto2);*/

             //baseVideo = sinsinSalto2;
         }

        send=(ImageView)findViewById(R.id.imageView20);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                databaseHandler.insertChatRoom(new Messages("", myuser_id, Id, "", "",VideoDataString, "","","","",date, "video", "","","","",""));
                loadData();
            }
        });
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    // pb.setVisibility(View.VISIBLE);
                      dialog.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    //  pb.setVisibility(View.GONE);
                     dialog.hide();
                    break;
            }
            return false;
        }
    });

    class MyAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/oneToOneVideo");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate( "user_id", Id);

                jsonObject.accumulate("sender_id",myuser_id);

                    jsonObject.accumulate("video",VideoDataString );

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                // httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcute", "" + s);
            VideoDataString="";
            Intent in = new Intent(VideoViewActivity.this, FragmentChat.class);
            in.putExtra("SenderId", Id);
            in.putExtra("SenderName", SendernameTitleBar);
            in.putExtra("SenderImage", SenderImageTitleBar);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
        }
    }

    public void  loadData(){
        myAsync=new MyAsync();

            if(!VideoDataString.equals("")) {
                myAsync.execute();

        }else{
            Toast.makeText(VideoViewActivity.this,"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }


    private String readResponse(HttpResponse httpResponse) {
        InputStream is=null;
        String return_text="";
        try {
            is=httpResponse.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
            Log.d("return_text",""+return_text);
        } catch (Exception e)
        {

        }
        return return_text;


    }
    @Override
    public void onPause() {
        super.onPause();
        //dialog.dismiss();
        try {
            if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
            {
                myAsync.cancel(true);
            }
        }catch (Exception e){
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }

}
