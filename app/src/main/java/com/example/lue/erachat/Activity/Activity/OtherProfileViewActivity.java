package com.example.lue.erachat.Activity.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.CallActivity;
import com.example.lue.erachat.Activity.FragmentCallActivity;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.Settings.Profile;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class OtherProfileViewActivity extends Activity {
    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    private CollapsingToolbarLayout collapsingToolbarLayout2 = null;
    MyAsync myAsync;
    BlockUser blockUser;
    String UserName="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Dialog dialog;
    String Id;
    TextView status_text,mobile_text,lastupdated, current_Status;
    ImageView profile_id;
    ImageView icon_phone;
    LinearLayout block;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_view);
      /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

      //Initialization
        current_Status=(TextView)findViewById(R.id.status);
        status_text=(TextView)findViewById(R.id.status_text);
        mobile_text=(TextView)findViewById(R.id.mobile_text) ;
        profile_id=(ImageView)findViewById(R.id.profile_id);
        lastupdated=(TextView)findViewById(R.id.lastupdated);
        icon_phone=(ImageView)findViewById(R.id.icon_phone);
        block=(LinearLayout)findViewById(R.id.block);
        myAsync= new MyAsync();
        blockUser=new BlockUser();
        //Intent
        Intent intent=getIntent();
         Id=intent.getStringExtra("Id");
        Log.d("hcucnun",""+Id);
        icon_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(OtherProfileViewActivity.this, CallActivity.class);
                startActivity(intent1);
            }
        });

        //dialog
        dialog = new Dialog(OtherProfileViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

     /*   ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");*/

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(UserName);

        dynamicToolbarColor();

        toolbarTextAppernce();
        loadData();
        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OtherProfileViewActivity.this);
                alertDialogBuilder.setMessage("Do You Want Block this user");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                BlockUser();
                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.profile_big);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
             //   collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
              //  collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
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

            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getUserDetail");
                httpPost.setHeader("Content-type", "application/json");
                Log.d("useridssss"," "+myuser_id);
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",Id);

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
                JSONObject jsonObject1=new JSONObject(s);
                String error=jsonObject1.getString("error");
                String message=jsonObject1.getString("message");
                JSONArray jsonArray=new JSONArray(message);
                Log.d("bvbh","% "+jsonArray);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    String UserName=object.getString("user_name");
                    String mobile=object.getString("mobile");
                    String Photo=object.getString("photo");
                    String Status=object.getString("status");
                    String lastupdate=object.getString("updated_at");
                    String status=object.getString("current_status");

                    String[] splitStr = lastupdate.trim().split("\\s+");
                    String lastupdate1=splitStr[0];
                    Log.d("bvbh","% "+UserName);
                    collapsingToolbarLayout.setTitle(UserName);
                    status_text.setText(Status);
                    current_Status.setText(status);
                    mobile_text.setText(mobile);
                    Picasso.with(getApplicationContext()).load(Photo).into(profile_id);
                    lastupdated.setText(lastupdate1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
        }
    } class BlockUser extends AsyncTask<String,Void,String> {

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
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/blockUser");
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",Id);

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
                JSONObject jsonObject1= null;
                  jsonObject1 = new JSONObject(s);
                    String error=jsonObject1.getString("error");
                    String message=jsonObject1.getString("message");
                if(error.equals("false")){
                    Toast.makeText(getApplicationContext(),"User Blocked Successfully You are no longer able to contact with him",Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(OtherProfileViewActivity.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(OtherProfileViewActivity.this,"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }public void  BlockUser(){
        blockUser=new BlockUser();
        if (Utils.isNetworkAvailable(OtherProfileViewActivity.this) && blockUser.getStatus()!= AsyncTask.Status.RUNNING)
        {
            blockUser.execute();
        }else{
            Toast.makeText(OtherProfileViewActivity.this,"NO net avalable",Toast.LENGTH_LONG).show();
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


}
