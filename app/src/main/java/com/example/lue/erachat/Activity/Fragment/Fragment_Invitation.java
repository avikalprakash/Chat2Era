package com.example.lue.erachat.Activity.Fragment;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.AdaptorInvite;
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

public class Fragment_Invitation extends Fragment {
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog1;
    AdaptorInvite adaptorInvite;
    MyAsync myAsync;

    ArrayList<Messages> InvitationList=new ArrayList<>();
  ListView listinvite;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_fragment__invitation, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //dialog
        dialog1 = (AVLoadingIndicatorView) view.findViewById(R.id.avi);

        //AsynkTask
        myAsync=new MyAsync();

        listinvite=(ListView)view.findViewById(R.id.list_invite);
        loadData();

    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SHOW_PROCESS_DIALOG:
                    // pb.setVisibility(View.VISIBLE);
                    dialog1.show();
                    break;
                case HIDE_PROCESS_DIALOG:
                    //  pb.setVisibility(View.GONE);
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

                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getInvitation");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");
                 Log.d("chuhcu",""+myuser_id);
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
            Log.d("onPostExcuteMeoBook", "" + s + "cs");

            try {
                JSONObject jsonObject=new JSONObject(s);
                String message=jsonObject.getString("message");
                JSONArray jsonArray=new JSONArray(message);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Messages message1= new Messages();
                    message1.setInvitationId(jsonObject1.getString(("invitation_id")));
                    message1.setMessageSenderId(jsonObject1.getString("sender_id"));
                    message1.setMessageReciverId(jsonObject1.getString("receiver_id"));
                    message1.setInvitationStatus(jsonObject1.getString("status"));
                    message1.setInvitationSenderName(jsonObject1.getString("sender_name"));
                    message1.setInvitationSenderImage(jsonObject1.getString("sender_image"));
                    message1.setInvitationSenderMobile(jsonObject1.getString("sender_mobile"));
                    InvitationList.add(message1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }if(InvitationList.size()>0){
                adaptorInvite=new AdaptorInvite(getActivity(),InvitationList);
                listinvite.setAdapter(adaptorInvite);
            }else {
                Toast.makeText(getActivity(),"No record Found",Toast.LENGTH_LONG).show();
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
