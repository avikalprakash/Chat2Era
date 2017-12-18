package com.example.lue.erachat.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lue.erachat.Activity.Other.MySinglton;
import com.example.lue.erachat.R;

import java.util.HashMap;
import java.util.Map;

public class GetStarted extends Activity {
LinearLayout layout;
    String app_server_url= "http://lueinfoservices.com/goods2go/api/registration";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        layout=(LinearLayout)findViewById(R.id.GetStarted);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                final String token=preferences.getString(getString(R.string.FCM_TOKEN)," ");
                Log.d("sharde",""+token);
                StringRequest stringRequest= new StringRequest(Request.Method.POST, app_server_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                })
                {
                    @Override
                    protected Map<String, String> getParams()throws AuthFailureError {
                        Map<String ,String> Params= new HashMap<String, String>();
                        Params.put("fcm_token",token);
                        Params.put("type","user");
                        return Params;
                    }
                };
                MySinglton.getMySinglton(GetStarted.this).addToRequest(stringRequest);*/

                Intent intent=new Intent(GetStarted.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
