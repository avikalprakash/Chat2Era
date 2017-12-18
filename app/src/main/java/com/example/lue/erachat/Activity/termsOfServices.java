package com.example.lue.erachat.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class termsOfServices extends AppCompatActivity {
LinearLayout AgreeTerms;
String number="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_services);

          ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Terms of Service");
        Intent intent=getIntent();
        number=intent.getStringExtra("number");
        AgreeTerms=(LinearLayout)findViewById(R.id.AgreeTerms);
        AgreeTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(termsOfServices.this,TermsOfServicesSecond.class);
                intent.putExtra("number_moble",number);
                startActivity(intent);
                finish();
            }
        });
    }






    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
