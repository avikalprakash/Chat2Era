package com.example.lue.erachat.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.GroupChat.GroupDetail;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddToContact extends AppCompatActivity {
TextView mobile,name;
    String Id="";
    MyAsync myAsync;
    Button Add;
    String Recivermobile="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    DatabaseHandler databaseHandler;
    ArrayList<User>databseContact=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_contact);
        databaseHandler=new DatabaseHandler(getApplicationContext());
        dialog=(AVLoadingIndicatorView)findViewById(R.id.avi) ;
        Add=(Button)findViewById(R.id.button13);
        myAsync=new MyAsync();
        Intent intent=getIntent();
        Log.d("AddtoContact",intent.getStringExtra("name")+"%"+intent.getStringExtra("id"));
        Id=intent.getStringExtra("id");
        mobile=(TextView)findViewById(R.id.mobile);
        name=(TextView)findViewById(R.id.name);
        mobile.setText(intent.getStringExtra("name"));
        name.setText(intent.getStringExtra("name"));

        databseContact=databaseHandler.getContact();
        for(int i=0;i<databseContact.size();i++){
            if(mobile.getText().toString().equals(databseContact.get(i).getContactNumber())){
                Id=databseContact.get(i).getContactId();
            }
        }
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Recivermobile=mobile.getText().toString();
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
            Log.d("werftghy", "iiiiiiiiiiii");
            String s = "";
            Log.d("call_in_user",""+Id+"##");
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/sendInvitation");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sender_id",myuser_id);
                jsonObject.accumulate("receiver_mobile","+"+Recivermobile );


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            String error="";
            JSONObject jsonObject = null;
            Log.d("onPostExcute", "" + s);
            try {
                 jsonObject=new JSONObject(s);

                 error=jsonObject.getString("failure");
            } catch (JSONException e) {
                e.printStackTrace();
            }try {


                 error=jsonObject.getString("error");
            } catch (JSONException e) {
                e.printStackTrace();
            }
                if(error.equals("1")){
                    Toast.makeText(getApplicationContext(), "Contact already added!", Toast.LENGTH_LONG).show();
                }else if(error.equals("true")){
                    Toast.makeText(getApplicationContext(), "Contact already added!", Toast.LENGTH_LONG).show();
                }
                else {
                    String message= null;
                    try {
                        message = jsonObject.getString("success");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (message.equals("1")) {

                        Add.setText("Invitation Send ");
                        Add.setBackgroundColor(getResources().getColor(R.color.grey));
                        Toast.makeText(getApplicationContext(), "Invitation Send To the User", Toast.LENGTH_LONG).show();
                    }
                }

                Intent intent=new Intent(getApplicationContext(),DashbordActivity.class);
                startActivity(intent);

        }
    }
    public void  loadData(){
        myAsync=new MyAsync();

        if (Utils.isNetworkAvailable(getApplicationContext()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(getApplicationContext(),"NO net avalable",Toast.LENGTH_LONG).show();
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
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
