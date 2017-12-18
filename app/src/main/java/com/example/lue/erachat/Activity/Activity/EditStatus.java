package com.example.lue.erachat.Activity.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.StatusTextAdaptor;
import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.Settings.Profile;
import com.example.lue.erachat.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EditStatus extends AppCompatActivity {
    Button ok,cancel;
    EditText status;
    String Status="";
    MyAsync myAsync;
    ArrayList<String>statusUpdateList=new ArrayList<>();
    StatusTextAdaptor StatusTextAdaptor;

    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Dialog dialog;
    ListView StatusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        statusUpdateList.add("Available");
        statusUpdateList.add("Busy");
        statusUpdateList.add("In a meeting");
        statusUpdateList.add("Can't talk");

        statusUpdateList.add("Sleeping");
        statusUpdateList.add("AT Work");
        statusUpdateList.add("At the movies");
        statusUpdateList.add("At school");
        statusUpdateList.add("At the gym");
        statusUpdateList.add("Feeling Good");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Status");



         myAsync=new MyAsync();
        ok=(Button)findViewById(R.id.ok);
        cancel=(Button)findViewById(R.id.cancel);
        status=(EditText)findViewById(R.id.editTextstatus);
        StatusList=(ListView)findViewById(R.id.ListStatus) ;
        StatusTextAdaptor=new StatusTextAdaptor(this,statusUpdateList);
        StatusList.setAdapter(StatusTextAdaptor);

        //dialog
        dialog = new Dialog(EditStatus.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        StatusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String chuh=statusUpdateList.get(i);
                Status=statusUpdateList.get(i);
                status.setText(Status);
            }
        });

        //Listner
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Status=status.getText().toString();
                if(!Status.equals("")) {
                    loadData();
                }else {
                    Toast.makeText(getApplicationContext(),"Please set Status",Toast.LENGTH_LONG).show();
                }
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
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/setStatus");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);
                jsonObject.accumulate("status",Status);

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
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("status");
                String error = jsonObject.getString("error");
                if (error.equals("false")) {
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("MYSTATUS", status).commit();
                    Intent intent = new Intent(EditStatus.this, DashbordActivity.class);
                    intent.putExtra("EditProfile","true");
                    startActivity(intent);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(EditStatus.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(EditStatus.this,"NO net avalable",Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        super.onBackPressed();
      finish();
    }
}
