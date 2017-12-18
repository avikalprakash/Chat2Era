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

import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.Fragment.Fragment_Invitation;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by lue on 19-08-2017.
 */

public class AdaptorInvite extends BaseAdapter {
    LayoutInflater mInflater;
    private Activity context;
    ArrayList<Messages> Invitation = new ArrayList<>();
    ImageView SenderImage;
    TextView Statement;
    TextView Sendername;
    TextView Status;
    Button Accept;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog1;
    AcceptInvitation acceptInvitation;
    String myuser_id="";
    String RecvierId="";
    public AdaptorInvite(FragmentActivity activity, ArrayList<Messages> invitationList) {
        this.context = activity;
        this.Invitation = invitationList;
    }

    @Override
    public int getCount() {
        return Invitation.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.adaptor_invite, null);

        dialog1=(AVLoadingIndicatorView)convertView.findViewById(R.id.avi);
        SenderImage = (ImageView) convertView.findViewById(R.id.imageView22);
        Statement = (TextView) convertView.findViewById(R.id.textView72);
        Sendername = (TextView) convertView.findViewById(R.id.textView73);
        Status = (TextView) convertView.findViewById(R.id.textView76);
        Accept=(Button)convertView.findViewById(R.id.button15);
        RecvierId=Invitation.get(position).getMessageSenderId();
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hgcy",""+RecvierId);

                loadData();
            }
        });
         myuser_id = PreferenceManager.getDefaultSharedPreferences(context).getString("myuserid", "");
        //
        acceptInvitation=new AcceptInvitation();
        if (!Invitation.get(position).getInvitationSenderName().equals("null")) {
            Sendername.setText(Invitation.get(position).getInvitationSenderName());
        }else {
            Sendername.setText(Invitation.get(position).getInvitationSenderMobile());
        }
        if (Invitation.get(position).getInvitationSenderImage() != null) {
            Picasso.with(context).load(Invitation.get(position).getInvitationSenderImage()).into(SenderImage);
        }
        Statement.setText( " Would like to add you on EraChat ");
        Status.setText(Invitation.get(position).getInvitationStatus());
        return convertView;
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


    class AcceptInvitation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);

            String s = "";
            try {

                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/addContact");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                Log.d("csgyhbu",""+RecvierId+""+myuser_id);
                jsonObject.accumulate("sender_id", RecvierId);
                jsonObject.accumulate("receiver_id",myuser_id);
                jsonObject.accumulate("status","accepted");


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
            try {
                JSONObject jsonObject=new JSONObject(s);
                String message=jsonObject.getString("error");
                if(message.equals("false")){
                  Toast.makeText(context,"Request Accepted Now you can communicate with User",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"Error ",Toast.LENGTH_LONG).show();
                }
                Intent intent=new Intent(context, DashbordActivity.class);
                context.startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void loadData() {
        acceptInvitation= new AcceptInvitation();
        if (Utils.isNetworkAvailable(context) && acceptInvitation.getStatus() != AsyncTask.Status.RUNNING) {

            acceptInvitation.execute();
        } else {
            Toast.makeText(context, "NO net avalable", Toast.LENGTH_LONG).show();

        }
    }


    private String readResponse(HttpResponse httpResponse) {
        InputStream is = null;
        String return_text = "";
        try {
            is = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
            Log.d("return_text", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;


    }



}
