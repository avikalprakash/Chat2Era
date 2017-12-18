package com.example.lue.erachat.Activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.R;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class CallActivity extends Activity {
ImageButton imageButton;
    String user_id="1";
    TextView callState;
    Call call;
    ImageView endCall;
    String number;
    String INnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Intent intent = getIntent();
        if(intent.getStringExtra("number")!=null){
            number=intent.getStringExtra("number");
        } if(intent.getStringExtra("user_id")!=null){
            user_id=intent.getStringExtra("user_id");
        }
        INnumber = number.replace("+60", "+91");
        callState=(TextView)findViewById(R.id.callState);
        final SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(user_id)   //   current-user-id
                .applicationKey("9d175782-83e3-4fba-b823-e07452aa1558")       //  key
                .applicationSecret("B1xULMGYAESa5W/nUglBTQ==")   // secret
                .environmentHost("clientapi.sinch.com")    //  environmentHost
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.start();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //finish();
                if (call == null) {
                    call = sinchClient.getCallClient().callPhoneNumber(INnumber);
                    call.addCallListener(new SinchCallListener());
                    // callButton.setText("Hang Up");
                }
            }
        }, 100);

      /*  if (call == null) {
            call = sinchClient.getCallClient().callPhoneNumber(number);
            call.addCallListener(new SinchCallListener());
            //callButton.setText("Hang Up");
        } else {
            call.hangup();
        }*/


      //  imageButton=(ImageButton)findViewById(R.id.imageButton);
        endCall=(ImageButton)findViewById(R.id.endCall);
     /*   imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CallActivity.this,InCallActivity.class);
                startActivity(intent);
            }
        });*/

        endCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (call == null) {
                    call = sinchClient.getCallClient().callPhoneNumber(number);
                    call.addCallListener(new SinchCallListener());
                    // callButton.setText("Hang Up");
                } else {
                    call.hangup();
                }
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2500);*/
try {
    call.hangup();
}catch (Exception e){}

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        });

    }


    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
           // callButton.setText("Call");
            //callState.setText("");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }
}
