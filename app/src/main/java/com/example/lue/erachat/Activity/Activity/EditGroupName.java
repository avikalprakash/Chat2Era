package com.example.lue.erachat.Activity.Activity;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.GroupChat.GroupDetail;
import com.example.lue.erachat.Activity.Other.Utils;
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

public class EditGroupName extends AppCompatActivity {
EditText edit_Group_Name;
    Button Ok,Cancel;
    String GroupName="";
    String GroupId="";
    MyAsync myAsync;
    AVLoadingIndicatorView dialog;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group_name);
        myAsync=new MyAsync();

        //Intent
        Intent intent=getIntent();
        GroupId=intent.getStringExtra("GroupId");
        //Initialization
        dialog=(AVLoadingIndicatorView)findViewById(R.id.avi);
        edit_Group_Name=(EditText)findViewById(R.id.edit_group_name);
        Ok=(Button)findViewById(R.id.OK);
        Cancel=(Button)findViewById(R.id.Cancel);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           GroupName=edit_Group_Name.getText().toString();
                if(!GroupName.equals("")){
                    loadData();
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/setGroupname");
                httpPost.setHeader("Content-type", "application/json");
                Log.d("useridssss"," "+myuser_id);
                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("group_id",GroupId);
                jsonObject.accumulate("groupname",GroupName);

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
                if(error.equals("false")){

                   Toast.makeText(getApplicationContext(),"Group name Updated Successfully",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(EditGroupName.this, GroupDetail.class);
                    intent.putExtra("GroupId",GroupId);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(EditGroupName.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(EditGroupName.this,"NO net avalable",Toast.LENGTH_LONG).show();
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
       // dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
       // dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
