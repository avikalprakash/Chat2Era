package com.example.lue.erachat.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lue.erachat.Activity.FireBase.MyFirebaseInstanceIdService;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.R;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.sample.calling.*;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends com.sinch.android.rtc.sample.calling.BaseActivity implements SinchService.StartFailedListener {

    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    private ArrayList<String> country_code = new ArrayList<String>();
    private ArrayList<String> country = new ArrayList<String>();
    private ArrayList<String> country_withCode = new ArrayList<String>();
    MyFirebaseInstanceIdService myFirebaseInstanceIDService;

    MyAsync myAsync;
    LinearLayout verifylayout;
    Spinner spinner;
    Dialog dialog;
    EditText reg_Mobile,reg_Email,reg_username;
    LinearLayout Email;
    String mobile="";
    String email="";
    final String token="";
    private ProgressDialog mSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Registration");
         myAsync=new MyAsync();
        //Dialog
        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        myFirebaseInstanceIDService=new MyFirebaseInstanceIdService();
        //Firebase.setAndroidContext(this);
      /*  spinner=(Spinner)findViewById(R.id.citylist);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.CountryName, android.R.layout.simple_spinner_item);
           // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SpinnerItem"," "+"Selected"+i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        reg_Mobile=(EditText)findViewById(R.id.phonenumber);
        reg_Email=(EditText)findViewById(R.id.email_reg);
        reg_username=(EditText)findViewById(R.id.username) ;
        Email=(LinearLayout) findViewById(R.id.verifylayout);
        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MYNUMBER", mobile).commit();

               String mobile1=reg_Mobile.getText().toString();
                 mobile="+60"+mobile1;
                Log.d("gcyhych"," "+mobile);
                email=reg_Email.getText().toString();
                loginClicked();
                loadData();
            }
        });


    }

    private void loginClicked() {
        String userName = reg_Mobile.getText().toString();
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("myNumber", userName);
        editor.commit();
        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
            showSpinner();
        } else {
          //  openPlaceCallActivity();
        }
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

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        if (mSpinner != null) {
            mSpinner.dismiss();
        }
    }

    @Override
    public void onStarted() {
       // openPlaceCallActivity();
    }

    private void openPlaceCallActivity() {
        Intent mainActivity = new Intent(this, PlaceCallActivity.class);
        startActivity(mainActivity);
    }

    private void showSpinner() {
       /* mSpinner = new ProgressDialog(this);
        mSpinner.setTitle("Logging in");
        mSpinner.setMessage("Please wait...");
        mSpinner.show();*/
    }

    class MyAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s="";
            SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
            final String token=preferences.getString(getString(R.string.FCM_TOKEN)," ");
            Log.d("sharde",""+token);

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/registration");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate( "mobile", mobile);
                jsonObject.accumulate("email",email);
                jsonObject.accumulate("token",token);
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
            // Save to SharedPreferences
            try {
                JSONObject jsonObject=new JSONObject(s);
                String message=jsonObject.getString("message");
                String User=jsonObject.getString("user_id");
              if(message.equals("Registered successfully!")){
                  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("myuserid", User).commit();
                  Intent intent=new Intent(MainActivity.this,termsOfServices.class);
                  intent.putExtra("number",mobile);
                  startActivity(intent);
                  finish();
              }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(MainActivity.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(MainActivity.this,"NO net avalable",Toast.LENGTH_LONG).show();
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

}
