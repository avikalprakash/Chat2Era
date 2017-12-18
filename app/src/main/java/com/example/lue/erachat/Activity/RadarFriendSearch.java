package com.example.lue.erachat.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ContactListMeoBookAdaptor;
import com.example.lue.erachat.Activity.Adaptor.RadarSearchAdaptor;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Fragment.FragmentContactListMeoBook;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.*;
import com.example.lue.erachat.R;
import com.wang.avi.AVLoadingIndicatorView;

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

public class RadarFriendSearch extends AppCompatActivity {
   MyAsync myAsync;
    ListView MeoList;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog1;
    ArrayList<User> compareList=new ArrayList<User>();
    RadarSearchAdaptor radarSearchAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_friend_search);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Radar Search");
        dialog1=(AVLoadingIndicatorView)findViewById(R.id.avi) ;
        MeoList=(ListView)findViewById(R.id.MeoList);
        MeoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name=compareList.get(i).getContactName();
                String image=compareList.get(i).getContactImage();
                String friend_id=compareList.get(i).getUserId();
                Log.d("yuhusdhc",""+name+","+friend_id);
                Toast.makeText(getApplicationContext(), name+" ,"+friend_id+", "+image, Toast.LENGTH_LONG).show();

            }
        });
        loadData();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    // pb.setVisibility(View.VISIBLE);
                    dialog1.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    //  pb.setVisibility(View.GONE);
                    dialog1.hide();
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
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(RadarFriendSearch.this).getString("myuserid", "");
            try {

                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getNearbyUsers");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection","Keep-Alive");

                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);
                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = Client.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);

                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();


            }
            return s;
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcuteMeoBook", "" + s+"cs");
            /*compareList.clear();
            StoreContactMeo.clear();
            StoreContacts.clear();*/
            try {

                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray  = jsonObject.getJSONArray("message");
                Log.d("sizee", "kk1111 " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.d("object", "kk1111 " + jsonArray.getJSONObject(i).toString());
                    JSONObject jobject = jsonArray.getJSONObject(i);
                    User itemEntity = new User();
                    itemEntity.setContactNumber(jobject.getString("mobile"));
                    itemEntity.setUserId(jobject.getString("user_id"));
                    itemEntity.setUsercontactid(jobject.getString("user_id"));
                    itemEntity.setContactName(jobject.getString("user_name"));
                    itemEntity.setContactImage(jobject.getString("photo"));
                    compareList.add(itemEntity);



                }
            } catch (Exception e) {
                e.printStackTrace();
            }if(compareList.size()>0){
                radarSearchAdaptor=new RadarSearchAdaptor(RadarFriendSearch.this,compareList);
                MeoList.setAdapter(radarSearchAdaptor);
                /*MeoList.setAdapter(contactListMeoBookAdaptor);
                    if(flag==0) {
                        Log.d("***","******"+compareList.size());
                        for (int i=0;i<compareList.size();i++){

                            databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                        }
                        Log.d("***","%%%%%%%%"+compareList.size());
                        flag=1;
                        Log.d("logCheck","*******"+flag);
                    } else {


                        ArrayList<User> ContactList = databaseHandler.getContact();
                        Log.d("Sizeof", " " + compareList.size() + "  " + ContactList.size());
                        if (compareList.size() > ContactList.size()) {
                            Log.d("Sizeof", " " + compareList.size() + "  " + ContactList.size());
                            databaseHandler.getWritableDatabase();
                            for (int i = ContactList.size(); i < compareList.size(); i++) {

                                Log.d("cjnjnc", "&&&*****");
                                position = i;


                                Log.d("cjnjnc", "&&&*****" + " " + position);
                                databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(), "", "", compareList.get(i).getContactEmail(), "", compareList.get(i).getUsercontactid()));
                            }
                        }
                    }

*/


            }
            /*if (StoreContactMeo.size()>0){
              //  GetContactsIntoArrayList();
                getContacts();
                Log.d("***","******");
                for(User user1: StoreContactMeo){
                    for(User user2: StoreContacts){
                        if(user1.isSimilarButNotEqual(user2)){
                            compareList.add(user1);
                            Log.d("cyuhnu",""+compareList.size());


                           // compareList.add(user2);
                        }
                    }
                }

                if(flag==0) {
                    Log.d("***","******"+compareList.size());
                    for (int i=0;i<compareList.size();i++){

                        databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                    }
                    Log.d("***","%%%%%%%%"+compareList.size());
                    flag=1;
                    Log.d("logCheck","*******"+flag);
                }
                else  {


                    ArrayList<User>ContactList =databaseHandler.getContact();
                    Log.d("Sizeof"," "+compareList.size()+"  "+ ContactList.size());
                    if(compareList.size() > ContactList.size()){
                        Log.d("Sizeof"," "+compareList.size()+"  "+ ContactList.size());
                        databaseHandler.getWritableDatabase();
                        for (int i = ContactList.size(); i < compareList.size(); i++) {
                            *//*if (!(compareList.get(i).getContactNumber().equals(ContactList.get(i).getContactName()))) {
                                //  String t =contact.get(i).getInTime();
                                Log.d("cjnjnc","&&&*****");
                                position = i;

                            }*//*
                            position=i;
                            Log.d("cjnjnc","&&&*****"+" "+position);
                            databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                        }



                    }

                }*/




        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (com.example.lue.erachat.Activity.Other.Utils.isNetworkAvailable(this) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(this,"NO net avalable",Toast.LENGTH_LONG).show();

        }
    }
}
