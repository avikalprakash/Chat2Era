package com.example.lue.erachat.Activity.More;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.AddFriend.FragmentAddFriend;
import com.example.lue.erachat.Activity.CreateTeam.Createteam;
import com.example.lue.erachat.Activity.Events.EventsActivity;
import com.example.lue.erachat.Activity.RadarSearch;
import com.example.lue.erachat.Activity.Settings.SettingOption;
import com.example.lue.erachat.Activity.Task.TaskActivity;
import com.example.lue.erachat.R;

/**
 * Created by lue on 08-06-2017.
 */

public class MoreFragment extends Fragment {
ImageView AddFriend,CreateGroup,Radar_search,settings,Erapoint,Event,AboutEra_Chat,Events,EraChat_Point,Task,Era_Point;
    TextView addfriend,radarsearch,setting,vertualgift,creategroup,earpoint,task,events,era_chat_point,about_erachat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radarsearch=(TextView)view.findViewById(R.id.textView15);
        setting=(TextView)view.findViewById(R.id.textView18);
        earpoint=(TextView)view.findViewById(R.id.textView13);
    AddFriend=(ImageView)view.findViewById(R.id.imageView4);
        creategroup=(TextView)view.findViewById(R.id.textView9);
        addfriend=(TextView)view.findViewById(R.id.textView8);
        vertualgift=(TextView)view.findViewById(R.id.textView11);
        task=(TextView)view.findViewById(R.id.textView16);
        events=(TextView)view.findViewById(R.id.textView17);
        era_chat_point=(TextView)view.findViewById(R.id.textView14);
        about_erachat=(TextView)view.findViewById(R.id.textView19);
        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriend.setImageResource(R.drawable.icon_addfriend_color);
                addfriend.setTextColor(Color.parseColor("#ff751a"));
                Intent intent=new Intent(getActivity(),FragmentAddFriend.class);
                startActivity(intent);

            }
        });
        CreateGroup=(ImageView)view.findViewById(R.id.imageView5);
        CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateGroup.setImageResource(R.drawable.icon_creategroup_color);
                creategroup.setTextColor(Color.parseColor("#ff751a"));
                Intent intent=new Intent(getActivity(), Createteam.class);
                startActivity(intent);
            }
        });
        Radar_search=(ImageView)view.findViewById(R.id.imageView10);
        Radar_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Radar_search.setImageResource(R.drawable.icon_radar_search_color);
                radarsearch.setTextColor(Color.parseColor("#ff751a"));
                Intent intent1=new Intent(getActivity(),RadarSearch.class);
                startActivity(intent1);
            }
        });
        Radar_search.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x79000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
        settings=(ImageView)view.findViewById(R.id.imageView13);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settings.setImageResource(R.drawable.icon_settings_color);
                setting.setTextColor(Color.parseColor("#ff751a"));
              Intent intent2=new Intent(getActivity(),SettingOption.class);
                startActivity(intent2);
            }
        });
        settings.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x79000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });
        Erapoint=(ImageView)view.findViewById(R.id.imageView8);
        Erapoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Erapoint.setImageResource(R.drawable.icon_erapoint_color);
                Intent intent=new Intent(getActivity(),EraPoints.class);
                earpoint.setTextColor(Color.parseColor("#ff751a"));
                startActivity(intent);
            }
        });
        Task=(ImageView)view.findViewById(R.id.imageView11);
        Task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task.setImageResource(R.drawable.icon_task_color);
                task.setTextColor(Color.parseColor("#ff751a"));
                Intent intent=new Intent(getActivity(), TaskActivity.class);
                startActivity(intent);
            }
        });
        AboutEra_Chat=(ImageView)view.findViewById(R.id.imageView14);
        AboutEra_Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutEra_Chat.setImageResource(R.drawable.icon_erachat_color);
                about_erachat.setTextColor(Color.parseColor("#ff751a"));
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("http://www.erachat2u.com//"));
                startActivity(viewIntent);
            }
        });
        Events=(ImageView)view.findViewById(R.id.imageView12);
        Events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                events.setTextColor(Color.parseColor("#ff751a"));
                Events.setImageResource(R.drawable.icon_events_color);
                Intent intent=new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }
        });
        EraChat_Point=(ImageView)view.findViewById(R.id.imageView9);
        EraChat_Point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EraChat_Point.setImageResource(R.drawable.icon_erachatpoint_color);
                era_chat_point.setTextColor(Color.parseColor("#ff751a"));
                Intent intent = new Intent(getActivity(), EraChatPoint.class);
                startActivity(intent);

            }
        });


    }
}
