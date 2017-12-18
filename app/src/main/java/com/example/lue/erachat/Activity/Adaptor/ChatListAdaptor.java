package com.example.lue.erachat.Activity.Adaptor;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lue.erachat.Activity.FireBase.MyFirebaseMessagingService;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lue on 14-06-2017.
 */

public class ChatListAdaptor extends BaseAdapter {


    LayoutInflater mInflater;
    private Activity context;
   DatabaseHandler databaseHandler;
    CircleImageView circleImageView;
    ArrayList<Messages>MessageList=new ArrayList<>();
    ArrayList<Messages>ListAccordingToId=new ArrayList<>();
    ArrayList<String>UnreadMessagecount=new ArrayList<>();

    TextView messagepriview,message_count,message_intime,message_sendername;


    public ChatListAdaptor(FragmentActivity activity, ArrayList<Messages> message, ArrayList<String> unread) {
        this.context=activity;
        this.MessageList=message;
        this.UnreadMessagecount=unread;
    }

    @Override
    public int getCount() {
        return MessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.adaptor_chat_list, null);
        databaseHandler=new DatabaseHandler(context);
        final String myuser_id = PreferenceManager.getDefaultSharedPreferences(context).getString("myuserid", "");
        //intialization
        messagepriview=(TextView)view.findViewById(R.id.cont_number);
        message_intime=(TextView)view.findViewById(R.id.time);
        message_sendername=(TextView)view.findViewById(R.id.name);
        circleImageView= (CircleImageView) view.findViewById(R.id.circleImageView);

        String GroupId=MessageList.get(i).getGroupId();

        if(GroupId==null){
            messagepriview.setText(MessageList.get(i).getMessageText());
            message_intime.setText(MessageList.get(i).getMessageInTime());
            message_sendername.setText(MessageList.get(i).getSender_Name());
            message_count=(TextView)view.findViewById(R.id.textView62);
            message_count.setVisibility(View.GONE);
            try {
                if (!MessageList.get(i).getSender_chat_image().equals("")) {
                    Picasso.with(context).load(MessageList.get(i).getSender_chat_image()).into(circleImageView);
                }
            }catch (Exception e){}
            String sender="";
            if(MyFirebaseMessagingService.UnreadMessages.size()>0){
                String id=MessageList.get(i).getMessageSenderId();
                {
                    for(int z=0;z<MyFirebaseMessagingService.UnreadMessages.size();z++){
                        sender=  MyFirebaseMessagingService.UnreadMessages.get(z);
                    }


                }
                try {
                    if (MessageList.get(i).getMessageSenderId().equals(sender)) {
                        message_count.setVisibility(View.VISIBLE);
                        String size = String.valueOf(MyFirebaseMessagingService.UnreadMessages.size());
                        message_count.setText(size);
                    }
                }catch (Exception e){}
            }
        }else {
            messagepriview.setText(MessageList.get(i).getGroupMessageText());
            message_sendername.setText(MessageList.get(i).getGroupName());
            message_intime.setText(MessageList.get(i).getGroupMessageSendingTime());
            message_count=(TextView)view.findViewById(R.id.textView62);
            message_count.setVisibility(View.GONE);
            if(!MessageList.get(i).getGroupIcon().equals("")) {
                Picasso.with(context).load(MessageList.get(i).getGroupIcon()).into(circleImageView);
            }
            String sender="";
            if(MyFirebaseMessagingService.UnreadMessagesGroup.size()>0){

                {
                    for(int z=0;z<MyFirebaseMessagingService.UnreadMessagesGroup.size();z++){
                        sender=  MyFirebaseMessagingService.UnreadMessagesGroup.get(z);
                    }


                }
                if(MessageList.get(i).getGroupId().equals(sender)) {
                    message_count.setVisibility(View.VISIBLE);
                    String size = String.valueOf(MyFirebaseMessagingService.UnreadMessagesGroup.size());
                    message_count.setText(size);
                }
            }
        }

        return view;
    }



    boolean containsCardno(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
            if (item.getMessageSenderId().equals(name)){
                return true;
            }
        }
        return false;
    }
}
