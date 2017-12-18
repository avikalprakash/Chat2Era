package com.example.lue.erachat.Activity.AddFriend;


import android.app.Dialog;
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
import android.widget.Toast;

import com.example.lue.erachat.Activity.Fragment.FragmentContactListMeoBook;
import com.example.lue.erachat.Activity.Other.Utils;
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

/**
 * Created by lue on 09-06-2017.
 */

public class FragmentContact extends Fragment {

    MyAsync myAsync;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Dialog dialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myAsync=new MyAsync();
    }

    public static FragmentContact newInstance(String text) {

        FragmentContact f = new FragmentContact();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
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
