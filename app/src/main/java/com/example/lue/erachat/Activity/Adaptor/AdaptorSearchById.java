package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.AddFriend.SearchbyId;
import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;
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
 * Created by lue on 23-08-2017.
 */

public class AdaptorSearchById extends BaseAdapter {
    MyAsync myAsync;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    LayoutInflater mInflater;
    private Activity context;
    ArrayList<Messages> Search = new ArrayList<>();
    ImageView UserImage;
    TextView status,name;
    Button add;
    String ReciverMobile="";
    public AdaptorSearchById(FragmentActivity activity, ArrayList<Messages> searchList) {
        this.context=activity;
        this.Search=searchList;
    }

    @Override
    public int getCount() {
        return Search.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.adaptor_search_by_id, null);
        UserImage=(ImageView)convertView.findViewById(R.id.circleImageView);
        dialog=(AVLoadingIndicatorView)convertView.findViewById(R.id.avi) ;
        name=(TextView)convertView.findViewById(R.id.name) ;
        status=(TextView)convertView.findViewById(R.id.status);
        add=(Button)convertView.findViewById(R.id.button17);
        if(Search.get(i).getUserName()!=null){
            name.setText(Search.get(i).getUserName());
        }else {
            name.setText(Search.get(i).getUserMobile());

        }if(Search.get(i).getUserImage()!=null){
            Picasso.with(context).load(Search.get(i).getUserImage()).into(UserImage);
        }if(Search.get(i).getUserStatus()!=null){
            status.setText(Search.get(i).getUserStatus());
        }if(Search.get(i).getCheckContact()!=null){
            Log.d("hedun",""+Search.get(i).getCheckContact());
           if(Search.get(i).getCheckContact().equals("true")){
               add.setText("Added");
           }
        }
        ReciverMobile=Search.get(i).getUserMobile();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
        return convertView;
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
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(context).getString("myuserid", "");

            try {

                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/sendInvitation");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("sender_id",myuser_id);
                Log.d("valueindatabasse",""+ReciverMobile);
                jsonObject.accumulate("receiver_mobile",ReciverMobile );


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
                JSONObject jsonObject=new JSONObject(s);
                String error=jsonObject.getString("error");

                if(error.equals("true")){
                    Toast.makeText(context, "Contact already added!", Toast.LENGTH_LONG).show();
                }else {
                    String message=jsonObject.getString("failure");
                    if (message.equals("1")) {
                        Toast.makeText(context, "Not registered", Toast.LENGTH_LONG).show();
                    } else {
                        add.setText("Invitation Send ");
                        add.setBackgroundColor(context.getResources().getColor(R.color.grey));
                        Toast.makeText(context, "Invitation Send To the User", Toast.LENGTH_LONG).show();
                    }
                }

                Intent intent=new Intent(context,DashbordActivity.class);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(context) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(context,"NO net avalable",Toast.LENGTH_LONG).show();

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

}
