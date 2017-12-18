package com.example.lue.erachat.Activity.Settings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.lue.erachat.Activity.RadarSearch;
import com.example.lue.erachat.Activity.RadarSearchPeople;
import com.example.lue.erachat.R;

/**
 * Created by lue on 08-06-2017.
 */

public class SettingOption extends AppCompatActivity {
LinearLayout profile,notification,friends,help,tellfriend,meotalk,syncphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_setting_option);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Settings");
     profile=(LinearLayout)findViewById(R.id.profile);
        notification=(LinearLayout)findViewById(R.id.notification);
        friends=(LinearLayout)findViewById(R.id.friends);
        help=(LinearLayout)findViewById(R.id.help);
        tellfriend=(LinearLayout)findViewById(R.id.tellfriend);
        meotalk=(LinearLayout)findViewById(R.id.meotalk);
        syncphone=(LinearLayout)findViewById(R.id.syncphone);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Profile.class);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Notification.class);
                startActivity(intent);
            }
        });
        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FriendSetting.class);
                startActivity(intent);
            }
        });
    }





    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
