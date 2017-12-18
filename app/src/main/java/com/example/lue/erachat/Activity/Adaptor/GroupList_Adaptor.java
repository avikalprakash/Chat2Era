package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lue on 02-08-2017.
 */

public class GroupList_Adaptor extends BaseAdapter {
    LayoutInflater inflater;
    private Activity context;
    ArrayList<Messages>groupList=new ArrayList<>();
    CircleImageView GroupIcon;
    TextView GroupName,GroupStaus,Group_Created;



    public GroupList_Adaptor(FragmentActivity activity, ArrayList<Messages> groupList) {
        this.groupList=groupList;
        this.context=activity;
    }

    @Override
    public int getCount() {
        return groupList.size();
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
        inflater=(LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView=inflater.inflate(R.layout.adaptor_grouplist,null);
        GroupIcon=(CircleImageView)convertView.findViewById(R.id.group_icon);
        GroupStaus=(TextView)convertView.findViewById(R.id.textView24);
        GroupName=(TextView)convertView.findViewById(R.id.group_name1);
        Group_Created=(TextView)convertView.findViewById(R.id.textView66);
        GroupName.setText(groupList.get(position).getGroupName());
        Group_Created.setText(groupList.get(position).getGroupCreatedDate());
        Picasso.with(context).load(groupList.get(position).getGroupIcon()).into(GroupIcon);
        return convertView;
    }
}
