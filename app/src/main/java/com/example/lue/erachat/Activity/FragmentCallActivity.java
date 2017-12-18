package com.example.lue.erachat.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lue.erachat.R;

public class FragmentCallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_call);
       /* FragmentGroup_List moreFragment= new FragmentGroup_List();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, moreFragment);
        transaction.commit();*/
        Intent intent=new Intent(FragmentCallActivity.this,DashbordActivity.class);
        startActivity(intent);
    }
}
