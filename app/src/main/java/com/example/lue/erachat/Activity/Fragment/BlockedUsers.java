package com.example.lue.erachat.Activity.Fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.AdaptorInvite;
import com.example.lue.erachat.Activity.Adaptor.Adaptor_BlockedUser;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.R;
import com.wang.avi.AVLoadingIndicatorView;

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
import java.util.ArrayList;

/**
 * Created by lue on 12-09-2017.
 */

public class BlockedUsers extends Fragment {

    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog1;
    ListView blockeduserlist;
    ArrayList<Messages>blockedusers=new ArrayList<>();
    Adaptor_BlockedUser adaptor_blockedUser;
    UnblockUser unblockUser;
    MyAsync myAsync;
    String  BlockedUserid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blockedusers, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        blockeduserlist=(ListView)view.findViewById(R.id.blockeduserlist);

        //dialog
        dialog1 = (AVLoadingIndicatorView) view.findViewById(R.id.avi);

        //AsynkTask
        myAsync=new MyAsync();
        unblockUser=new UnblockUser();
        loadData();
        blockeduserlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlockedUserid=blockedusers.get(position).getBlockedContactUserid();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setMessage("Unblock User");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                unblockuser();
                                adaptor_blockedUser=new Adaptor_BlockedUser(getActivity(),blockedusers);
                                blockeduserlist.setAdapter(adaptor_blockedUser);
                                adaptor_blockedUser.notifyDataSetChanged();
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


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SHOW_PROCESS_DIALOG:

                    dialog1.show();
                    break;
                case HIDE_PROCESS_DIALOG:

                    dialog1.hide();
                    break;
            }
            return false;
        }
    });

    class MyAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);

            String s = "";
            try {
                String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
                HttpClient httpClient=new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getBlockedUsers");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", myuser_id);



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
            Log.d("onPostExcuteMeoBlocked", "" + s + "cs");
            blockedusers.clear();
            try {
                JSONObject jsonObject=new JSONObject(s);
                String msg=jsonObject.getString("message");
                JSONArray jsonArray=new JSONArray(msg);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Messages messages=new Messages();
                    messages.setBlockedContactUserid(jsonObject1.getString("user_id"));
                    messages.setBlockedContactUserName(jsonObject1.getString("user_name"));
                    messages.setBlockedContactUserMobile(jsonObject1.getString("mobile"));
                    messages.setBlockedContactUserPhoto(jsonObject1.getString("photo"));
                    blockedusers.add(messages);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }if(blockedusers.size()>0){
                adaptor_blockedUser=new Adaptor_BlockedUser(getActivity(),blockedusers);
                blockeduserlist.setAdapter(adaptor_blockedUser);

            }else {
                Toast.makeText(getActivity(),"No blocked users",Toast.LENGTH_LONG).show();
            }


        }
    }
    class UnblockUser extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);

            String s = "";
            try {
                String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
                HttpClient httpClient=new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/unblockUser");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id",BlockedUserid );



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
            Log.d("onPostExcuteMeoBlocked", "" + s + "cs");
            blockedusers.clear();
            try {
                JSONObject jsonObject=new JSONObject(s);
                String msg=jsonObject.getString("message");
                JSONArray jsonArray=new JSONArray(msg);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Messages messages=new Messages();
                    messages.setBlockedContactUserid(jsonObject1.getString("user_id"));
                    messages.setBlockedContactUserName(jsonObject1.getString("user_name"));
                    messages.setBlockedContactUserMobile(jsonObject1.getString("mobile"));
                    messages.setBlockedContactUserPhoto(jsonObject1.getString("photo"));
                    blockedusers.add(messages);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }if(blockedusers.size()>0){
                adaptor_blockedUser=new Adaptor_BlockedUser(getActivity(),blockedusers);
                blockeduserlist.setAdapter(adaptor_blockedUser);

            }else {
                Toast.makeText(getActivity(),"No blocked users",Toast.LENGTH_LONG).show();
            }


        }
    }

    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(getActivity()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {

            myAsync.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();

        }
    }
    public void  unblockuser(){
        unblockUser=new UnblockUser();
        if (Utils.isNetworkAvailable(getActivity()) && unblockUser.getStatus()!= AsyncTask.Status.RUNNING)
        {

            unblockUser.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();

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
}
