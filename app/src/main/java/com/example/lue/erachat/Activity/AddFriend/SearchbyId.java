package com.example.lue.erachat.Activity.AddFriend;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.AdaptorSearchById;
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

public class SearchbyId extends Fragment {

  MyAsync myAsync;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    ImageView SearchButton;
    EditText searchText;
    String Value="";
    ListView listView;
    AdaptorSearchById adaptorSearchById;
    ArrayList<Messages>SearchList=new ArrayList<>();
    String myuser_id="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_searchby_id, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myAsync=new MyAsync();
        dialog=(AVLoadingIndicatorView)view.findViewById(R.id.avi) ;
        SearchButton=(ImageView)view.findViewById(R.id.Search);
        searchText=(EditText)view.findViewById(R.id.searchText);
        listView=(ListView)view.findViewById(R.id.UserList);
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Value=searchText.getText().toString();
                loadData();
            }
        });

        myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
        Log.d("myuserid"," "+myuser_id);
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

            String s = "";

            try {

                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/searchUser");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("search",Value );
                jsonObject.accumulate("user_id",myuser_id);


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
            SearchList.clear();
            try {
                JSONObject jsonObject=new JSONObject(s);
                String message1=jsonObject.getString("message");
                Log.d("sgyhxy",""+s);
              //  if(m)
                JSONArray jsonArray=new JSONArray(message1);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Messages message=new Messages();
                    message.setUserId(jsonObject1.getString("user_id"));
                    message.setUserName(jsonObject1.getString("user_name"));
                    message.setUserMobile(jsonObject1.getString("mobile"));
                    message.setUserStatus(jsonObject1.getString("status"));
                    message.setUserImage(jsonObject1.getString("photo"));
                    message.setCheckContact(jsonObject1.getString("in_contact"));
                    SearchList.add(message);
                }

                adaptorSearchById=new AdaptorSearchById(getActivity(),SearchList);
                listView.setAdapter(adaptorSearchById);

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
