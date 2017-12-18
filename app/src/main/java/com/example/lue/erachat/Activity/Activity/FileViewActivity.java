package com.example.lue.erachat.Activity.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.Activity.location.MapLocationActivity;
import com.example.lue.erachat.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FileViewActivity extends AppCompatActivity {
    String FileString;
    String ReciverId;
    String GropId;
    String SendernameTitleBar;
    String SenderImageTitleBar;
    Bitmap imagefromActivity;
    ImageView imageFile;
    ImageView send;
    DatabaseHandler databaseHandler;
    String Senderid="";
    String myuser_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        imageFile = (ImageView)findViewById(R.id.imageFile);
        send=(ImageView)findViewById(R.id.imageView20);
        databaseHandler=new DatabaseHandler(getApplicationContext());
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
        Intent intent = getIntent();
        Uri uri;
        FileString = intent.getStringExtra("FileString");
        uri = Uri.parse(FileString);
        Senderid=intent.getStringExtra("id");
        ReciverId = intent.getStringExtra("ReciverId");
        if (intent.getStringExtra("GroupId") != null) {
            GropId = intent.getStringExtra("GroupId");
        }
        if (intent.getStringExtra("SenderName") != null) {
            SendernameTitleBar = intent.getStringExtra("SenderName");
        }

        if (intent.getStringExtra("SenderImage") != null) {
            SenderImageTitleBar = intent.getStringExtra("SenderImage");
        }
        /*try {
           // imagefromActivity = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            File auxFile = new File(uri.getPath());
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap image = BitmapFactory.decodeFile(auxFile.getAbsolutePath(),bmOptions);
            //imagefromActivity= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            imageFile.setImageBitmap(image);
            String s;

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                if(!GropId.equals("")){
                    databaseHandler.insertGroupChat(new Messages(GropId,"",myuser_id,"",date,"location","","","","",FileString,"","","",""));

                }else {
                    databaseHandler.insertChatRoom(new Messages("", myuser_id, Senderid, "", "", "", "", "", "", "", date, "location", FileString,"",""));
                }

               /* if(!GropId.equals("")){
                    Intent intent = new Intent(FileViewActivity.this, FragmentChat.class);
                    intent.putExtra("GroupId",GropId);
                    intent.putExtra("SenderName", SendernameTitleBar);
                    intent.putExtra("SenderImage", SenderImageTitleBar);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(FileViewActivity.this, FragmentChat.class);
                    intent.putExtra("SenderId", Senderid);
                    intent.putExtra("SenderName", SendernameTitleBar);
                    intent.putExtra("SenderImage", SenderImageTitleBar);
                    startActivity(intent);
                }*/
            }

        });

    }
}