package com.example.lue.erachat.Activity.Fragment;

import android.app.Dialog;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.CreateTeam.Createteam;
import com.example.lue.erachat.Activity.CreateTeam.SearchTeamParticipant;
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

/**
 * Created by lue on 01-08-2017.
 */

public class FragmentGroupList extends Fragment {
    ListView listView_group;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    AVLoadingIndicatorView dialog;
    Dialog dialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grouplist, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView_group=(ListView)view.findViewById(R.id.group_list);
        myAsync=new MyAsync();

        //Dialog
       /* dialog=(AVLoadingIndicatorView)view.findViewById(R.id.avi) ;
        dialog.setVisibility(View.GONE);*/

        //dialog
        dialog1 = new Dialog(getActivity());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.fill_dialog);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loadData();

    }



    //Handler
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :

                   // dialog.show();
                    dialog1.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                  //  dialog.hide();
                    dialog1.show();

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
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getUserCreatedGroups");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();

                jsonObject.accumulate("group_admin",myuser_id);
                Log.d("jcenjn"," "+myuser_id);

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
            JSONObject jsonObject1 = null;
            try {
                jsonObject1=new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String error=jsonObject1.getString("error");
                if(error.equals("false")){
                    String Groupid=jsonObject1.getString("group_id");
                    Intent intent=new Intent(getContext(),SearchTeamParticipant.class);
                    intent.putExtra("GROUP_ID",Groupid);
                    startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
