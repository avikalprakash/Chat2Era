package com.example.lue.erachat.Activity.GroupChat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ChatListAdaptor;
import com.example.lue.erachat.Activity.Adaptor.ItemListAdaptor;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.lue.erachat.Activity.Fragment.FragmentChat.MyPref;

/**
 * Created by lue on 14-06-2017.
 */

public class ChatList extends Fragment {

         ListView ChatList;
    ChatListAdaptor chatListAdaptor;
    ArrayList<Messages>Message=new ArrayList<>();
    ArrayList<Messages>Group=new ArrayList<>();
    ArrayList<Messages>GroupChat=new ArrayList<>();
    ArrayList<Messages>CombileList=new ArrayList<>();
    ArrayList<Messages>UnreadMessage=new ArrayList<>();
    DatabaseHandler databaseHandler;
    ArrayList<Messages> newList=new ArrayList<>();
    ArrayList<String>Unread=new ArrayList<>();
    String gId="";
   String myuser_id="";
    private Timer timer;
    String senderNo;
    public static int sizegroup=0;
    public static final String SENDER_NUMBER = "sender_number";
     public ChatList(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chatlist, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ChatList=(ListView)view.findViewById(R.id.ListChat);

        Bundle b = getArguments();
        if(b!=null) {
            Unread = b.getStringArrayList("UnreadMeaage");
        }

        databaseHandler=new DatabaseHandler(getActivity());
        Message=databaseHandler.getMessage();
        Group=databaseHandler.getGroup();

        newList.clear();
        newList .addAll(Group);
        newList.addAll(Message);
      /* newList= new ArrayList<Messages>(Message);
        newList.addAll(Group);*/
       // union.addAll(b);
        sizegroup=(Message.size()+Group.size());
        Log.d("sizeofList",""+newList.size());
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
        chatListAdaptor=new ChatListAdaptor(getActivity(),newList,Unread);
        ChatList.setAdapter(chatListAdaptor);
        ChatList.setAdapter(chatListAdaptor);
        final Handler Hnd = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Hnd.post(new Runnable() {
                    @Override
                    public void run() {
                        Message=databaseHandler.getMessage();
                        Group=databaseHandler.getGroup();
                        newList.clear();
                        newList .addAll(Group);
                        newList.addAll(Message);;
                        chatListAdaptor = new ChatListAdaptor(getActivity(), newList, Unread);
                        ChatList.setAdapter(chatListAdaptor);
                        chatListAdaptor.notifyDataSetChanged();
                        sizegroup=newList.size();

                    }
                });
            }
        }, 5000, 5000);

        ChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               /* SharedPreferences sharedpreferences=getActivity().getSharedPreferences(MyPref, Context.MODE_PRIVATE);
                senderNo=sharedpreferences.getString(SENDER_NUMBER,"");*/
                Intent intent=new Intent(getActivity(),FragmentChat.class);

                if(newList.get(i).getGroupId()==null){
                    String SenderId=newList.get(i).getMessageSenderId();
                    String keyid=newList.get(i).getKeyId();
                    try {
                        if (SenderId.equals(myuser_id)) {
                            SenderId = newList.get(i).getMessageReciverId();
                        }
                    }catch (Exception e){}
                    String Sendername=newList.get(i).getSender_Name();
                    String SenderMobile=newList.get(i).getSenderMobile();
                   // Toast.makeText(getContext(), SenderMobile, Toast.LENGTH_LONG).show();
                    String SenderImage=newList.get(i).getSender_chat_image();
                    intent.putExtra("SenderId",SenderId);
                    intent.putExtra("keyid",keyid);
                    intent.putExtra("SenderName",Sendername);
                    intent.putExtra("SenderMobile",SenderMobile);
                    intent.putExtra("SenderImage",SenderImage);
                    Log.d("cjhn","@@@@@@@@@@@");
                }else {
                    String SenderId=newList.get(i).getMessageSenderId();
                    String GroupId=newList.get(i).getGroupId();
                    String GroupName=newList.get(i).getGroupName();
                    intent.putExtra("SenderId",SenderId);
                    intent.putExtra("GroupId",GroupId);
                    intent.putExtra("GroupName",GroupName);
                    Log.d("cjhn","@@@@@@@@@@@"+GroupId);
                }
                startActivity(intent);

            }
        });
        ChatList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                final int rowid= Integer.parseInt(newList.get(position).getKeyId());
                if(newList.get(position).getGroupId()!=null){
                     gId=newList.get(position).getGroupId();
                }
                alertDialogBuilder.setMessage("Do You Want to Clear all Chat");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (gId == null) {
                                    databaseHandler.deleteRowChatList(rowid);
                                    databaseHandler.deleteAll();
                                    Message = databaseHandler.getMessage();
                                    chatListAdaptor = new ChatListAdaptor(getActivity(), newList, Unread);
                                    ChatList.setAdapter(chatListAdaptor);
                                }else {
                                    databaseHandler.deleteRowGroupList(rowid);
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    boolean containsCardno(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
            if (item.getMessageSenderId().equals(name)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
