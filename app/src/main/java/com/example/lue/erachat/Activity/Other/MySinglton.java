package com.example.lue.erachat.Activity.Other;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.Volley;

public class MySinglton {
    private static MySinglton mySinglton;
    private static Context ctx;
    private com.android.volley.RequestQueue requestQueue;

    private MySinglton(Context context){
         ctx=context;
        requestQueue=getQueue();
    }

    private com.android.volley.RequestQueue getQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized MySinglton getMySinglton(Context context){
      if (mySinglton==null){
          mySinglton=new MySinglton(context);
      }
        return mySinglton;
    }
    public <T> void addToRequest(Request<T> request){
        getQueue().add(request);
    }
}
