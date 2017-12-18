/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lue.erachat.Activity.FireBase;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.DownloadImage;
import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Fragment.Fragment_Invitation;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


import com.example.lue.erachat.R;

import static java.lang.System.in;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String notification="";
    String Title="";
    Context context;
    DatabaseHandler handler;
    private int position = 0;
    ArrayList<Messages>MeaasgeListIncoming=new ArrayList<>();
    ArrayList<Messages>MessageListGroup=new ArrayList<>();
    String SenderId="";
    String MessageText="";
    String MessageInTime="";
    String MessageType="";
    String MessageId="";
    String Reciverid="";
    String Sendername="";
    String SenderNo="";
    String FinamlSendername="";
    String MessageImage="";
    String MessageVideo="";
    String MessageVoice="";
    String SenderImage="";
    String ContactId="";
    String ContactNumber="";
    String ContactImage="";
    String MessageInvite="";
    String ChatType="";
    String GroupName="";
    String GroupId="";
    String GroupPhoto="";
    String GroupAdmin="";
    String GroupchatMessageSenderName="";
    String GroupMessageImage;
    String MessageLat="";
    String MessageLongitude="";
      DownloadImage downloadImage;
    String blueTick;
    int id=0;
    int badgeView=0;
    NotificationManager notificationManager;
    public static ArrayList<String> UnreadMessages=new ArrayList<>();
    public static ArrayList<String>UnreadMessagesGroup=new ArrayList<>();
    ArrayList<Messages>UnreadMessage=new ArrayList<>();
    ArrayList<Messages>BlueTick=new ArrayList<>();
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public String Currentdate = df.format(Calendar.getInstance().getTime());

    String myuser_id="";

    public void onMessageReceived(RemoteMessage remoteMessage) {

        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
        Log.d(TAG, "FCM Message Id: " + remoteMessage.getMessageId());
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.getNotification());
        Log.d(TAG, "FCM Data Message: " + remoteMessage.getData());
        Log.d(TAG, "To: " + remoteMessage.getTo());
        Log.d(TAG,"From:"+remoteMessage.getFrom());
        Log.d(TAG,"SendTime"+remoteMessage.getSentTime());
        Log.d(TAG,"Type:"+remoteMessage.getMessageType());
        handler=new DatabaseHandler(getApplicationContext());
        MeaasgeListIncoming=handler.getMessage();
        MessageListGroup=handler.getGroup();
        sendNotification(remoteMessage.getData());

        if (remoteMessage == null) {
            return;
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(Map<String, String> params) {

        Log.d(TAG, "Notification Message Body: ");
        JSONObject jsonObject = new JSONObject(params);
        Log.d("JsonObject", jsonObject.toString());
        try {
            MessageType = jsonObject.getString("type");
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        try {
            ChatType=jsonObject.getString("chat_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(ChatType.equals("one_to_one")) {
            try {
                MessageType = jsonObject.getString("type");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                notification = jsonObject.getString("message");

            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                MessageId = jsonObject.getString("blueTick");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                SenderId = jsonObject.getString("sender_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            String vjnev = null;
            try {
                vjnev = jsonObject.getString("date");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            MessageInTime = vjnev.substring(10, 16);

            MessageInTime.substring(MessageInTime.indexOf(':') + 1);
            if (MessageType.equals("invitation")) {
                try {
                    MessageInvite = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (MessageType.equals("text")) {

                try {
                    MessageText = jsonObject.getString("message");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (MessageType.equals("image")) {

                try {
                    MessageImage = jsonObject.getString("message");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (MessageType.equals("video")) {
                try {
                    MessageVideo = jsonObject.getString("message");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } else if (MessageType.equals("contact")) {
                try {
                    ContactId = jsonObject.getString("contact_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ContactNumber = jsonObject.getString("contact_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ContactImage = jsonObject.getString("contact_name");
                    String l="vgfyfgk";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (MessageType.equals("voice")) {
                try {
                    MessageVoice = jsonObject.getString("message");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }else if(MessageType.equals("location")){
                try {
                    String location=jsonObject.getString("message");
                    String[] loc = location.split(",");
                    MessageLat= loc[0];
                    MessageLongitude=loc[1];
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
          /*  try {
                MessageId = jsonObject.getString("message_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }*/
          /*  try {
                MessageId = jsonObject.getString("message_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }*/
            try {
                Reciverid = jsonObject.getString("receiver_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


            try {
                SenderNo = jsonObject.getString("mobile");
                FinamlSendername = SenderNo;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jsonObject.getString("sender_name") != null) {

                    Sendername = jsonObject.getString("sender_name");
                    FinamlSendername = Sendername;
                } else {
                    FinamlSendername = SenderNo;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jsonObject.getString("sender_image") != null) {

                    SenderImage = jsonObject.getString("sender_image");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Messages messages = new Messages();

            handler = new DatabaseHandler(MyFirebaseMessagingService.this);
            MeaasgeListIncoming = handler.getMessage();

            if (containsCardno(MeaasgeListIncoming, SenderId)) {

                updateMessage();

                if (!MessageText.equals("")) {

                    messages.setChatroom_Message_Senderid(SenderId);
                    UnreadMessage.add(messages);

                 //   messages.setBlueTick(blueTick);
                 //   BlueTick.add(messages);

                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, MessageText,"", "", "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));
                } else if (!MessageImage.equals("")) {
                    downloadImage=new DownloadImage();
                    downloadImage.loadUrlImage(MessageImage);
                    String imagepath=downloadImage.loadPath();
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", imagepath, "", "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));
                    messages.setChatroom_Message_Senderid(SenderId);
                    UnreadMessage.add(messages);
                } else if (!MessageVideo.equals("")) {
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", MessageVideo, "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));

                } else if ((MessageType.equals("voice")) && (!MessageVoice.equals(""))) {
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", MessageVoice, "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));

                } else if (MessageType.equals("contact")) {
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", "", ContactImage, ContactNumber, ContactId, MessageInTime, MessageType, Currentdate,"","","",""));

                }else if(MessageType.equals("location")){
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", "", "", "", "", MessageInTime, "MessageType", "",MessageLat,MessageLongitude,"",""));
                }
            } else {
                if (!MessageText.equals("")) {
                    handler.insertMessage(new Messages(SenderId, Reciverid, MessageType, MessageText, MessageInTime, FinamlSendername, SenderImage, SenderNo));

                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, MessageText, "", "", "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));
                } else if (!MessageImage.equals("")) {
                    downloadImage=new DownloadImage();
                    downloadImage.loadUrlImage(MessageImage);
                    String imagepath=downloadImage.loadPath();
                    handler.insertMessage(new Messages(SenderId, Reciverid, MessageType, MessageText, MessageInTime, FinamlSendername, SenderImage, SenderNo));

                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", imagepath, "", "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));

                } else if (!MessageVideo.equals("")) {
                    handler.insertMessage(new Messages(SenderId, Reciverid, MessageType, MessageText, MessageInTime, FinamlSendername, SenderImage, SenderNo));

                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", MessageVideo, "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));
                } else if ((MessageType.equals("voice")) && (!MessageVoice.equals(""))) {
                    handler.insertMessage(new Messages(SenderId, Reciverid, MessageType, MessageText, MessageInTime, FinamlSendername, SenderImage, SenderNo));

                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", MessageVoice, "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));

                }else if (MessageType.equals("contact")) {


                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", "", ContactImage, ContactNumber, ContactId, MessageInTime, MessageType, Currentdate,"","","",""));

                }else if(MessageType.equals("location")){
                    handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, "", "", "", "", "", "", "", MessageInTime, "MessageType", "",MessageLat,MessageLongitude,"",""));

                }
            }
        }else if(ChatType.equals("group")){
            try {
                SenderId = jsonObject.getString("sender_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                GroupId = jsonObject.getString("group_id");
                GroupName=jsonObject.getString("group_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                GroupPhoto=jsonObject.getString("group_photo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                MessageType = jsonObject.getString("type");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                MessageId = jsonObject.getString("message_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            try {
                Reciverid = jsonObject.getString("receiver_id");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            if(MessageType.equals("text")){
                try {
                    MessageText=jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(MessageType.equals("voice")){
                try {
                    MessageVoice=jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (MessageType.equals("contact")) {
                try {
                    ContactId = jsonObject.getString("contact_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ContactNumber = jsonObject.getString("contact_no");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    ContactImage = jsonObject.getString("contact_name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(MessageType.equals("location")){
                try {
                    String location=jsonObject.getString("message");
                    String[] loc = location.split(",");
                    MessageLat= loc[0];
                    MessageLongitude=loc[1];
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }else if(MessageType.equals("image")){
                try {
                    String Image=jsonObject.getString("message");
                    GroupMessageImage=Image;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try {
                if(jsonObject.getString("sender_name")!=null)
                GroupchatMessageSenderName=jsonObject.getString("sender_name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String vjnev = null;
            try {
                vjnev = jsonObject.getString("date");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            MessageInTime = vjnev.substring(10, 16);

            if(containsGroupId(MessageListGroup,GroupId)){
                if (MessageType.equals("text")) {
                    Log.d("sendername",""+GroupchatMessageSenderName);

                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,MessageText,"","","","","","","",""));
                }else if ((MessageType.equals("voice")) && (!MessageVoice.equals(""))) {
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","",MessageVoice,"","","","","",""));

                }else if(MessageType.equals("contact")){
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","","","","","",ContactId,ContactNumber,ContactImage));

                }else if(MessageType.equals("location")){
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","","","",MessageLat,MessageLongitude,"","",""));

                }else if(MessageType.equals("image")){
                    downloadImage=new DownloadImage();
                    downloadImage.loadUrlImage(GroupMessageImage);
                    String imagepath=downloadImage.loadPath();
                    handler.insertGroupChat(new Messages(GroupId, MessageId, SenderId, GroupchatMessageSenderName, MessageInTime, MessageType, "", imagepath, "", "", "", "", "", "", ""));

                }
                updateMessageGroup();
            }else {
                if (MessageType.equals("text")) {
                    Log.d("sendername",""+GroupchatMessageSenderName);
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,MessageText,"","","","","","","",""));
                    handler.insertGroup(new Messages(MessageId, GroupId, GroupName, GroupPhoto, SenderId, MessageType, MessageText, MessageInTime));

                }else if ((MessageType.equals("voice")) && (!MessageVoice.equals(""))) {
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","",MessageVoice,"","","","","",""));
                    handler.insertGroup(new Messages(MessageId, GroupId, GroupName, GroupPhoto, SenderId, MessageType, MessageText, MessageInTime));

                }else if ((MessageType.equals("contact"))) {
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","","","","","",ContactId,ContactNumber,ContactImage));
                    handler.insertGroup(new Messages(MessageId, GroupId, GroupName, GroupPhoto, SenderId, MessageType, MessageText, MessageInTime));

                }else if(MessageType.equals("location")){
                    handler.insertGroupChat(new Messages(GroupId,MessageId,SenderId,GroupchatMessageSenderName,MessageInTime,MessageType,"","","","",MessageLat,MessageLongitude,"","",""));
                    handler.insertGroup(new Messages(MessageId, GroupId, GroupName, GroupPhoto, SenderId, MessageType, MessageText, MessageInTime));

                }else if(MessageType.equals("image")){
                    downloadImage=new DownloadImage();
                    downloadImage.loadUrlImage(GroupMessageImage);
                    String imagepath=downloadImage.loadPath();
                    handler.insertGroupChat(new Messages(GroupId, MessageId, SenderId, GroupchatMessageSenderName, MessageInTime, MessageType, "", imagepath, "", "", "", "", "", "", ""));
                    handler.insertGroup(new Messages(MessageId, GroupId, GroupName, GroupPhoto, SenderId, MessageType, MessageText, MessageInTime));

                }

            }
        }else if(ChatType.equals("invitation")){
            try {
                MessageInvite = jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                SenderNo = jsonObject.getString("mobile");
                FinamlSendername = SenderNo;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (jsonObject.getString("sender_name") != null) {

                    Sendername = jsonObject.getString("sender_name");
                    FinamlSendername = Sendername;
                } else {
                    FinamlSendername = SenderNo;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        PendingIntent pendingIntent;
        if(MessageType.equals("invitation")){
            Intent intent = new Intent(this, DashbordActivity.class);
            intent.putExtra("Invitation", "true");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent= PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else if(ChatType.equals("group")) {
            Intent intent = new Intent(this, FragmentChat.class);
            intent.putExtra("GroupId", GroupId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }else {
            Intent intent = new Intent(this, FragmentChat.class);
            intent.putExtra("SenderId", SenderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }

        if (MessageType.equals("image")) {
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.copy)
                    .setContentTitle(FinamlSendername)

                    .setContentText("image")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            id = 0;
            notificationManager.notify(id, notificationBuilder.build());
        }else if(ChatType.equals("invitation")){
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.copy)
                    .setContentTitle(FinamlSendername)

                    .setContentText(MessageInvite)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            notificationManager.notify(0, notificationBuilder.build());
        }else if(ChatType.equals("group")){
            if(MessageType.equals("location")){
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                        .setSmallIcon(R.drawable.copy)
                        .setContentTitle(FinamlSendername)

                        .setContentText("location")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                id = 0;
                notificationManager.notify(id, notificationBuilder.build());
            }else if(MessageType.equals("image")){
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                        .setSmallIcon(R.drawable.copy)
                        .setContentTitle(FinamlSendername)

                        .setContentText("Image")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                id = 0;
                notificationManager.notify(id, notificationBuilder.build());
            }else {
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                        .setSmallIcon(R.drawable.copy)
                        .setContentTitle(GroupName)

                        .setContentText(MessageText)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                id = 0;
                notificationManager.notify(id, notificationBuilder.build());
            }
        }else if(MessageType.equals("contact")){
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.copy)
                    .setContentTitle(FinamlSendername)

                    .setContentText("contact")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            id = 0;
            notificationManager.notify(id, notificationBuilder.build());
        }else if(MessageType.equals("location")){
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.copy)
                    .setContentTitle(FinamlSendername)

                    .setContentText("location")
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            id = 0;
            notificationManager.notify(id, notificationBuilder.build());
        }
        else {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)

                    .setSmallIcon(R.drawable.copy)
                    .setContentTitle(FinamlSendername)

                    .setContentText(MessageText)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            id = 0;
            notificationManager.notify(id, notificationBuilder.build());


        }
    }
    boolean containsCardno(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
            if (item.getMessageSenderId().equals(name)||item.getMessageReciverId().equals(name)){
                return true;
            }
        }
        return false;
    }
    boolean containsGroupId(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
            if (item.getGroupId().equals(name)){
                return true;
            }
        }
        return false;
    }
    public void updateMessage(){
        handler=new DatabaseHandler(getApplicationContext());
        MeaasgeListIncoming=handler.getMessage();
        for (int i = 0; i < MeaasgeListIncoming.size(); i++) {

            if (((MeaasgeListIncoming.get(i).getMessageSenderId().equals(SenderId))&&MeaasgeListIncoming.get(i).getMessageReciverId().equals(myuser_id))||((MeaasgeListIncoming.get(i).getMessageSenderId().equals(myuser_id))&&(MeaasgeListIncoming.get(i).getMessageReciverId().equals(SenderId)))) {


                position = i;

            }
        }

        Messages c = MeaasgeListIncoming.get(position);
        c.setMessageSenderId(SenderId);
        if(!MessageText.equals("")) {
            c.setMessageText(MessageText);
        }else if(!MessageImage.equals("")){
            c.setMessageText("Image");
        }else if(!MessageVideo.equals("")){
            c.setMessageText("Video");
        }else if(!MessageLongitude.equals("")){
            c.setMessageText("location");
        }
        c.setMessageReciverId(Reciverid);
        c.setMessageType(MessageType);
        c.setMessageInTime(MessageInTime);
        c.setSender_Name(FinamlSendername);
        c.setSender_chat_image(SenderImage);
        c.setSenderMobile(SenderNo);


       /* SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("sender_number", SenderNo);
        editor.commit();*/

        handler.updateMessage(c);

    }
    public void updateMessageGroup() {
        handler = new DatabaseHandler(getApplicationContext());
        MessageListGroup = handler.getGroup();
        for (int i = 0; i < MessageListGroup.size(); i++) {

            if ((MessageListGroup.get(i).getGroupId().equals(GroupId))) {

                position = i;

            }

        }

        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Messages c = MessageListGroup.get(position);
        c.setGroupMessageText(MessageText);
        c.setGroupMessageType(MessageType);
        c.setMessageInTime(date);

        handler.updateGroup(c);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (FragmentChat.class != null) {
                    if (!SenderId.equals("")) {
                        if (isAppIsInBackground(MyFirebaseMessagingService.this)) {


                        } else {
                            if (MessageType.equals("invitation")) {
                                Toast.makeText(getApplicationContext(),"MessageInvite",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getApplicationContext(),DashbordActivity.class);
                                intent.putExtra("Invitation","true");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else {
                                if (FragmentChat.isActivityVisible()) {

                                    if(ChatType.equals("one_to_one")){
                                    removeNotification(id);
                                    if (containsCardno(MeaasgeListIncoming, SenderId)) {

                                        updateMessage();
                                    }
                                    }else if(ChatType.equals("group")) {
                                        removeNotification(id);
                                        if (containsGroupId(MessageListGroup, GroupId)) {

                                            updateMessageGroup();
                                        }

                                    }

                                } else {

                                     if(ChatType.equals("one_to_one")){
                                         Intent in = new Intent(MyFirebaseMessagingService.this, DashbordActivity.class);
                                         badgeView++;

                                         UnreadMessages.add(SenderId);
                                         removeNotification(id);
                                         if (containsCardno(MeaasgeListIncoming, SenderId)) {

                                             updateMessage();
                                         }
                                       /*  in.putStringArrayListExtra("UnreadMessage", UnreadMessages);
                                         in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         startActivity(in);*/
                                     }else  {
                                         Intent in = new Intent(MyFirebaseMessagingService.this, DashbordActivity.class);
                                         badgeView++;
                                         UnreadMessagesGroup.add(GroupId);
                                         Log.d("GroupIDDetail",""+GroupId);
                                         removeNotification(id);

                                         if (containsGroupId(MessageListGroup, GroupId)) {
                                             Log.d("cssdhbh",""+SenderId);
                                             updateMessageGroup();
                                         }
                                     }

                                }
                            }
                        }
                    }
                }
            }
        }, 1500);
    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            try {
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(context.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            }catch (Exception e){}

        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
    private void removeNotification(final int id) {
        Handler handler = new Handler(Looper.getMainLooper());

        long delayInMilliseconds = 1000;
        handler.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(id);
            }
        }, delayInMilliseconds);
    }
}
