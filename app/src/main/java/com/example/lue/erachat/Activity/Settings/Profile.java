package com.example.lue.erachat.Activity.Settings;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.EditName;
import com.example.lue.erachat.Activity.Activity.EditStatus;
import com.example.lue.erachat.Activity.MainActivity;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.FileUtils;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by lue on 08-06-2017.
 */

public class Profile extends AppCompatActivity {
    CircleImageView imageView;
    ImageView profile_takepic, profile_choosepic;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView EditName, EditStatus;
     TextView number,email;
    MyAsync myAsync;

    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Dialog dialog;
    String myuser_id="";
    //url
    String photoUrl = "http://erachat.condoassist2u.com/api/setPhoto";
    String myBase64Image="";

    TextView name, status;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");

        //database
        databaseHandler=new DatabaseHandler(getApplicationContext());


        //asynktask
        myAsync=new MyAsync();
        //Dialog
        dialog = new Dialog(Profile.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //sharedprf
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
        Log.d("uyUserId"," "+myuser_id);

        //intialization
        imageView = (CircleImageView) findViewById(R.id.profile_image);
        name = (TextView) findViewById(R.id.name);
        status = (TextView) findViewById(R.id.status);
        EditName = (ImageView) findViewById(R.id.edit_name);
        EditStatus = (ImageView) findViewById(R.id.edit_status);
        number=(TextView)findViewById(R.id.textView3);
        email=(TextView)findViewById(R.id.textView4) ;

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);
            }
        });
        EditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.lue.erachat.Activity.Activity.EditName.class);
                startActivity(intent);
            }
        });
        EditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), com.example.lue.erachat.Activity.Activity.EditStatus.class);
                startActivity(intent);
            }
        });
        //SharedPrf
        String myuser_name = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MYNAME", "");
        String myuser_status = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MYSTATUS", "");
        String myuser_profilePIc = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MYPROFILE", "");
        Log.d("myUserName", " " + myuser_name);
        ArrayList<User>ContactList =databaseHandler.getContact();
        for(int i=0;i<ContactList.size();i++){
                 if(ContactList.get(i).getContactId().equals(myuser_id)) {
                     String name = ContactList.get(i).getContactEmail();
                     String nuamber=ContactList.get(i).getContactNumber();
                     number.setText(nuamber);
                     Log.d("myUserdxdsName", " " + name+nuamber);
                 }

        }

        if (!myuser_name.equals("")) {
            name.setText(myuser_name);
        }
        if (!myuser_status.equals("")) {
            status.setText(myuser_status);
        }
        if(!myuser_profilePIc.equals("")){
            Picasso.with(getApplicationContext()).load(myuser_profilePIc).into(imageView);
        }


    }

    public void onShowPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.dialogchoose_profile, null, false);
        // find the ListView in the popup layout
        profile_takepic = (ImageView) inflatedView.findViewById(R.id.imageView40);
        profile_choosepic = (ImageView) inflatedView.findViewById(R.id.imageView42);
        // get device size
        Display display = this.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        profile_choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        profile_takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        //mDeviceHeight = size.y;
        // fill the data to the list items
        // set height depends on the device size
        PopupWindow popupWindow = new PopupWindow(inflatedView, size.x - 0, size.y / 6, true);

        // set a background drawable with rounders corners
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        // make it focusable to show the keyboard to enter in `EditText`
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // make it outside touchable to dismiss the popup window
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("Imageview", String.valueOf(bitmap));
                imageView.setImageBitmap(bitmap);
                myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG,40);
              loadData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.d("Imageview", " " + imageBitmap);


             myBase64Image = encodeToBase641(imageBitmap, Bitmap.CompressFormat.JPEG,40);
            loadData();
        }


    }

    //encoding image
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    public static String encodeToBase641(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image,image.getWidth(),image.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        rotatedBitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
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
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";

            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/updatePhoto");
                httpPost.setHeader("Content-type", "application/json");
                Log.d("useridssss"," "+myuser_id);
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);
                jsonObject.accumulate("photo",myBase64Image);

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
            try {
                JSONObject jsonObject=new JSONObject(s);
                String error=jsonObject.getString("error");
                String message=jsonObject.getString("message");
                if(error.equals("false")){

                    myBase64Image="";
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MYPROFILE", message).commit();




                    Intent intent=new Intent(Profile.this, Profile.class);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(Profile.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(Profile.this,"NO net avalable",Toast.LENGTH_LONG).show();
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
        dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }




    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(Profile.this,SettingOption.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
