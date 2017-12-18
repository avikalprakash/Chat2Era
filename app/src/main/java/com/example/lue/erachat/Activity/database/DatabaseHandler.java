package com.example.lue.erachat.Activity.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Models.User;

import java.util.ArrayList;

/**
 * Created by lue on 26-06-2017.
 */

public class DatabaseHandler  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "databaseEraChat.db";
    private static final int DATABASE_VERSION = 2;
    private static String CREATE_TABLE_CONTACTS;
    private static String CREATE_TABLE_CHAT_MESSAGE;
    private static String CREATE_TABLE_CHAT_ROOM;
    private static String CREATE_TABLE_GROUP;
    private static String CREATE_TABLE_GROUP_CHAT;

    public static ContentValues cValues;
    public  SQLiteDatabase dataBase = null;
    public static Cursor cursor;

    public static final String TABLE_CONTACTS = "Contacts";
    public static final String KEY_ID = "id";
    public static final String COLUMN_CONTACT_NUMBER="contact_number";
    public static final String COLUMN_CONTACT_NAME="contact_name";
    public static final String COLUMN_CONTACT_STATUS="contact_status";
    public static final String COLUMN_CONTACT_PHOTO="contact_photo";
    public static final String COLUMN_CONTACT_EMAIL="contact_email";
    public static final String COLUMN_REG_ID="contact_regId";
    public static final String COLUMN_CONTACT_ID="contact_Id";

    public static String TABLE_CHAT_MESSSAGE="Chat_data";
    public static String COLUMN_CHAT_SENDER_ID="Sender";
    public static String COLUMN_CHAT_RECIVER_ID="Reciver";
    public static String COLUMN_CHAT_TEXT="Chat_Text";
    public static String COLUMN_CHAT_INCOMING_TIME="Incoming_Time";
    public static String COLUMN_CHAT_TYPE="Message_Type";
    public static String COLUMN_CHAT_SENDERNAME="Sender_name";
    public static String COLUMN_CHAT_SENDERIMAGE="Sender_Image";
    public static String COLUMN_CHAT_SENDERMOBILE="Sender_Mobile";
    public static String COLUMN_CHAT_IMAGE="Chat_Image";
    public static String COLUMN_CHAT_FILE="Chat_File";

    public static String TABLE_CHAT_ROOM="Chat_room";
    public static String COLUMN_MESSAGE_ID="Message_id";
    public static String COLUMN_MESSAGE_SENDER_ID="Message_Sender_Id";
    public static String COLUMN_MESSAGE_RECIVER_ID="Message_Reciver_id";
    public static String COLUMN_MESSAGE_TEXT="Message_Text";
    public static String COLUMN_MESSAGE_IMAGE="Message_Image";
    public static String COLUMN_MESSAGE_TYPE="Message_Type";
    public static String COLUMN_MESSAGE_VIDEO="Message_Video";
    public static String COLUMN_MESSAGE_VOICE="Message_Voice";
    public static String COLUMN_MESSAGE_CONTACTINFO_IMAGE="ContactInfo_Image";
    public static String COLUMN_MESSAGE_CONTACTNFO_NUMBER="ContactInfo_Number";
    public static String COLUMN_MESSAGE_CONTACTNFO_ID="ContactInfo_Id";
    public static String COLUMN_CHATROOM_CREATION_DATE="Chatroom_creation_date";
    public static String COLUMN_CHATROOM_LATITUDE="Chatroom_latitude";
    public static String COLUMN_CHATROOM_LANGITUDE="Chatroom_lagitude";
    public static String COLUMN_MESSAGE_FILE_NAME="file_name";
    public static String COLUMN_IMAGE_PATH="Chatroom_ImagePath";


    public static String TABLE_GROUP= "TableGroup";
    public static String COLUMN_GROUP_ID="Group_Id";
    public static String COLUMN_GROUP_MESSAGE_ID="Group_message_id";
    public static String COLUMN_GROUP_Name="Group_Name";
    public static String COLUMN_GROUP_Photo="Group_Photo";
    public static String COLUMN_GROUP_CREATED_DATE="Group_Created_date";
    public static String COLUMN_GROUP_ADMIN = "GRoup_Admin";
    public static String COLUMN_GROUP_MESSAGE_SENDER_ID="Group_member_id";
    public static String COLUMN_GROUP_MESSAGE_TYPE="Group_Message_type";
    public static String COLUMN_GROUP_MESSAGE_SENDING_TIME="Group_Message_Sending_Time";
    public static String COLUMN_GROUP_MESSAGE_TEXT="Group_Message_text";
    public static String COLUMN_GROUP_MESSAGE_IMAGE="Group_Message_Image";

    public static String TABLE_GROUP_CHAT="TableGroupChat";
    public static String COLUMN_GROUP_ID_CHAT="Group_chat_id";
    public static String COLUMN_GROUP_CHAT_MESSAGE_ID="Group_chat_message_id";
    public static String COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID="GroupChat_message_senderid";
    public static String COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME="GroupChat_message_sendername";
    public static String COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME="Group_chat_message_intime";
    public static String COLUMN_GROUP_CHAT_MESSAGE_TYPE="Group_chat_message_type";
    public static String COLUMN_GROUP_CHAT_MESSAGE_TEXT="Group_chat_meesage_text";
    public static String COLUMN_GROUP_CHAT_MESSAGE_IMAGE="Group_chat_message_image";
    public static String COLUMN_GROUP_CHAT_MESSAGE_VOICE="Group_chat_message_voice";
    public static String COLUMN_GROUP_CHAT_MESSAGE_VIDEO="Group_chat_message_video";
    public static String COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE="Group_chat_contactinfo_image";
    public static String COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER="Group_chat_contactonfo_number";
    public static String COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID="Group_chat_contactinfo_id";
    public static String COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE="Group_chat_lattitude";
    public static String COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE="Group_chat_longitude";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CREATE_TABLE_CONTACTS= " CREATE TABLE " + TABLE_CONTACTS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_CONTACT_NUMBER + " VARCHAR," + COLUMN_CONTACT_NAME +" VARCHAR," + COLUMN_CONTACT_STATUS + " VARCHAR,"
                + COLUMN_CONTACT_PHOTO + " BLOB," + COLUMN_CONTACT_EMAIL + " VARCHAR,"+ COLUMN_REG_ID + " VARCHAR," + COLUMN_CONTACT_ID + " VARCHAR )";

        CREATE_TABLE_CHAT_MESSAGE= " CREATE TABLE " + TABLE_CHAT_MESSSAGE + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_CHAT_SENDER_ID + " VARCHAR," + COLUMN_CHAT_RECIVER_ID +" VARCHAR," + COLUMN_CHAT_TYPE + " VARCHAR,"+ COLUMN_CHAT_TEXT + " VARCHAR,"
                + COLUMN_CHAT_INCOMING_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+ COLUMN_CHAT_SENDERNAME + " VARCHAR,"+ COLUMN_CHAT_SENDERIMAGE + " VARCHAR,"+ COLUMN_CHAT_SENDERMOBILE + " VARCHAR)";

        CREATE_TABLE_CHAT_ROOM=" CREATE TABLE " + TABLE_CHAT_ROOM + " ( " +
                 KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_MESSAGE_ID + " VARCHAR," + COLUMN_MESSAGE_SENDER_ID +" VARCHAR," + COLUMN_MESSAGE_RECIVER_ID + " VARCHAR,"+ COLUMN_MESSAGE_TEXT + " VARCHAR,"+ COLUMN_MESSAGE_IMAGE + " VARCHAR," + COLUMN_MESSAGE_VIDEO + " VARCHAR," + COLUMN_MESSAGE_VOICE + " VARCHAR,"+ COLUMN_MESSAGE_CONTACTINFO_IMAGE + " VARCHAR,"+ COLUMN_MESSAGE_CONTACTNFO_NUMBER + " VARCHAR,"+ COLUMN_MESSAGE_CONTACTNFO_ID + " VARCHAR,"+ COLUMN_CHAT_INCOMING_TIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_MESSAGE_TYPE + " VARCHAR," + COLUMN_CHATROOM_CREATION_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + COLUMN_CHATROOM_LATITUDE + " VARCHAR, " + COLUMN_CHATROOM_LANGITUDE + " VARCHAR, " + COLUMN_MESSAGE_FILE_NAME + " VARCHAR, " + COLUMN_IMAGE_PATH + " VARCHAR )";

        CREATE_TABLE_GROUP = "  CREATE TABLE " + TABLE_GROUP + " ( " +
                 KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_GROUP_MESSAGE_ID + " VARCHAR," + COLUMN_GROUP_ID + " VARCHAR," + COLUMN_GROUP_Name +" VARCHAR," + COLUMN_GROUP_Photo + " VARCHAR,"+ COLUMN_GROUP_CREATED_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+ COLUMN_GROUP_ADMIN + " VARCHAR,"+ COLUMN_GROUP_MESSAGE_SENDER_ID + " VARCHAR,"+ COLUMN_GROUP_MESSAGE_TYPE + " VARCHAR,"+ COLUMN_GROUP_MESSAGE_SENDING_TIME + " VARCHAR,"+ COLUMN_GROUP_MESSAGE_TEXT + " VARCHAR," + COLUMN_GROUP_MESSAGE_IMAGE + " VARCHAR )";

        CREATE_TABLE_GROUP_CHAT = "  CREATE TABLE " + TABLE_GROUP_CHAT + " ( " +
                KEY_ID + " INTEGER PRIMARY KEY," + COLUMN_GROUP_ID_CHAT + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_ID + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME + " VARCHAR," + COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"+ COLUMN_GROUP_CHAT_MESSAGE_TYPE + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_TEXT + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_IMAGE + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_VOICE + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_VIDEO + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID + " VARCHAR,"+ COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER + " VARCHAR," + COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE + " VARCHAR )";


        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_CHAT_MESSAGE);
        db.execSQL(CREATE_TABLE_CHAT_ROOM);
        db.execSQL(CREATE_TABLE_GROUP);
        db.execSQL(CREATE_TABLE_GROUP_CHAT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAT_MESSSAGE);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CHAT_ROOM);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS" +CREATE_TABLE_GROUP_CHAT);
        // Create tables again
        onCreate(db);
    }

     public void insertGroup(Messages messages){
         dataBase=getWritableDatabase();
         cValues=new ContentValues();
         cValues.put(COLUMN_GROUP_MESSAGE_ID,messages.getGroupMessageId());
         cValues.put(COLUMN_GROUP_ID,messages.getGroupId());
         cValues.put(COLUMN_GROUP_Name,messages.getGroupName());
         cValues.put(COLUMN_GROUP_Photo,messages.getGroupIcon());
         cValues.put(COLUMN_GROUP_CREATED_DATE,messages.getGroupCreatedDate());
         cValues.put(COLUMN_GROUP_ADMIN,messages.getGroupAdmin());
         cValues.put(COLUMN_GROUP_MESSAGE_SENDER_ID,messages.getGroupMessageSenderId());
         cValues.put(COLUMN_GROUP_MESSAGE_TYPE,messages.getGroupMessageType());
         cValues.put(COLUMN_GROUP_MESSAGE_SENDING_TIME,messages.getGroupMessageSendingTime());
         cValues.put(COLUMN_GROUP_MESSAGE_TEXT,messages.getGroupMessageText());
         cValues.put(COLUMN_GROUP_MESSAGE_IMAGE,messages.getGroupMessageImage());
         dataBase.insert(TABLE_GROUP, null, cValues);
         dataBase.close();
     }
    public ArrayList<Messages> getGroup() {
        dataBase = getWritableDatabase();

//    Getting data from database table
        Cursor cursor = dataBase.query(TABLE_GROUP, null, null, null, null, null, null);

        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID)));
                messages.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Name)));
                messages.setGroupIcon(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Photo)));
                messages.setGroupCreatedDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CREATED_DATE)));
                messages.setGroupAdmin(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ADMIN)));
                messages.setGroupMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDER_ID)));
                messages.setGroupMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TYPE)));
                messages.setGroupMessageSendingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDING_TIME)));
                messages.setGroupMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TEXT)));
                messages.setGroupMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    } public ArrayList<Messages> getGroupListwithid(String id) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_GROUP, null, null, null, null, null, null);
        Cursor cursor = dataBase.query(TABLE_GROUP_CHAT,
                new String [] {KEY_ID,COLUMN_GROUP_ID, COLUMN_GROUP_Name, COLUMN_GROUP_Photo, COLUMN_GROUP_CREATED_DATE,COLUMN_GROUP_ADMIN,COLUMN_GROUP_MESSAGE_SENDER_ID,COLUMN_GROUP_MESSAGE_TYPE,COLUMN_GROUP_MESSAGE_SENDING_TIME,
                        COLUMN_GROUP_MESSAGE_TEXT,COLUMN_GROUP_MESSAGE_IMAGE},
                COLUMN_GROUP_ID_CHAT + "=? " ,
                new String[] { id},
                null, null, null);
        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                Log.d("djif","jciji");
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID)));
                messages.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Name)));
                messages.setGroupIcon(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Photo)));
                messages.setGroupCreatedDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CREATED_DATE)));
                messages.setGroupAdmin(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ADMIN)));
                messages.setGroupMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDER_ID)));
                messages.setGroupMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TYPE)));
                messages.setGroupMessageSendingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDING_TIME)));
                messages.setGroupMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TEXT)));
                messages.setGroupMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    }
    public ArrayList<Messages> getGroupwithId(String GroupId) {
        dataBase = getWritableDatabase();
        Cursor cursor = dataBase.query(TABLE_GROUP,
                new String [] {KEY_ID,COLUMN_GROUP_ID, COLUMN_GROUP_Name, COLUMN_GROUP_Photo, COLUMN_GROUP_CREATED_DATE,COLUMN_GROUP_ADMIN,COLUMN_GROUP_MESSAGE_SENDER_ID,COLUMN_GROUP_MESSAGE_TYPE,COLUMN_GROUP_MESSAGE_SENDING_TIME,COLUMN_GROUP_MESSAGE_TEXT,COLUMN_GROUP_MESSAGE_IMAGE},
                COLUMN_GROUP_ID +"=?",
                new String[] { GroupId },
                null, null, null);
        // Cursor cursor = dataBase.query(TABLE_GROUP, null, null, null, null, null, null);

        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID)));
                messages.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Name)));
                messages.setGroupIcon(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Photo)));
                messages.setGroupCreatedDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CREATED_DATE)));
                messages.setGroupAdmin(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ADMIN)));
                messages.setGroupMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDER_ID)));
                messages.setGroupMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TYPE)));
                messages.setGroupMessageSendingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDING_TIME)));
                messages.setGroupMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TEXT)));
                messages.setGroupMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    } public ArrayList<Messages> getGroupwithIdLimit(String GroupId) {
        dataBase = getWritableDatabase();
        Cursor cursor = dataBase.query(TABLE_GROUP,
                new String [] {KEY_ID,COLUMN_GROUP_ID, COLUMN_GROUP_Name, COLUMN_GROUP_Photo, COLUMN_GROUP_CREATED_DATE,COLUMN_GROUP_ADMIN,COLUMN_GROUP_MESSAGE_SENDER_ID,COLUMN_GROUP_MESSAGE_TYPE,COLUMN_GROUP_MESSAGE_SENDING_TIME,COLUMN_GROUP_MESSAGE_TEXT,COLUMN_GROUP_MESSAGE_IMAGE},
                COLUMN_GROUP_ID +"=?" + " LIMIT " + " 10 " + " OFFSET " +"(SELECT COUNT(*) FROM "+ TABLE_GROUP +")-10;",
                new String[] { GroupId },
                null, null, null);
        // Cursor cursor = dataBase.query(TABLE_GROUP, null, null, null, null, null, null);

        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID)));
                messages.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Name)));
                messages.setGroupIcon(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Photo)));
                messages.setGroupCreatedDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CREATED_DATE)));
                messages.setGroupAdmin(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ADMIN)));
                messages.setGroupMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDER_ID)));
                messages.setGroupMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TYPE)));
                messages.setGroupMessageSendingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDING_TIME)));
                messages.setGroupMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TEXT)));
                messages.setGroupMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    } public ArrayList<Messages> getGroupwithIdLimitVari(String GroupId,String id) {
        dataBase = getWritableDatabase();
        Cursor cursor = dataBase.query(TABLE_GROUP,
                new String [] {KEY_ID,COLUMN_GROUP_ID, COLUMN_GROUP_Name, COLUMN_GROUP_Photo, COLUMN_GROUP_CREATED_DATE,COLUMN_GROUP_ADMIN,COLUMN_GROUP_MESSAGE_SENDER_ID,COLUMN_GROUP_MESSAGE_TYPE,COLUMN_GROUP_MESSAGE_SENDING_TIME,COLUMN_GROUP_MESSAGE_TEXT,COLUMN_GROUP_MESSAGE_IMAGE},
                COLUMN_GROUP_ID +"=?" + " LIMIT " + " 10 " + " OFFSET " +"(SELECT COUNT(*) FROM "+ TABLE_GROUP +")-"+id+";",
                new String[] { GroupId },
                null, null, null);
        // Cursor cursor = dataBase.query(TABLE_GROUP, null, null, null, null, null, null);

        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID)));
                messages.setGroupName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Name)));
                messages.setGroupIcon(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_Photo)));
                messages.setGroupCreatedDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CREATED_DATE)));
                messages.setGroupAdmin(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ADMIN)));
                messages.setGroupMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDER_ID)));
                messages.setGroupMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TYPE)));
                messages.setGroupMessageSendingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_SENDING_TIME)));
                messages.setGroupMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_TEXT)));
                messages.setGroupMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_MESSAGE_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    }
    public int updateGroup(Messages messages) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUP_MESSAGE_ID,messages.getGroupMessageId());
        values.put(COLUMN_GROUP_ID,messages.getGroupId());
        values.put(COLUMN_GROUP_Name,messages.getGroupName());
        values.put(COLUMN_GROUP_Photo,messages.getGroupIcon());
        values.put(COLUMN_GROUP_CREATED_DATE,messages.getGroupCreatedDate());
        values.put(COLUMN_GROUP_ADMIN,messages.getGroupAdmin());
        values.put(COLUMN_GROUP_MESSAGE_SENDER_ID,messages.getGroupMessageSenderId());
        values.put(COLUMN_GROUP_MESSAGE_TYPE,messages.getGroupMessageType());
        values.put(COLUMN_GROUP_MESSAGE_SENDING_TIME,messages.getGroupMessageSendingTime());
        values.put(COLUMN_GROUP_MESSAGE_TEXT,messages.getGroupMessageText());
        values.put(COLUMN_GROUP_MESSAGE_IMAGE,messages.getGroupMessageImage());


        // updating row
        return db.update(TABLE_GROUP, values,  KEY_ID + " = ? ", new String[] { (messages.getGroupMessageId()) });

    }

    public void insertGroupChat(Messages messages){
        dataBase=getWritableDatabase();
        cValues=new ContentValues();
        cValues.put(COLUMN_GROUP_ID_CHAT,messages.getGroupIdChat());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_ID,messages.getGroupMessageChatId());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID,messages.getGroupChatMessageSenderId());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME,messages.getGroupChatMessageSenderName());
        cValues.put(COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME,messages.getGroupChatMessageIncomingTime());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_TYPE,messages.getGroupChatMessageType());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_TEXT,messages.getGroupChatMessageText());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_IMAGE,messages.getGroupChatMessageImage());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_VOICE,messages.getGroupChatMessageVoice());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_VIDEO,messages.getGroupChatMessageVideo());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE,messages.getGroupChatMessageLongitude());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE,messages.getGroupChatMessageLattitude());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID,messages.getGroupChatMessageContactInfo_Id());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER,messages.getGroupChatMessageContactInfo_Number());
        cValues.put(COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE,messages.getGroupChatMessageContactInfo_Image());



        dataBase.insert(TABLE_GROUP_CHAT, null, cValues);
        dataBase.close();
    }

    public ArrayList<Messages> getGroupChat(String id ) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_GROUP_CHAT, null, null, null, null, null, null);
        Cursor cursor = dataBase.query(TABLE_GROUP_CHAT,
                new String [] {KEY_ID,COLUMN_GROUP_ID_CHAT, COLUMN_GROUP_CHAT_MESSAGE_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME,COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME,COLUMN_GROUP_CHAT_MESSAGE_TYPE,COLUMN_GROUP_CHAT_MESSAGE_TEXT,COLUMN_GROUP_CHAT_MESSAGE_IMAGE,
                        COLUMN_GROUP_CHAT_MESSAGE_VOICE,COLUMN_GROUP_CHAT_MESSAGE_VIDEO,COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE,COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE ,COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID},
                COLUMN_GROUP_ID_CHAT + "=? ",
                new String[] { id},
                null, null, null);
        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupIdChat(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID_CHAT)));
                messages.setGroupMessageChatId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_ID)));
                messages.setGroupChatMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID)));
                messages.setGroupChatMessageSenderName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME)));
                messages.setGroupChatMessageIncomingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME)));
                messages.setGroupChatMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TYPE)));
                messages.setGroupChatMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TEXT)));
                messages.setGroupChatMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_IMAGE)));
                messages.setGroupChatMessageVoice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VOICE)));
                messages.setGroupChatMessageVideo(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VIDEO)));
                messages.setGroupChatMessageLongitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE)));
                messages.setGroupChatMessageLattitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE)));
                messages.setGroupChatMessageContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID)));
                messages.setGroupChatMessageContactInfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER)));

                messages.setGroupChatMessageContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    } public ArrayList<Messages> getGroupChatWithVarable(String id,int limit ) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_GROUP_CHAT, null, null, null, null, null, null);
        Cursor cursor = dataBase.query(TABLE_GROUP_CHAT,
                new String [] {KEY_ID,COLUMN_GROUP_ID_CHAT, COLUMN_GROUP_CHAT_MESSAGE_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME,COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME,COLUMN_GROUP_CHAT_MESSAGE_TYPE,COLUMN_GROUP_CHAT_MESSAGE_TEXT,COLUMN_GROUP_CHAT_MESSAGE_IMAGE,
                        COLUMN_GROUP_CHAT_MESSAGE_VOICE,COLUMN_GROUP_CHAT_MESSAGE_VIDEO,COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE,COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE ,COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID},
                COLUMN_GROUP_ID_CHAT + "=?" + " LIMIT " + limit + " OFFSET " + " (SELECT COUNT(*) FROM "+ TABLE_GROUP_CHAT + " )- " + limit+ " ; ",
                new String[] { id},
                null, null, null);
        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupIdChat(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID_CHAT)));
                messages.setGroupMessageChatId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_ID)));
                messages.setGroupChatMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID)));
                messages.setGroupChatMessageSenderName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME)));
                messages.setGroupChatMessageIncomingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME)));
                messages.setGroupChatMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TYPE)));
                messages.setGroupChatMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TEXT)));
                messages.setGroupChatMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_IMAGE)));
                messages.setGroupChatMessageVoice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VOICE)));
                messages.setGroupChatMessageVideo(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VIDEO)));
                messages.setGroupChatMessageLongitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE)));
                messages.setGroupChatMessageLattitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE)));
                messages.setGroupChatMessageContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID)));
                messages.setGroupChatMessageContactInfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER)));

                messages.setGroupChatMessageContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    }
    public ArrayList<Messages> getGroupChat1(String id ,int limit ) {
        dataBase = getWritableDatabase();
        String quesry= " select * from ( "+
                 " select * from "+ TABLE_GROUP_CHAT +" WHERE "+ COLUMN_GROUP_ID_CHAT + " = '" + id +"'" +"ORDER BY "  + KEY_ID + " DESC "+" limit "+ limit + " ) " +" ORDER BY "+ KEY_ID +" ASC ";
        Cursor cursor = dataBase.rawQuery(quesry, null);

        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupIdChat(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID_CHAT)));
                messages.setGroupMessageChatId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_ID)));
                messages.setGroupChatMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID)));
                messages.setGroupChatMessageSenderName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME)));
                messages.setGroupChatMessageIncomingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME)));
                messages.setGroupChatMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TYPE)));
                messages.setGroupChatMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TEXT)));
                messages.setGroupChatMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_IMAGE)));
                messages.setGroupChatMessageVoice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VOICE)));
                messages.setGroupChatMessageVideo(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VIDEO)));
                messages.setGroupChatMessageLongitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE)));
                messages.setGroupChatMessageLattitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE)));
                messages.setGroupChatMessageContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID)));
                messages.setGroupChatMessageContactInfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER)));

                messages.setGroupChatMessageContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    }
    public ArrayList<Messages> getGroupChat11(String id ,int limit) {
        dataBase = getWritableDatabase();


        Cursor cursor = dataBase.query(TABLE_GROUP_CHAT,
                new String [] {KEY_ID,COLUMN_GROUP_ID_CHAT, COLUMN_GROUP_CHAT_MESSAGE_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID, COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME,COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME,COLUMN_GROUP_CHAT_MESSAGE_TYPE,COLUMN_GROUP_CHAT_MESSAGE_TEXT,COLUMN_GROUP_CHAT_MESSAGE_IMAGE,
                        COLUMN_GROUP_CHAT_MESSAGE_VOICE,COLUMN_GROUP_CHAT_MESSAGE_VIDEO,COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE,COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE ,COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER,COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID},
                COLUMN_GROUP_ID_CHAT + "=? "+ " ORDER BY " + KEY_ID + " ASC "+ " limit " + limit,
                new String[] { id},
                null, null, null);
        ArrayList<Messages> messageGroupdetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setGroupIdChat(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_ID_CHAT)));
                messages.setGroupMessageChatId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_ID)));
                messages.setGroupChatMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_ID)));
                messages.setGroupChatMessageSenderName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_SENDER_NAME)));
                messages.setGroupChatMessageIncomingTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CROUP_CHAT_MESSAGE_INCOMINGTIME)));
                messages.setGroupChatMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TYPE)));
                messages.setGroupChatMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_TEXT)));
                messages.setGroupChatMessageImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_IMAGE)));
                messages.setGroupChatMessageVoice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VOICE)));
                messages.setGroupChatMessageVideo(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_VIDEO)));
                messages.setGroupChatMessageLongitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LONGITUDE)));
                messages.setGroupChatMessageLattitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_LATTITUDE)));
                messages.setGroupChatMessageContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINFO_ID)));
                messages.setGroupChatMessageContactInfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACtINFO_NUMBER)));

                messages.setGroupChatMessageContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_GROUP_CHAT_MESSAGE_CONTACTINO_IMAGE)));
                messageGroupdetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageGroupdetail;
    }
    public void inserRecord(User user) {
        dataBase = getWritableDatabase();
        cValues = new ContentValues();
        cValues.put(COLUMN_CONTACT_NUMBER, user.getContactNumber()); // Contact Name
        cValues.put(COLUMN_CONTACT_NAME, user.getContactName()); // Contact Name
        cValues.put(COLUMN_CONTACT_STATUS, user.getContactStatus()); // Contact Name
        cValues.put(COLUMN_CONTACT_EMAIL, user.getContactEmail());
        cValues.put(COLUMN_CONTACT_PHOTO,user.getContactImage());
           cValues.put(COLUMN_CONTACT_ID,user.getUsercontactid());
        dataBase.insert(TABLE_CONTACTS, null, cValues);

        dataBase.close();
    }
    public ArrayList<User> getContact() {
        dataBase = getWritableDatabase();

//    Getting data from database table
        cursor = dataBase.rawQuery(" select * from " + TABLE_CONTACTS + " ORDER BY " + KEY_ID + " DESC" ,null);

        ArrayList<User> contact = new ArrayList<User>();
        User user;
        if (cursor.moveToFirst()) {

            do {
                user = new User();
                user.setContactId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                user.setContactName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_NAME)));
                user.setContactNumber(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_NUMBER)));
                user.setContactEmail(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_EMAIL)));
                user.setContactStatus(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_STATUS)));
                user.setContactImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_PHOTO)));
                user.setContactRegId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_REG_ID)));
                user.setUsercontactid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CONTACT_ID)));

                contact.add(user);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return contact;
    }
    public int updateContact(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTACT_NAME, user.getContactName());
        values.put(COLUMN_CONTACT_NUMBER, user.getContactNumber());
        values.put(COLUMN_CONTACT_EMAIL, user.getContactEmail());
        values.put(COLUMN_CONTACT_STATUS, user.getContactStatus());
        values.put(COLUMN_CONTACT_PHOTO,  user.getContactImage());
        values.put(COLUMN_CONTACT_ID,user.getUsercontactid());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { (user.getContactId()) });

    }
 public void insertMessage(Messages messages) {
        dataBase = getWritableDatabase();
         cValues = new ContentValues();
          cValues.put(KEY_ID,messages.getMessageId());
         cValues.put(COLUMN_CHAT_SENDER_ID, messages.getMessageSenderId()); // Contact Name
         cValues.put(COLUMN_CHAT_RECIVER_ID, messages.getMessageReciverId());
         cValues.put(COLUMN_CHAT_TYPE,messages.getMessageType());// Contact Name
         cValues.put(COLUMN_CHAT_TEXT, messages.getMessageText()); // Contact Name
         cValues.put(COLUMN_CHAT_INCOMING_TIME, messages.getMessageInTime());
         cValues.put(COLUMN_CHAT_SENDERNAME,messages.getSender_Name());
         cValues.put(COLUMN_CHAT_SENDERIMAGE,messages.getSender_chat_image());
         cValues.put(COLUMN_CHAT_SENDERMOBILE,messages.getSenderMobile());

        // insert data into database
        dataBase.insert(TABLE_CHAT_MESSSAGE, null, cValues);

        dataBase.close();
    }
    public boolean deleteRowChatList(int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CHAT_MESSSAGE, KEY_ID + "=" + value, null) > 0;
    }
    public ArrayList<Messages> getMessage() {
        dataBase = getWritableDatabase();

//    Getting data from database table
        Cursor cursor = dataBase.query(TABLE_CHAT_MESSSAGE, null, null, null, null, null, null);

        ArrayList<Messages> messageDetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setMessageId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDER_ID)));
                messages.setMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_TEXT)));
                messages.setMessageInTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_INCOMING_TIME)));
                messages.setMessageReciverId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_RECIVER_ID)));
                messages.setMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_TYPE)));
                messages.setSender_Name(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERNAME)));
                messages.setSender_chat_image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERIMAGE)));
                messages.setSenderMobile(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERMOBILE)));


                messageDetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageDetail;
    }

    public ArrayList<Messages> getMessage1(String Senderid, String ReciverId, String sendid, String Recid) {
        dataBase = getWritableDatabase();

//    Getting data from database table
        Cursor cursor = dataBase.query(TABLE_CHAT_MESSSAGE, null, null, null, null, null, null);
        cursor = dataBase.query(TABLE_CHAT_MESSSAGE,
                new String [] {KEY_ID,COLUMN_CHAT_SENDER_ID, COLUMN_CHAT_TEXT, COLUMN_CHAT_INCOMING_TIME, COLUMN_CHAT_RECIVER_ID,COLUMN_CHAT_TYPE,COLUMN_CHAT_SENDERNAME,COLUMN_CHAT_SENDERIMAGE, COLUMN_CHAT_SENDERMOBILE},
                COLUMN_CHAT_SENDER_ID +"=?" +" AND " + COLUMN_CHAT_RECIVER_ID + "=?" + " OR " + COLUMN_CHAT_SENDER_ID +"=?" +" AND " + COLUMN_CHAT_RECIVER_ID +"=?",
                new String[] { Senderid,ReciverId,sendid,Recid},
                null, null, null);
        ArrayList<Messages> messageDetail = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setMessageSenderId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDER_ID)));
                messages.setMessageText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_TEXT)));
                messages.setMessageInTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_INCOMING_TIME)));
                messages.setMessageReciverId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_RECIVER_ID)));
                messages.setMessageType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_TYPE)));
                messages.setSender_Name(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERNAME)));
                messages.setSender_chat_image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERIMAGE)));
                messages.setSenderMobile(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_SENDERMOBILE)));

                messageDetail.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messageDetail;
    }

    public int updateMessage(Messages messages) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,messages.getMessageId());
        values.put(COLUMN_CHAT_SENDER_ID, messages.getMessageSenderId());
        values.put(COLUMN_CHAT_RECIVER_ID, messages.getMessageReciverId());
        values.put(COLUMN_MESSAGE_TYPE, messages.getMessageType());
        values.put(COLUMN_CHAT_TEXT, messages.getMessageText());
        values.put(COLUMN_CHAT_INCOMING_TIME, messages.getMessageInTime());
        values.put(COLUMN_CHAT_SENDERNAME,messages.getSender_Name());
        values.put(COLUMN_CHAT_SENDERIMAGE,messages.getSender_chat_image());
        values.put(COLUMN_CHAT_SENDERMOBILE,messages.getSenderMobile());


        // updating row
        return db.update(TABLE_CHAT_MESSSAGE, values,  KEY_ID + " = ? ", new String[] { (messages.getMessageId()) });

    }
    public int updateGroupMessage(Messages messages) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID,messages.getGroupMessageId());
        values.put(COLUMN_GROUP_ID, messages.getGroupId());
        values.put(COLUMN_GROUP_Name, messages.getGroupName());
        values.put(COLUMN_GROUP_Photo, messages.getGroupIcon());
        values.put(COLUMN_GROUP_CREATED_DATE, messages.getGroupCreatedDate());
        values.put(COLUMN_GROUP_ADMIN,messages.getGroupAdmin());
        values.put(COLUMN_GROUP_MESSAGE_SENDER_ID,messages.getGroupMessageSenderId());
        values.put(COLUMN_GROUP_MESSAGE_TYPE,messages.getGroupMessageType());
        values.put(COLUMN_GROUP_MESSAGE_SENDING_TIME,messages.getGroupMessageSendingTime());
        values.put(COLUMN_GROUP_MESSAGE_TEXT,messages.getGroupMessageText());
        values.put(COLUMN_GROUP_MESSAGE_IMAGE,messages.getGroupMessageImage());
        // updating row
        return db.update(TABLE_GROUP, values,  KEY_ID + " = ? ", new String[] { (messages.getMessageId()) });

    }
    public void insertChatRoom(Messages messages) {

        dataBase = getWritableDatabase();

        cValues = new ContentValues();
        cValues.put(KEY_ID,messages.getKeyId());
        cValues.put(COLUMN_MESSAGE_ID,messages.getChatroom_Message_id());
        cValues.put(COLUMN_MESSAGE_SENDER_ID, messages.getChatroom_Message_Senderid()); // Contact Name
        cValues.put(COLUMN_MESSAGE_RECIVER_ID, messages.getChatroomMessage_Reciverid()); // Contact Name
        cValues.put(COLUMN_MESSAGE_TEXT, messages.getChatroom_Message_Text()); // Contact Name
        cValues.put(COLUMN_MESSAGE_IMAGE,messages.getChatroom_Message_Image());
        cValues.put(COLUMN_MESSAGE_VIDEO,messages.getChatroom_Message_Video());
        cValues.put(COLUMN_MESSAGE_VOICE,messages.getChatroom_Message_Voice());
        cValues.put(COLUMN_MESSAGE_CONTACTINFO_IMAGE,messages.getChatroom_Message_ContactInfo_Image());
        cValues.put(COLUMN_MESSAGE_CONTACTNFO_NUMBER,messages.getChatroom_Message_ContactOnfo_Number());
        cValues.put(COLUMN_MESSAGE_CONTACTNFO_ID,messages.getChatroom_Message_ContactInfo_Id());
        cValues.put(COLUMN_CHAT_INCOMING_TIME, messages.getChatroom_message_inTime());
        cValues.put(COLUMN_MESSAGE_TYPE,messages.getChatroom_message_Type());
        cValues.put(COLUMN_CHATROOM_CREATION_DATE,messages.getChatroom_CreationDate());
        cValues.put(COLUMN_CHATROOM_LANGITUDE,messages.getChatroom_Longitude());
        cValues.put(COLUMN_CHATROOM_LATITUDE,messages.getChatroom_Latitude());
        cValues.put(COLUMN_MESSAGE_FILE_NAME,messages.getChatroom_FileName());
        cValues.put(COLUMN_IMAGE_PATH,messages.getChatroomImagePath());
        // insert data into database
        dataBase.insert(TABLE_CHAT_ROOM, null, cValues);

        dataBase.close();
    }
    public ArrayList<Messages> getCHatRoomMessage(String Senderid,String ReciverId,String Sendid,String Recid) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_CHAT_ROOM, null, null, null, null, null, null);
        Cursor cursor = dataBase.query(TABLE_CHAT_ROOM,
                new String [] {KEY_ID,COLUMN_MESSAGE_ID, COLUMN_MESSAGE_SENDER_ID, COLUMN_MESSAGE_RECIVER_ID, COLUMN_MESSAGE_TEXT,COLUMN_MESSAGE_IMAGE,COLUMN_MESSAGE_VIDEO,COLUMN_MESSAGE_VOICE,COLUMN_MESSAGE_CONTACTINFO_IMAGE,COLUMN_MESSAGE_CONTACTNFO_NUMBER,COLUMN_MESSAGE_CONTACTNFO_ID, COLUMN_MESSAGE_TYPE,COLUMN_CHAT_INCOMING_TIME,COLUMN_CHATROOM_CREATION_DATE,COLUMN_CHATROOM_LATITUDE,COLUMN_CHATROOM_LANGITUDE,COLUMN_MESSAGE_FILE_NAME,COLUMN_IMAGE_PATH},
                COLUMN_MESSAGE_SENDER_ID +"=?" + " AND " + COLUMN_MESSAGE_RECIVER_ID + "=?" + " OR " + COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID +"=? ",
                new String[] { Senderid,ReciverId,Sendid,Recid},
                null, null, null);
        ArrayList<Messages> messagechatroom = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setChatroom_Message_id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_ID)));
                messages.setChatroom_Message_Senderid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_SENDER_ID)));
                messages.setChatroomMessage_Reciverid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_RECIVER_ID)));
                messages.setChatroom_Message_Text(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TEXT)));
                messages.setChatroom_Message_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_IMAGE)));
                messages.setChatroom_Message_Video(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VIDEO)));
                messages.setChatroom_Message_Voice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VOICE)));
                messages.setChatroom_Message_ContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTINFO_IMAGE)));
                messages.setChatroom_Message_ContactOnfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_NUMBER)));
                messages.setChatroom_Message_ContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_ID)));
                messages.setChatroom_message_Type(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TYPE)));
                messages.setChatroom_message_inTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_INCOMING_TIME)));
                messages.setMessageCreationDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_CREATION_DATE)));
                messages.setChatroom_Latitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LATITUDE)));
                messages.setChatroom_Longitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LANGITUDE)));
                messages.setChatroom_FileName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_FILE_NAME)));
                messages.setChatroomImagePath(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_IMAGE_PATH)));

                messagechatroom.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messagechatroom;
    }  public ArrayList<Messages> getCHatRoomMessagewithLimit(String Senderid,String ReciverId,String Sendid,String Recid,int limit) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_CHAT_ROOM, null, null, null, null, null, null);
         /*cursor = dataBase.query(TABLE_CHAT_ROOM,
                new String [] {KEY_ID,COLUMN_MESSAGE_ID, COLUMN_MESSAGE_SENDER_ID, COLUMN_MESSAGE_RECIVER_ID, COLUMN_MESSAGE_TEXT,COLUMN_MESSAGE_IMAGE,COLUMN_MESSAGE_VIDEO,COLUMN_MESSAGE_VOICE,COLUMN_MESSAGE_CONTACTINFO_IMAGE,COLUMN_MESSAGE_CONTACTNFO_NUMBER,COLUMN_MESSAGE_CONTACTNFO_ID, COLUMN_MESSAGE_TYPE,COLUMN_CHAT_INCOMING_TIME,COLUMN_CHATROOM_CREATION_DATE,COLUMN_CHATROOM_LATITUDE,COLUMN_CHATROOM_LANGITUDE,COLUMN_IMAGE_PATH},
                COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID + "=?" + " OR " + COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID +"=? " + " ORDER BY " + KEY_ID + " ASC "+ " limit " + " 10 ",
                new String[] { Senderid,ReciverId,Sendid,Recid},
                null, null, null);*/
        String quesry= " select * from ( "+
                " select * from "+ TABLE_CHAT_ROOM +" WHERE "+ COLUMN_MESSAGE_SENDER_ID + " = '" + Senderid +"'" + " AND " +COLUMN_MESSAGE_RECIVER_ID + " = '" + ReciverId +"'" + " OR " + COLUMN_MESSAGE_SENDER_ID + " = '" + Sendid +"'" +" AND " + COLUMN_MESSAGE_RECIVER_ID + " = '" + Recid +"'" +"ORDER BY "  + KEY_ID + " DESC "+" limit "+ limit + " ) " +" ORDER BY "+ KEY_ID +" ASC ";
        Cursor cursor = dataBase.rawQuery(quesry, null);
        ArrayList<Messages> messagechatroom = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setChatroom_Message_id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_ID)));
                messages.setChatroom_Message_Senderid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_SENDER_ID)));
                messages.setChatroomMessage_Reciverid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_RECIVER_ID)));
                messages.setChatroom_Message_Text(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TEXT)));
                messages.setChatroom_Message_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_IMAGE)));
                messages.setChatroom_Message_Video(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VIDEO)));
                messages.setChatroom_Message_Voice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VOICE)));
                messages.setChatroom_Message_ContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTINFO_IMAGE)));
                messages.setChatroom_Message_ContactOnfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_NUMBER)));
                messages.setChatroom_Message_ContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_ID)));
                messages.setChatroom_message_Type(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TYPE)));
                messages.setChatroom_message_inTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_INCOMING_TIME)));
                messages.setMessageCreationDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_CREATION_DATE)));
                messages.setChatroom_Latitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LATITUDE)));
                messages.setChatroom_Longitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LANGITUDE)));
                messages.setChatroom_FileName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_FILE_NAME)));
                messages.setChatroomImagePath(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_IMAGE_PATH)));

                messagechatroom.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messagechatroom;
    }
 public ArrayList<Messages> getCHatRoomMessage1(String Senderid,String ReciverId,String Sendid,String Recid,int value) {
        dataBase = getWritableDatabase();

//    Getting data from database table
       // Cursor cursor = dataBase.query(TABLE_CHAT_ROOM, null, null, null, null, null, null);
         cursor = dataBase.query(TABLE_CHAT_ROOM,
                new String [] {KEY_ID,COLUMN_MESSAGE_ID, COLUMN_MESSAGE_SENDER_ID, COLUMN_MESSAGE_RECIVER_ID, COLUMN_MESSAGE_TEXT,COLUMN_MESSAGE_IMAGE,COLUMN_MESSAGE_VIDEO,COLUMN_MESSAGE_VOICE,COLUMN_MESSAGE_CONTACTINFO_IMAGE,COLUMN_MESSAGE_CONTACTNFO_NUMBER,COLUMN_MESSAGE_CONTACTNFO_ID, COLUMN_MESSAGE_TYPE,COLUMN_CHAT_INCOMING_TIME,COLUMN_CHATROOM_CREATION_DATE,COLUMN_CHATROOM_LATITUDE,COLUMN_CHATROOM_LANGITUDE,COLUMN_MESSAGE_FILE_NAME,COLUMN_IMAGE_PATH},
                COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID + "=?" + " OR " + COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID +"=? " + " ORDER BY " + KEY_ID + " DESC "+ " limit " + value,
                new String[] { Senderid,ReciverId,Sendid,Recid},
                null, null, null);
        ArrayList<Messages> messagechatroom = new ArrayList<Messages>();
        Messages messages;
        if (cursor.moveToFirst()) {

            do {
                messages = new Messages();
                messages.setKeyId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                messages.setChatroom_Message_id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_ID)));
                messages.setChatroom_Message_Senderid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_SENDER_ID)));
                messages.setChatroomMessage_Reciverid(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_RECIVER_ID)));
                messages.setChatroom_Message_Text(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TEXT)));
                messages.setChatroom_Message_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_IMAGE)));
                messages.setChatroom_Message_Video(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VIDEO)));
                messages.setChatroom_Message_Voice(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_VOICE)));
                messages.setChatroom_Message_ContactInfo_Image(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTINFO_IMAGE)));
                messages.setChatroom_Message_ContactOnfo_Number(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_NUMBER)));
                messages.setChatroom_Message_ContactInfo_Id(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_CONTACTNFO_ID)));
                messages.setChatroom_message_Type(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_TYPE)));
                messages.setChatroom_message_inTime(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHAT_INCOMING_TIME)));
                messages.setMessageCreationDate(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_CREATION_DATE)));
                messages.setChatroom_Latitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LATITUDE)));
                messages.setChatroom_Longitude(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_CHATROOM_LANGITUDE)));
                messages.setChatroom_FileName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_MESSAGE_FILE_NAME)));
                messages.setChatroomImagePath(cursor.getString(cursor.getColumnIndex(DatabaseHandler.COLUMN_IMAGE_PATH)));

                messagechatroom.add(messages);


            } while (cursor.moveToNext());

        }
        cursor.close();
        return messagechatroom;
    }


    public boolean deleteRow(int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CHAT_ROOM, KEY_ID + "=" + value, null) > 0;
    }
    public boolean deleteRowGroupChat(int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROUP_CHAT, KEY_ID + "=" + value, null) > 0;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHAT_ROOM, null, null);


    } public void deleteAllGroupChat()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROUP_CHAT, null, null);


    }
    public boolean deleteRowGroupList(int value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROUP, KEY_ID + "=" + value, null) > 0;
    }
    public Integer deleteData (String send,String recive,String senderid,String recid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CHAT_ROOM, COLUMN_MESSAGE_SENDER_ID+" = ?" + " AND " + COLUMN_MESSAGE_RECIVER_ID + " = ?" +" OR "+  COLUMN_MESSAGE_SENDER_ID +"=?" +" AND " + COLUMN_MESSAGE_RECIVER_ID +"=? ",new String[] {send,recive,senderid,recid});
    }
    public Integer deleteDataGroup (String groupid) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROUP_CHAT, COLUMN_GROUP_ID_CHAT+" = ?", new String[] {groupid});
    }
}
