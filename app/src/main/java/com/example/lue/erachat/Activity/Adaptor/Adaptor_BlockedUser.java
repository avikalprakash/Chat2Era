package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lue on 12-09-2017.
 */

public class Adaptor_BlockedUser extends BaseAdapter {

    LayoutInflater mInflater;
    private Activity context;
    ArrayList<Messages> BlockedUser=new ArrayList<>();
    ImageView bimage;
    TextView bName,Bnumber;

    public Adaptor_BlockedUser(FragmentActivity activity, ArrayList<Messages> blockedusers) {
        this.context=activity;
        this.BlockedUser=blockedusers;
    }

    @Override
    public int getCount() {
        return BlockedUser.size();
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
    public View getView(int i, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.adptor_blockedusers, null);
        bName=(TextView)convertView.findViewById(R.id.textView65);
        bimage=(ImageView)convertView.findViewById(R.id.imageView49);
        Bnumber=(TextView)convertView.findViewById(R.id.textView69);
        bName.setText(BlockedUser.get(i).getBlockedContactUserName());
        Bnumber.setText(BlockedUser.get(i).getBlockedContactUserMobile());
        Picasso.with(context).load(BlockedUser.get(i).getBlockedContactUserPhoto()).into(bimage);
        return convertView;
    }
}
