package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lue.erachat.Activity.GroupChat.GroupDetail;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lue on 03-08-2017.
 */

public class AdaptorGroupDetail extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity context;
    ArrayList<Messages>GroupMDeatil=new ArrayList<>();
    CircleImageView memberImage;
    TextView memberName,memberStatus;

    public AdaptorGroupDetail(GroupDetail groupDetail, ArrayList<Messages> groupMemberDetail) {
        this.context=groupDetail;
        this.GroupMDeatil=groupMemberDetail;
    }


    @Override
    public int getCount() {
        return GroupMDeatil.size();
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
    public View getView(int i, View view, ViewGroup parent) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.adaptor_group_detail, null);
        memberImage=(CircleImageView)view.findViewById(R.id.imageView49);
        memberName=(TextView)view.findViewById(R.id.textView65);
        memberStatus=(TextView)view.findViewById(R.id.textView69);
        String name=GroupMDeatil.get(i).getGroupMemberName();
        Log.d("ecuunjn",name);

        if(name.equals("null")){
            memberName.setText(GroupMDeatil.get(i).getGroupMemberNumber());

        }else {
            memberName.setText(GroupMDeatil.get(i).getGroupMemberName());
        }

        memberStatus.setText(GroupMDeatil.get(i).getGroupMemberStatus());
        Picasso.with(context).load(GroupMDeatil.get(i).getGroupMemberPhoto()).into(memberImage);

        return view;
    }
}
