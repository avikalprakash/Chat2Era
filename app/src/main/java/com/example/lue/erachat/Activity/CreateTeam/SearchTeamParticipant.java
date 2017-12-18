package com.example.lue.erachat.Activity.CreateTeam;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ContactListMeoBookAdaptor;
import com.example.lue.erachat.Activity.Adaptor.GroupList_Adaptor;
import com.example.lue.erachat.Activity.Fragment.FragmentContactListMeoBook;
import com.example.lue.erachat.Activity.GroupChat.GroupDetail;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SearchTeamParticipant extends AppCompatActivity {
ListView TeamMember;
    ImageView done;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    DatabaseHandler databaseHandler;
    ArrayList<User> users=new ArrayList<>();
    ArrayList<String>userid=new ArrayList<>();
    String GroupId="";
    SearchTeamAdaptor searchTeamAdaptor;
    Dialog dialog;
    GetUserContact getUserContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_team_participant);
        //asynk
        myAsync=new MyAsync();
        getUserContact=new GetUserContact();
        //databse
        databaseHandler=new DatabaseHandler(getApplicationContext());
        TeamMember=(ListView)findViewById(R.id.addteammember);
        //DisplayContact();
        getUserContact();
        //Dialog
        dialog = new Dialog(SearchTeamParticipant.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       //Intent
        Intent intent=getIntent();
        GroupId=intent.getStringExtra("GROUP_ID");

       //actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.custom_imageview_three, null);
        actionBar.setCustomView(v);
        done=(ImageView)findViewById(R.id.imageView4);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");


                for(int i=0;i<users.size();i++){
                    User country = users.get(i);
                    if(country.isSelected()){
                        userid.add(country.getUsercontactid());
                       // responseText.append("\n" + country.getUsercontactid());
                    }
                }

                loadData();
            }
        });

    }
  public void DisplayContact(){

     users = databaseHandler.getContact();
      Log.d("userSize","  "+users.size());
    searchTeamAdaptor =new SearchTeamAdaptor(SearchTeamParticipant.this,users);
      TeamMember.setAdapter(searchTeamAdaptor);
    TeamMember.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    });
  }

    //Handler
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




    //AsynkTask
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
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/addMembersInGroup");
                httpPost.setHeader("Content-type", "application/json");
                StringBuilder stringBuilder=new StringBuilder();
                for(int i=0;i<userid.size();i++){
                    if (i == (userid.size() - 1)) {
                        stringBuilder.append(userid.get(i));
                    } else {
                        stringBuilder.append(userid.get(i) + ",");
                    }

                }
                JSONObject jsonObject= new JSONObject();
                Log.d("hnjnj",""+GroupId+"  "+userid);
                jsonObject.accumulate("group_id",GroupId);
                jsonObject.accumulate("members",stringBuilder.toString());
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
                String message=jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Group Crated Sucessfully",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getApplicationContext(),GroupDetail.class);
            intent.putExtra("GroupId",GroupId);
            startActivity(intent);

        }
    }

    class GetUserContact extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            Log.d("werftghy", "iiiiiiiiiiii");
            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {

                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getContact");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", myuser_id);


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = Client.execute(httpPost);
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
            Log.d("onPostExcuteMeoBook", "" + s + "cs");

            try {

                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("message");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jobject = jsonArray.getJSONObject(i);
                    User itemEntity = new User();
                    itemEntity.setContactNumber(jobject.getString("mobile"));
                    itemEntity.setUserId(jobject.getString("user_id"));
                    itemEntity.setUsercontactid(jobject.getString("user_id"));
                    itemEntity.setContactName(jobject.getString("user_name"));
                    itemEntity.setContactImage(jobject.getString("photo"));
                    users.add(itemEntity);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (users.size() > 0) {
                searchTeamAdaptor = new SearchTeamAdaptor(SearchTeamParticipant.this, users);
                TeamMember.setAdapter(searchTeamAdaptor);
            }
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();

        if (Utils.isNetworkAvailable(SearchTeamParticipant.this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(SearchTeamParticipant.this,"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }

    public void  getUserContact(){
        getUserContact=new GetUserContact();
        if (Utils.isNetworkAvailable(getApplicationContext()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            getUserContact.execute();
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
