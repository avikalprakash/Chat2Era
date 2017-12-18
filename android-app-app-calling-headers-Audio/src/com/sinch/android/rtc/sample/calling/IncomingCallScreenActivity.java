package com.sinch.android.rtc.sample.calling;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallEndCause;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class IncomingCallScreenActivity extends BaseActivity {

    static final String TAG = IncomingCallScreenActivity.class.getSimpleName();
    private String mCallId;
    private String mCallLocation;
    private AudioPlayer mAudioPlayer;
    String photo;
    ImageView imageView;
    String Mobile;
    String GET_IMAGE="http://erachat.condoassist2u.com/api/getImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incoming);
        imageView=(ImageView)findViewById(R.id.imageView);
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);

        imageView.setImageResource(R.drawable.icon_about);
       /* new GetImage().execute();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

            }
        }, 3500);*/

        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
       // photo = getIntent().getStringExtra("photo");
        Button answer = (Button) findViewById(R.id.answerButton);
        answer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                answerClicked();
            }
        });
        Button decline = (Button) findViewById(R.id.declineButton);
        decline.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                declineClicked();
            }
        });

        mAudioPlayer = new AudioPlayer(this);
        mAudioPlayer.playRingtone();
        mCallId = getIntent().getStringExtra(SinchService.CALL_ID);
        mCallLocation = getIntent().getStringExtra(SinchService.LOCATION);
       // photo = getIntent().getStringExtra("photo");
      //  photo = "http://erachat.condoassist2u.com//api//uploads//user//11_899f42a8c759383d51d1e5d53e06b767.jpg";
    }

    class GetImage extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(IncomingCallScreenActivity.this);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";
          //  String sMobile="+608804286695";
            String sMobile="+607982038746";
           // String sMobile="+601234567890";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(GET_IMAGE);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("mobile", Mobile);
                Log.d("mobile", sMobile);




                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("response", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }

            return s;

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
          //  pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                boolean check  = objone.getBoolean("error");
                if(check) {
                    String message = objone.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(IncomingCallScreenActivity.this);
                    builder.setMessage(message)
                            .setNegativeButton("ok", null)
                            .create()
                            .show();
                }else{

                    /*JSONArray jsonArray = objone.getJSONArray("message");
                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        photo=jsonObject.getString("photo");
                        Log.d("photo", photo);
                    }*/
                    JSONObject jsonObject = objone.getJSONObject("message");
                    photo = jsonObject.getString("photo");
                    Log.d("photo", photo);
                    imageView.setImageResource(R.drawable.icon_about);
                    Picasso.with(getApplicationContext()).load(photo).into(imageView);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private String readadsResponse(HttpResponse httpResponse) {

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
            Log.d("return1230", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;
    }

    @Override
    protected void onServiceConnected() {
      /*  new GetImage().execute();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

            }
        }, 2500);*/

        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.addCallListener(new SinchCallListener());
            TextView remoteUser = (TextView) findViewById(R.id.remoteUser);
            remoteUser.setText(call.getRemoteUserId());
            Mobile="+60"+call.getRemoteUserId();

           // Picasso.with(getApplicationContext()).load(photo).resize(312, 162).into(imageView);
          //  remoteUser.setText(String.valueOf(call.getDetails()));
            call.getDetails();
            new GetImage().execute();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {

                }
            }, 3500);
           // TextView remoteUserLocation = (TextView) findViewById(R.id.remoteUserLocation);
          //  remoteUserLocation.setText("Calling from " + mCallLocation);
        } else {
            Log.e(TAG, "Started with invalid callId, aborting");
            finish();
        }
    }

    private void answerClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.answer();
            Intent intent = new Intent(this, CallScreenActivity.class);
            intent.putExtra(SinchService.CALL_ID, mCallId);
            startActivity(intent);
        } else {
            finish();
        }
    }

    private void declineClicked() {
        mAudioPlayer.stopRingtone();
        Call call = getSinchServiceInterface().getCall(mCallId);
        if (call != null) {
            call.hangup();
        }
        finish();
    }

    private class SinchCallListener implements CallListener {

        @Override
        public void onCallEnded(Call call) {
            CallEndCause cause = call.getDetails().getEndCause();
            Log.d(TAG, "Call ended, cause: " + cause.toString());
            mAudioPlayer.stopRingtone();
            finish();
        }

        @Override
        public void onCallEstablished(Call call) {
            Log.d(TAG, "Call established");
        }

        @Override
        public void onCallProgressing(Call call) {
            Log.d(TAG, "Call progressing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
            // Send a push through your push provider here, e.g. GCM
        }
    }

/*    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerButton:
                    answerClicked();
                    break;
                case R.id.declineButton:
                    declineClicked();
                    break;
            }
        }
    };*/
}
