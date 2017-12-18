package com.example.lue.erachat.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.lue.erachat.R;

public class TermsOfServicesSecond extends AppCompatActivity {
Button termsok;
    String Mobileno="";
    TextView no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_services_second);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Terms of Service");
        no=(TextView)findViewById(R.id.mobileno);
        Intent intent=getIntent();
        Mobileno=intent.getStringExtra("number_moble");
        no.setText(Mobileno);
        termsok=(Button)findViewById(R.id.termsok);
        termsok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(TermsOfServicesSecond.this,VerifyVerification.class);
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
