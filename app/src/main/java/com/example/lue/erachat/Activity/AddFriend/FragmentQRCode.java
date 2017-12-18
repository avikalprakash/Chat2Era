package com.example.lue.erachat.Activity.AddFriend;

import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.AdaptorSearchById;
import com.example.lue.erachat.Activity.DecoderActivity;
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

/**
 * Created by lue on 09-06-2017.
 */

public class FragmentQRCode extends Fragment {
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    Qrcode qrcode;
    String myuser_id;
    Button button;
    String Qrimage="";
    ImageView QRImageview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
        dialog=(AVLoadingIndicatorView)view.findViewById(R.id.avi) ;
        button=(Button)view.findViewById(R.id.button6);
        qrcode=new Qrcode();
        QRImageview=(ImageView)view.findViewById(R.id.imageView29);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), DecoderActivity.class);
                startActivity(intent);
            }
        });
       String qrimage=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("QRIMAGE", "");
        if(qrimage.equals("")){
            loadData();
        }else {
           Picasso.with(getActivity()).load(qrimage).into(QRImageview);
        }

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
    class Qrcode extends AsyncTask<String,Void,String> {

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
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getQrCode");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id",myuser_id);


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = Client.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);

            } catch (Exception exception) {
                exception.printStackTrace();


            }
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("postExecuteQr",""+s);

            try {
                JSONObject jsonObject=new JSONObject(s);
                String message1=jsonObject.getString("message");
                JSONArray jsonArray=new JSONArray(message1);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Qrimage= jsonObject1.getString("qrcode");
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("QRIMAGE", Qrimage).commit();
                    Picasso.with(getActivity()).load(Qrimage).into(QRImageview);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void  loadData(){
        qrcode=new Qrcode();
        if (Utils.isNetworkAvailable(getActivity()) && qrcode.getStatus()!= AsyncTask.Status.RUNNING)
        {
            qrcode.execute();
        }else{
            Toast.makeText(getActivity(),"NO net available please check your internet connection",Toast.LENGTH_LONG).show();

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

        } catch (Exception e)
        {

        }
        return return_text;


    }
    @Override
    public void onPause() {
        super.onPause();

        if ( qrcode.getStatus()== AsyncTask.Status.RUNNING)
        {
            qrcode.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( qrcode.getStatus()== AsyncTask.Status.RUNNING)
        {
            qrcode.cancel(true);
        }
    }
}
