package com.example.lue.erachat.Activity.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ItemListAdaptor;
import com.example.lue.erachat.Activity.FireBase.MyFirebaseMessagingService;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.MainActivity;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.lue.erachat.R.id.cropImageView;

public class ImageViewActivity extends AppCompatActivity {
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    String ImageString;
    String REGSID="";
    String SenderId="";
    String ReciverId;
    CropImageView imagePrevirew;
    ImageView send,crop_image,WriteText;
    DatabaseHandler databaseHandler;
    ItemListAdaptor itemListAdaptor;
    ArrayList<Messages> chatroom=new ArrayList<>();
    ArrayList<Messages>Chat_List=new ArrayList<>();
    String myuser_id="";
    Dialog dialog;
    Bitmap cropped;
    String  ImageINString="";
    TextView writetextView;
    String VideoString="";
    Bitmap imagefromActivity;
    int position=0;
    String GropId="";
    String SendernameTitleBar;
    String SenderImageTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
         databaseHandler=new DatabaseHandler(getApplicationContext());
        imagePrevirew=(CropImageView)findViewById(R.id.showpreview);
        crop_image=(ImageView)findViewById(R.id.crop_image);
        WriteText=(ImageView)findViewById(R.id.imageView37);
        writetextView=(TextView)findViewById(R.id.textView63);

        //Dialog
        dialog = new Dialog(ImageViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //sharedprf
        myuser_id= PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");

        //Intent
        Intent intent=getIntent();
        Uri uri;
        ImageString=intent.getStringExtra("ImageString");
        uri=Uri.parse(ImageString) ;
        ReciverId=intent.getStringExtra("ReciverId");
        if(intent.getStringExtra("GroupId")!=null){
            GropId=intent.getStringExtra("GroupId");
        }
        if(intent.getStringExtra("SenderName")!=null){
            SendernameTitleBar=intent.getStringExtra("SenderName");
        }

        if(intent.getStringExtra("SenderImage")!=null){
            SenderImageTitleBar=intent.getStringExtra("SenderImage");
        }

       Chat_List=databaseHandler.getMessage();
        try {
            imagefromActivity = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            imagePrevirew.setImageBitmap(imagefromActivity);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageINString=getStringImage(imagefromActivity);
        send=(ImageView)findViewById(R.id.imageView20);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                Log.d("dcuhucdh",""+ImageINString);
                if(!ImageINString.equals("")){
                    if(GropId.equals("")) {

                        if (containsCardno(Chat_List, SenderId)) {
                            updateMessage();
                            databaseHandler.insertChatRoom(new Messages("", myuser_id, ReciverId, "", ImageINString, "", "", "", "", "", date, "image", "", "", "", "",""));


                        } else {
                            databaseHandler.insertChatRoom(new Messages("", myuser_id, ReciverId, "", ImageINString, "", "", "", "", "", date, "image", "", "", "", "",""));

                        }
                    }else {
                        databaseHandler.insertGroupChat(new Messages(GropId, "", myuser_id, "", date, "image", "", ImageINString, "", "", "", "", "", "", ""));

                    }
                }else {
                    if(GropId.equals("")) {
                        if (containsCardno(Chat_List, SenderId)) {
                            updateMessage();
                            databaseHandler.insertChatRoom(new Messages("", myuser_id, ReciverId, "", ImageINString, "", "", "", "", "", date, "image", "", "", ""));


                        } else {
                            databaseHandler.insertChatRoom(new Messages("", myuser_id, ReciverId, "", ImageINString, "", "", "", "", "", date, "image", "", "", "", "",""));

                        }
                    }else {
                        databaseHandler.insertGroupChat(new Messages(GropId, "", myuser_id, "", date, "image", "", ImageINString, "", "", "", "", "", "", ""));

                    }
                }
                loadData();
            }
        });
        crop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage(imagefromActivity);
            }
        });
        WriteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                writetextView.setVisibility(View.VISIBLE);
            }
        });

    }


    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                      dialog.show();
                    break;
                case HIDE_PROCESS_DIALOG :
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
            HttpPost httpPost = null;
            try {
                HttpClient httpClient = new DefaultHttpClient();
                if(GropId.equals("")) {
                    httpPost = new HttpPost("http://erachat.condoassist2u.com/api/oneToOneImage");
                }else {
                    httpPost = new HttpPost("http://erachat.condoassist2u.com/api/sendGroupMessage");
                }

                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();
                if(GropId.equals("")) {
                    jsonObject.accumulate("user_id", ReciverId);

                    jsonObject.accumulate("sender_id", myuser_id);
                    if (!ImageINString.equals("")) {
                        jsonObject.accumulate("image", ImageINString);
                    } else {
                        jsonObject.accumulate("image", ImageString);

                    }
                }else {
                    jsonObject.accumulate("group_id", GropId);
                    jsonObject.accumulate("sender_id", myuser_id);
                    if (!ImageINString.equals("")) {
                        jsonObject.accumulate("message", ImageINString);
                    }
                    jsonObject.accumulate("type", "image");
                }

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
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
            ImageString="";
            if(GropId.equals("")) {
                Intent in = new Intent(ImageViewActivity.this, FragmentChat.class);
                in.putExtra("SenderId", ReciverId);
                in.putExtra("SenderName", SendernameTitleBar);
                in.putExtra("SenderImage", SenderImageTitleBar);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }else {
                Intent in = new Intent(ImageViewActivity.this, FragmentChat.class);
                in.putExtra("GroupId", GropId);
                in.putExtra("SenderName", SendernameTitleBar);
                in.putExtra("SenderImage", SenderImageTitleBar);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
            }

        }
    }
    boolean containsCardno(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
                if( (item.getMessageSenderId().equals(myuser_id)&&item.getMessageReciverId().equals(ReciverId))||((item.getMessageSenderId().equals(ReciverId)&&item.getMessageReciverId().equals(myuser_id)))){
                    return true;
                }

        }
        return false;
    }
    public void  loadData(){
        myAsync=new MyAsync();

        if (Utils.isNetworkAvailable(ImageViewActivity.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            if(!ImageString.equals("")) {
                myAsync.execute();
            }/*else {
                postImage.execute();
            }*/
        }else{
            Toast.makeText(ImageViewActivity.this,"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }
    public void updateMessage(){
        databaseHandler=new DatabaseHandler(getApplicationContext());
        Chat_List=databaseHandler.getMessage();
        for (int i = 0; i < Chat_List.size(); i++) {

            if (((Chat_List.get(i).getMessageSenderId().equals(SenderId))&&Chat_List.get(i).getMessageReciverId().equals(myuser_id))||((Chat_List.get(i).getMessageSenderId().equals(myuser_id))&&Chat_List.get(i).getMessageReciverId().equals(SenderId))) {

                position = i;

            }

        }
        Log.d("hbdcun",""+position);
        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Messages c = Chat_List.get(position);
        c.setMessageSenderId(myuser_id);
        c.setMessageReciverId(ReciverId);
            c.setMessageText("Image");

        c.setMessageType("image");
        c.setMessageInTime(date);
        // c.setSender_Name(Username);
        databaseHandler.updateMessage(c);

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
        dialog.dismiss();
        myAsync=new MyAsync();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        myAsync=new MyAsync();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    public void cropImage(Bitmap bitmap)

    {
        cropped = imagePrevirew.getCroppedImage();
          imagePrevirew.clearImage();
           imagePrevirew.setImageBitmap(cropped);
        ImageINString = getStringImage(cropped);

    }
    public String getStringImage(Bitmap bmp){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
