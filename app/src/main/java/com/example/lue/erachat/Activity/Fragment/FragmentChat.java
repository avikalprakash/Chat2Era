package com.example.lue.erachat.Activity.Fragment;


import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.ScrollerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.CustomLayoutManager;
import com.example.lue.erachat.Activity.Activity.CustomRVItemTouchListener;
import com.example.lue.erachat.Activity.Activity.FileViewActivity;
import com.example.lue.erachat.Activity.Activity.ImageViewActivity;
import com.example.lue.erachat.Activity.Activity.RecyclerViewItemClickListener;
import com.example.lue.erachat.Activity.Activity.customButtonListener;
import com.example.lue.erachat.Activity.Adaptor.BlueTickHelper;
import com.example.lue.erachat.Activity.Adaptor.ItemListAdaptor;
import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.FireBase.MyFirebaseMessagingService;
import com.example.lue.erachat.Activity.GroupChat.ChatList;
import com.example.lue.erachat.Activity.Helper;
import com.example.lue.erachat.Activity.InterFace.OnLoadMoreListener;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.FileUtils;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.Other.VideoViewActivity;
import com.example.lue.erachat.Activity.Activity.OtherProfileViewActivity;
import com.example.lue.erachat.Activity.Settings.Profile;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.Activity.location.MapLocationActivity;
import com.example.lue.erachat.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.sample.calling.BaseActivity;
import com.sinch.android.rtc.sample.calling.CallScreenActivity;
import com.sinch.android.rtc.sample.calling.LoginActivity;
import com.sinch.android.rtc.sample.calling.PlaceCallActivity;
import com.sinch.android.rtc.sample.calling.SinchService;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.R.attr.bottom;
import static android.R.attr.phoneNumber;
import static android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER;
import static android.support.constraint.R.id.parent;
import static android.view.View.FOCUS_DOWN;



/**
 * Created by lue on 09-06-2017.
 */

public class FragmentChat extends BaseActivity implements customButtonListener  {
    static final private int DELAY_TIME = 30 * 1000;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    private static final int PICK_CONTACT = 6;
    private static boolean activityVisible;
    String BLUE_TICK_URL="http://erachat.condoassist2u.com/api/blueTickOnSeen";
    MyAsync myAsync;
    Messages messages;
    PostImage postImage;
    GroupMessage groupMessage;
    PostAudio postAudio;
    USerStatus uSerStatus;
    ImageView attach, imgphone, imgprofile;
     RecyclerView chatList;
    //Recy chatList;
    ImageView imageButtonSend;
    int position = 0;
    int pos = 0;
    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String audioFilePath;
    EmojiconEditText textchat;
    public ItemListAdaptor itemListAdaptor;
    ArrayList<Messages> chatroom = new ArrayList<>();
    ArrayList<Messages>handlerRecord=new ArrayList<>();
    ArrayList<Messages>HandlerRcGroup=new ArrayList<>();
    ArrayList<Messages> Chat_List = new ArrayList<>();
    ArrayList<Messages>GroupChat=new ArrayList<>();
    ArrayList<Messages>GroupList=new ArrayList<>();
    private boolean side = false;
    ImageView img_choosepic, img_takephoto, img_addfile, img_addlocation, img_choosevideo,
            img_takevideo, img_choosefile, img_chooseContact;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST_ = 2;
    static final int REQUEST_IMAGE_CAPTURE = 9;
    static final int SELECT_VIDEO = 4;
    static final int REQUEST_VIDEO_CAPTURE = 5;
    private static final int FILE_SELECT_CODE = 0;
    private String selectedVideoPath;

    String REGSID = "";
    String SenderId = "";
    String TextMessage = "";
    SharedPreferences preferencesImage;
    private String MyPREFERENCES;
    SharedPreferences.Editor editor;
    public String Senderid;
    DatabaseHandler databaseHandler;
    public static String MYID = "";
    String idvalue = "";
    String Username = "";
    String UserImage = "";
    Messages chat;
    String ImagestringGallery = "";
    String ImageContactInfo = "";
    String VideoString = "";
    String myNumber = "";
    ImageView dialog_image;
    Bitmap bitmap, bitmapContact;
    EmojIconActions emojIcon;
    ImageView emojiImageView;
    View rootView;
    String SendernameTitleBar = "";
    String SenderImageTitleBar = "";
    String SenderMobileNumber;
   // GPSTracker gpsTracker;
    byte[] contactInfoPhoto;
    String myuser_id = "";
    ImageView mike;
    String GroupIdDetail1 = "";
    String name = "";
    String contact_id = "";
    private Timer timer;
    private TimerTask timerTask;
    Uri captureimageUri;
    private boolean isRecording = false;
    int flagmike = 0;
    String AudioString="";
    String GroupId="";
    String GroupName="";
    String GroupPhoto="";
    final int SCROLLING_UP = -1;
   public static int databasesize=0;
    public static int datasizegroup=0;
    LinearLayout layout;
    CustomLayoutManager customLayoutManager;
     NestedScrollView nestedScrollView;
    String PhoneNumber="";
    LinearLayoutManager linearLayoutManager;
    public static int end=10;
    Chronometer mchronometer;
    String keyid="";
    ImageView blockuser;
    GetUserStatus getUserStatus;
    RelativeTimeTextView userstatus;
    RelativeTimeTextView timertime;
    String CurrentStatus="";
    String OwnStatus="";
    private String selectedFilePath;
    public static ImageView confmtick,confirmtick1;
    public static final String SENDER_MOBILE = "SenderNumber";
    public static final String MyPref = "MyPref";

    @Override
    protected void onStart() {
        super.onStart();
        File pictureFileDir1 = getDir1();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
        String date1 = dateFormat1.format(new Date());
        String photoFile = "Audio" + "_" + date1;
        audioFilePath =
                pictureFileDir1.getPath() + File.separator + photoFile;
        if (!pictureFileDir1.exists() && !pictureFileDir1.mkdirs()) {
            Toast.makeText(getApplicationContext(), getString(R.string.Cantcreatedirectorytosaveimage),
                    Toast.LENGTH_LONG).show();
            return;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = preferences.getString(getString(R.string.FCM_TOKEN), " ");
        Senderid = token;
        databaseHandler = new DatabaseHandler(getApplicationContext());
        rootView = findViewById(R.id.root_view);
        myAsync = new MyAsync();
        getUserStatus=new GetUserStatus();
        postImage = new PostImage();
        groupMessage = new GroupMessage();
        postAudio = new PostAudio();
        uSerStatus=new USerStatus();
        mchronometer=(Chronometer)findViewById(R.id.chrono);
       // Toast.makeText(getApplicationContext(), "CBA", Toast.LENGTH_LONG).show();
        //Shared prefence

        preferencesImage = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = preferencesImage.edit();
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
        MYID = myuser_id;
        myNumber = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("MYNUMBER", "");
        attach = (ImageView) findViewById(R.id.attach);
        textchat = (EmojiconEditText) findViewById(R.id.editText2);
        chatList = (RecyclerView) findViewById(R.id.chat_list);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested);
        blockuser=(ImageView)findViewById(R.id.blockuser);
        timertime=(RelativeTimeTextView)findViewById(R.id.timer);
        nestedScrollView.post(new Runnable() {
            @Override
            public void run() {
                nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                nestedScrollView.clearFocus();
            }
        });

        chatList.addOnItemTouchListener(new CustomRVItemTouchListener(this, chatList, new RecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

                /*if (GroupId.equals("")) {
                    int id = Integer.parseInt(chatroom.get(position).getKeyId());
                    databaseHandler.deleteRow(id);
                } else {
                    int id1 = Integer.parseInt(GroupChatList.get(position).getKeyId());
                    databaseHandler.deleteRowGroupChat(id1);
                }

                Toast.makeText(getApplicationContext(), "ItemClickedLong", Toast.LENGTH_LONG).show();
                view.setBackgroundColor(getResources().getColor(R.color.selectlist));*/
            }
        }));

        mike = (ImageView) findViewById(R.id.imageView19);
        emojiImageView = (ImageView) findViewById(R.id.emoji_btn);

        emojIcon = new EmojIconActions(this, rootView, textchat, emojiImageView);
        emojIcon.ShowEmojIcon();
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley);
        boolean b=true;
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                OwnStatus="typing";
                loadOwnStatus();
              //  Toast.makeText(getApplicationContext(),"mkmjnmjkm",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onKeyboardClose() {
                OwnStatus="online";
                loadOwnStatus();
             //  cc
            }
        });

        if (!Helper.isAppRunning(FragmentChat.this, "com.example.lue.erachat")) {
            OwnStatus="offline";
        }

        DatabaseReference presenceRef = FirebaseDatabase.getInstance().getReference("disconnectmessage");
// Write a string when this client loses connection
        presenceRef.onDisconnect().setValue("I disconnected!");
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(DataSnapshot snapshot) {
                                                   boolean connected = snapshot.getValue(Boolean.class);
                                                   if (connected) {
                                                       System.out.println("connected");
                                                   } else {
                                                       System.out.println("not connected");
                                                       OwnStatus="offline";
                                                   }
                                               }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textchat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, boolean hasFocus) {
                if (hasFocus) {
                    // Request focus in a short time because the
                    // keyboard may steal it away.
                    v.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!v.hasFocus()) {
                                v.requestFocus();
                            }
                        }
                    }, 200);
                }
            }
        });


        //Intent get
        Intent intent = getIntent();
        if (intent.getStringExtra("GroupIdChatList")==  null) {

        }else {
            GroupId = intent.getStringExtra("GroupIdChatList");
            Log.d("hnchdn",""+GroupId);
        }
        if(intent.getStringExtra("GroupId")!=null){
            GroupId=intent.getStringExtra("GroupId");
            Log.d("hnchssdn",""+GroupId);
        }
        else if (intent.getStringExtra("UserId") != null) {
            REGSID = intent.getStringExtra("UserId");
            idvalue = REGSID;

        }

        if (intent.getStringExtra("Username") != null) {
            Username = intent.getStringExtra("Username");
        }
        if (intent.getStringExtra("UserImage") != null) {
            UserImage = intent.getStringExtra("UserImage");
        }
        if (intent.getStringExtra("SenderId") != null) {
            SenderId = intent.getStringExtra("SenderId");
            idvalue = SenderId;

        }
        if (intent.getStringExtra("SenderName") != null) {
            SendernameTitleBar = intent.getStringExtra("SenderName");
        }
        if (intent.getStringExtra("SenderImage") != null) {
            SenderImageTitleBar = intent.getStringExtra("SenderImage");
        }
        if (intent.getStringExtra("SenderMobile") != null) {
            SenderMobileNumber = intent.getStringExtra("SenderMobile");
        }

        if(intent.getStringExtra("groupName")!=null){
            GroupName=intent.getStringExtra("groupName");
        } if(intent.getStringExtra("groupPhoto")!=null){
            GroupPhoto=intent.getStringExtra("groupPhoto");
        }if(intent.getStringExtra("keyid")!=null){
            keyid=intent.getStringExtra("keyid");
        }
        if (MyFirebaseMessagingService.UnreadMessages.size() > 0) {
            MyFirebaseMessagingService.UnreadMessages.removeAll(Arrays.asList(idvalue));
        }   if (MyFirebaseMessagingService.UnreadMessagesGroup.size() > 0) {

            MyFirebaseMessagingService.UnreadMessagesGroup.removeAll(Arrays.asList(GroupId));
        }
        loadstatus();
        //action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_title, null);
        TextView textView = (TextView) v.findViewById(R.id.actionbarTitle);
        CircleImageView circleImageView =(CircleImageView) v.findViewById(R.id.circleImageView);
        userstatus =(RelativeTimeTextView )v.findViewById(R.id.textView78);
        textView.setText(SendernameTitleBar);
        try {
            Picasso.with(this).load(SenderImageTitleBar).into(circleImageView);
        }catch (Exception e){}
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), OtherProfileViewActivity.class);
                intent1.putExtra("Id", SenderId);
                startActivity(intent1);
            }
        });

      //  new BlueTickReceive().execute();


      /*  String sTick="read";
        String MessageId="read";
        messages = new Messages();
        messages.setMessageId(MessageId);
        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        databaseHandler.insertChatRoom(new Messages("", myuser_id, idvalue, MessageId, "", "", "", "", "", "", date, "text", myNumber,"","","",""));
       */




        actionBar.setCustomView(v);




        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShowPopup(view);

            }
        });


        GroupChat=databaseHandler.getGroupChat(GroupId);
        datasizegroup=GroupChat.size();
        chatroom = databaseHandler.getCHatRoomMessage(idvalue, myuser_id, myuser_id, idvalue);
        databasesize=chatroom.size();

        GroupList=databaseHandler.getGroupwithId(GroupId);
        if(!GroupId.equals("")){
            GroupChat = databaseHandler.getGroupChat1(GroupId,10);
            itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat,idvalue);
            chatList.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext() );
            linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
            linearLayoutManager.setAutoMeasureEnabled( true );
            // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            chatList.setLayoutManager(linearLayoutManager);
            chatList.setAdapter(itemListAdaptor);
            itemListAdaptor.notifyDataSetChanged();
            chatList.setNestedScrollingEnabled(false);
            nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            nestedScrollView.clearFocus();
        }else {
            chatroom = databaseHandler.getCHatRoomMessagewithLimit(idvalue, myuser_id, myuser_id, idvalue,10);
            itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, idvalue);
            chatList.setNestedScrollingEnabled(false);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext() );
            linearLayoutManager.setOrientation( LinearLayoutManager.VERTICAL );
            linearLayoutManager.setAutoMeasureEnabled( true );
           // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            chatList.setLayoutManager(linearLayoutManager);
            chatList.setAdapter(itemListAdaptor);
            chatList.setNestedScrollingEnabled(false);
            nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            nestedScrollView.clearFocus();
        }


        final Handler Hnd = new Handler();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Hnd.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!GroupId.equals("")){

                            GroupChat=databaseHandler.getGroupChat(GroupId);
                            if(datasizegroup<GroupChat.size()) {
                                HandlerRcGroup=databaseHandler.getGroupChat1(GroupId,10);
                                    itemListAdaptor = new ItemListAdaptor(FragmentChat.this, HandlerRcGroup, idvalue);
                                    chatList.setNestedScrollingEnabled(false);
                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    linearLayoutManager.setAutoMeasureEnabled(true);
                                    chatList.setLayoutManager(linearLayoutManager);
                                    chatList.setAdapter(itemListAdaptor);
                                    datasizegroup = GroupChat.size();
                                    itemListAdaptor.notifyDataSetChanged();
                                  nestedScrollView.fullScroll(FOCUS_DOWN);
                                  nestedScrollView.clearFocus();
                                  textchat.requestFocus();

                            }
                        }else {

                            chatroom = databaseHandler.getCHatRoomMessage(idvalue, myuser_id, myuser_id, idvalue);
                            if(databasesize<chatroom.size()) {
                                handlerRecord=databaseHandler.getCHatRoomMessagewithLimit(idvalue, myuser_id, myuser_id, idvalue,10);
                               // if(!(idjv.equals(myuser_id)))
                                //{
                                    itemListAdaptor = new ItemListAdaptor(FragmentChat.this, handlerRecord, idvalue);
                                    chatList.setNestedScrollingEnabled(false);
                                    linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    linearLayoutManager.setAutoMeasureEnabled(true);
                                    chatList.setLayoutManager(linearLayoutManager);
                                    chatList.setAdapter(itemListAdaptor);
                                    databasesize = chatroom.size();
                                    itemListAdaptor.notifyDataSetChanged();
                                    nestedScrollView.fullScroll(FOCUS_DOWN);
                                    nestedScrollView.clearFocus();
                                    Log.d("jnfjnj", "$$$$$$");
                                    textchat.requestFocus();
                                //}
                            }
                        }

                    }
                });
            }
        }, 3500, 3500);

       /* final SinchClient sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(idvalue)   //   current-user-id
                .applicationKey("9d175782-83e3-4fba-b823-e07452aa1558")       //  key
                .applicationSecret("B1xULMGYAESa5W/nUglBTQ==")   // secret
                .environmentHost("clientapi.sinch.com")    //  environmentHost
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.start();*/
        imgphone = (ImageView) findViewById(R.id.imgphone);
        imgphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*try {
                    String INnumber = SenderMobileNumber.replace("+60", "+91");
                    if (call == null) {
                        call = sinchClient.getCallClient().callPhoneNumber(INnumber);
                        call.addCallListener(new SinchCallListener());
                        // callButton.setText("Hang Up");
                    } else {
                        call.hangup();
                    }
                }catch (Exception e){}*/
               /*SharedPreferences sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
                SenderMobileNumber=sharedpreferences.getString(SENDER_MOBILE,"");*/
               try {
                   String mobileNo = SenderMobileNumber.replace("+60", "");
                   if (mobileNo.isEmpty()) {
                       // Toast.makeText(getApplicationContext(), "Please enter a user to call", Toast.LENGTH_LONG).show();
                       return;
                   }

                   //  Call call = getSinchServiceInterface().callUser(userName, headers);
                   Call call = getSinchServiceInterface().callUser(mobileNo);
                   String callId = call.getCallId();

                   Intent callScreen = new Intent(getApplicationContext(), CallScreenActivity.class);
                   callScreen.putExtra(SinchService.CALL_ID, callId);
                   callScreen.putExtra("photo", SenderImageTitleBar);
                   startActivity(callScreen);
               }catch (Exception e){}

               /* Intent intent = new Intent(FragmentChat.this, PlaceCallActivity.class);
               *//* intent.putExtra("user_id",idvalue);
                intent.putExtra("number",SenderMobileNumber);*//*
                intent.putExtra("number",mobileNo);
                startActivity(intent);*/
            }
        });

        imgprofile = (ImageView) findViewById(R.id.imgprofile);
        imgprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), DashbordActivity.class);
                intent1.putExtra("EditProfile","true");
                startActivity(intent1);
            }
        });
        blockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getApplicationContext(), DashbordActivity.class);
                intent2.putExtra("Key","true");
                startActivity(intent2);
            }
        });
        imageButtonSend = (ImageView) findViewById(R.id.imageView20);

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                TextMessage = textchat.getText().toString();

                GroupList=databaseHandler.getGroup();
                if (!GroupId.equals("")) {
                    if (!TextMessage.equals("")) {
                        if (containsgroup(GroupList, GroupId)) {
                            updateMessageGroup();
                            databaseHandler.insertGroupChat(new Messages(GroupId, "", myuser_id, "", date, "text", TextMessage, "", "", "", "", "", "", "", ""));

                        } else {
                            databaseHandler.insertGroup(new Messages("", GroupId, GroupName, GroupPhoto, SenderId, "text", TextMessage, date));
                            databaseHandler.insertGroupChat(new Messages(GroupId, "", myuser_id, "", date, "text", TextMessage, "", "", "", "", "", "", "", ""));

                        }
                        GroupChat = databaseHandler.getGroupChat1(GroupId,10);
                        itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat, idvalue);

                        chatList.setAdapter(itemListAdaptor);
                        textchat.setText("");
                        loadDataGroup();


                    }
                } else {

                    if (!TextMessage.equals("")) {
                        Chat_List = databaseHandler.getMessage();
                        if (containsCardno(Chat_List, SenderId)) {
                            updateMessage();
                        } else {

                            databaseHandler.insertMessage(new Messages(myuser_id, idvalue, "text", TextMessage, date, Username, UserImage));
                        }

                        databaseHandler.insertChatRoom(new Messages("", myuser_id, idvalue, TextMessage, "", "", "", "", "", "", date, "text", myNumber,"","","",""));

                        chatroom = databaseHandler.getCHatRoomMessagewithLimit(idvalue, myuser_id, myuser_id, idvalue,10);
                        itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, idvalue);

                        chatList.setAdapter(itemListAdaptor);
                        textchat.setText("");
                        loadData();

                    }
                }


            }
        });

        mike.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setTag(true);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    try {
                        Toast.makeText(getApplicationContext(),"Recording start",Toast.LENGTH_SHORT);
                        recordAudio(v);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(getApplicationContext(),"Recording stop",Toast.LENGTH_SHORT);
                    stopClicked(v);

                }
                return true;
            }
        });
        if (nestedScrollView != null) {

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                public static final String TAG = "syuhwushy";

                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        Log.i(TAG, "Scroll DOWN");
                    }
                    if (scrollY < oldScrollY) {
                        Log.i(TAG, "Scroll UP");
                    }

                    if (scrollY == 0) {
                        Log.i(TAG, "TOP SCROLL");
                        Log.d("sjsuj",""+chatroom.size());
                        loadMoreItems();
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        Log.i(TAG, "BOTTOM SCROLL");
                    }
                }
            });
        }

        chatList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
              //  Toast.makeText(getApplicationContext(), "ABC", Toast.LENGTH_LONG).show();
                //  itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat,idvalue);
               // confmtick=(ImageView)findViewById(R.id.conftick);
                new BlueTickSend().execute();

            }
        });
    }



    class BlueTickSend extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(FragmentChat.this);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(BLUE_TICK_URL);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                if (SenderId.equals("")) {
                    jsonObject.accumulate("user_id", idvalue);
                } else {
                    jsonObject.accumulate("user_id", SenderId);
                }
                jsonObject.accumulate("sender_id", myuser_id);




                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }

            return s;

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
          //  pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                boolean check  = objone.getBoolean("error");
                if(check) {
                    String message = objone.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentChat.this);
                    builder.setMessage(message)
                            .setNegativeButton("ok", null)
                            .create()
                            .show();
                }else{
                    BlueTickHelper b=new BlueTickHelper();
                  //  Messages b = new Messages();
                    String sTick="Tick";

         b.setTick(sTick);
                    /*String sd= b.getTick();
                    String s= objone.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentChat.this);
                    builder.setMessage(sd)
                            .setNegativeButton("ok", null)
                                .create()
                            .show();*/

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }

    private String readadsResponse(HttpResponse httpResponse) {

        InputStream is = null;
        String return_text = "";
        try {
            is = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
            Log.d("return1230", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;
    }

    class BlueTickReceive extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(FragmentChat.this);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();*/

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(BLUE_TICK_URL);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                if (SenderId.equals("")) {
                    jsonObject.accumulate("user_id", idvalue);
                } else {
                    jsonObject.accumulate("user_id", SenderId);
                }
                jsonObject.accumulate("sender_id", myuser_id);




                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }

            return s;

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            //  pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                boolean check  = objone.getBoolean("error");
                if(check) {
                    String message = objone.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentChat.this);
                    builder.setMessage(message)
                            .setNegativeButton("ok", null)
                            .create()
                            .show();
                }else{
                  /*  BlueTickHelper b=new BlueTickHelper();
                    //  Messages b = new Messages();
                    String sTick="Tick";

                    b.setTick(sTick);*/
                    String sTick="read";
                    messages = new Messages();
                    messages.setMessageId(sTick);
                   // handler.insertChatRoom(new Messages(MessageId, SenderId, Reciverid, MessageText,"", "", "", "", "", "", MessageInTime, MessageType, Currentdate,"","","",""));
                    /*String sd= b.getTick();
                    String s= objone.getString("message");
                    AlertDialog.Builder builder = new AlertDialog.Builder(FragmentChat.this);
                    builder.setMessage(sd)
                            .setNegativeButton("ok", null)
                                .create()
                            .show();*/

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    protected static boolean isVisible = false;



    class USerStatus extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {


            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/setOnlineOffline");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", myuser_id);
                jsonObject.accumulate("current_status",OwnStatus);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            OwnStatus="";
            try {
                Log.d("xnjnj"," "+s);
                JSONObject jsonObject=new JSONObject(s);
                String message=jsonObject.getString("message");
                JSONArray jsonArray=new JSONArray(message);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    String UserStatus=jsonObject1.getString("current_status");
                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("USERSTATUS", UserStatus).commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
    public void loadOwnStatus() {

        uSerStatus = new USerStatus();
        if (Utils.isNetworkAvailable(FragmentChat.this) && uSerStatus.getStatus() != AsyncTask.Status.RUNNING) {

            uSerStatus.execute();

        } else {
            Toast.makeText(FragmentChat.this, "NO net avalable", Toast.LENGTH_LONG).show();
        }
    }
    public void recordAudio(View view) throws IOException {
        isRecording = true;
        mchronometer.setVisibility(View.VISIBLE);
        mchronometer.setBase(SystemClock.elapsedRealtime());
        mchronometer.start();



        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mediaRecorder.start();
    }

    public void stopClicked(View view) {
        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        mchronometer.stop();
        if (isRecording) {
            try {
                if (mediaRecorder != null) {

                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                    isRecording = false;
                }

             else{
                mediaPlayer.release();
                mediaPlayer = null;

            }
                if (!GroupId.equals("")) {
                    if(containsgroup(GroupList,GroupId)){

                        databaseHandler.insertGroupChat(new Messages(GroupId,"",myuser_id,"",date,"voice","","",audioFilePath,"","","","","",""));

                    }else {
                        databaseHandler.insertGroup(new Messages("", GroupId, GroupName, GroupPhoto, SenderId, "text", TextMessage, date));
                        databaseHandler.insertGroupChat(new Messages(GroupId,"",myuser_id,"",date,"voice","","",audioFilePath,"","","","","",""));

                    }
                    GroupChat=databaseHandler.getGroupChat(GroupId);
                    itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat, idvalue);

                    chatList.setAdapter(itemListAdaptor);
                    encodeAudio(audioFilePath);
                    loadDataGroup();

                }else {
                    databaseHandler.insertChatRoom(new Messages("", myuser_id, idvalue, "", "", "", audioFilePath, "", "", "", date, "voice", myNumber,"","","",""));
                    encodeAudio(audioFilePath);
                    loadAudio();
                }


            } catch (RuntimeException ex) {
                Toast.makeText(getApplicationContext(), "Hold to Record Release to send", Toast.LENGTH_LONG).show();
            }
        }



    }

    boolean containsCardno(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {
            try {
                if (!SenderId.equals("")) {
                    if ((item.getMessageSenderId().equals(myuser_id) && item.getMessageReciverId().equals(SenderId)) || ((item.getMessageSenderId().equals(SenderId) && item.getMessageReciverId().equals(myuser_id)))) {
                        return true;
                    }
                } else {
                    try {
                        if ((item.getMessageSenderId().equals(myuser_id) && item.getMessageReciverId().equals(idvalue)) || ((item.getMessageSenderId().equals(idvalue) && item.getMessageReciverId().equals(myuser_id)))) {
                            return true;
                        }
                    } catch (Exception e) {
                    }
                }
            }catch (Exception e){}
        }
        return false;
    }
    boolean containsgroup(ArrayList<Messages> contact1, String name) {
        for (Messages item : contact1) {

                if (item.getGroupId().equals(name) ) {
                    return true;
                }

        }
        return false;
    }

    public void onShowPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.dialog_attach, null, false);
        // find the ListView in the popup layout
        img_choosepic = (ImageView) inflatedView.findViewById(R.id.imageView25);
        img_takephoto = (ImageView) inflatedView.findViewById(R.id.imageView26);
        img_addfile = (ImageView) inflatedView.findViewById(R.id.imageView23);
        img_addlocation = (ImageView) inflatedView.findViewById(R.id.imageView24);
        img_choosevideo = (ImageView) inflatedView.findViewById(R.id.imageView27);
        img_takevideo = (ImageView) inflatedView.findViewById(R.id.imageView28);
        img_choosefile = (ImageView) inflatedView.findViewById(R.id.imageView23);
        img_chooseContact = (ImageView) inflatedView.findViewById(R.id.imageView17);
        // get device size
        Display display = this.getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        PopupWindow popupWindow = new PopupWindow(inflatedView, size.x - 0, size.y / 4, true);

        // set a background drawable with rounders corners
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        // make it focusable to show the keyboard to enter in `EditText`
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // make it outside touchable to dismiss the popup window
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 100);
        img_takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                captureimageUri = getApplicationContext().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, captureimageUri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });
        img_choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });
        img_choosevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takeVideoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takeVideoIntent, SELECT_VIDEO);
                }
                // Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
               // startActivityForResult(i, SELECT_VIDEO);


            }
        });
        img_takevideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakeVideoIntent();
            }
        });
        img_choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
        img_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MapLocationActivity.class);
                intent.putExtra("id",idvalue);
                intent.putExtra("GroupId",GroupId);
                intent.putExtra("SenderName",SendernameTitleBar);
                startActivity(intent);

            }
        });
        img_chooseContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

                startActivityForResult(intent, PICK_CONTACT);
            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            //  bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

            Intent intent = new Intent(FragmentChat.this, ImageViewActivity.class);
            intent.putExtra("ImageString", uri.toString());
            intent.putExtra("ReciverId", idvalue);
            intent.putExtra("GroupId",GroupId);
            intent.putExtra("SenderName",SendernameTitleBar);
            intent.putExtra("SenderImage",SenderImageTitleBar);
            startActivity(intent);
        }
        if (requestCode == PICK_IMAGE_REQUEST_ && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();


            File file = new File(getRealPathFromURI(getApplicationContext(), uri));
            editor.putString("URI", String.valueOf(file));
            editor.commit();
            String fileq = preferencesImage.getString("URI", "");
            File fl = new File(fileq);
            if (fl.exists()) {

                Drawable d = Drawable
                        .createFromPath(fl.getAbsolutePath());


                chatList.setBackground(d);
            }

        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(FragmentChat.this, ImageViewActivity.class);
            intent.putExtra("ImageString", captureimageUri.toString());
            intent.putExtra("ReciverId", idvalue);
            intent.putExtra("GroupId",GroupId);
            intent.putExtra("SenderName",SendernameTitleBar);
            intent.putExtra("SenderImage",SenderImageTitleBar);
            startActivity(intent);
        }
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK && data != null && data.getData() != null) {
           // Toast.makeText(getApplicationContext(), "Working On it", Toast.LENGTH_LONG).show();
            Uri videoUri;
            videoUri = data.getData();
            String UriData = String.valueOf(videoUri);
            selectedVideoPath = getPath(data.getData());

            if (selectedVideoPath == null) {

                finish();
            } else {

                Intent intent = new Intent(FragmentChat.this, VideoViewActivity.class);
                intent.putExtra("VideoString", UriData);
                intent.putExtra("ReciverId", idvalue);
                intent.putExtra("SenderName",SendernameTitleBar);
                intent.putExtra("SenderImage",SenderImageTitleBar);
                startActivity(intent);

            }

        }
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
           // Toast.makeText(getApplicationContext(), "Working On it", Toast.LENGTH_LONG).show();
            Uri videoUri = data.getData();

            videoUri = data.getData();
            String UriData = String.valueOf(videoUri);
            selectedVideoPath = getPath(data.getData());

            if (selectedVideoPath == null) {

                finish();
            } else {


                Intent intent = new Intent(FragmentChat.this, VideoViewActivity.class);
                intent.putExtra("VideoString", UriData);
                intent.putExtra("ReciverId", idvalue);
                intent.putExtra("SenderName",SendernameTitleBar);
                intent.putExtra("SenderImage",SenderImageTitleBar);
                startActivity(intent);


            }
        }
        if (requestCode == FILE_SELECT_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
           // Toast.makeText(getApplicationContext(), "Working On it", Toast.LENGTH_LONG).show();
            Uri uri = data.getData();
            DateFormat df = new SimpleDateFormat(" HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
           String fileSelectedPath= String.valueOf(uri);
            int index = fileSelectedPath.lastIndexOf("\\");
            String fileName = fileSelectedPath.substring(index + 1);
            fileName = fileName.substring(fileName.lastIndexOf("/"));
           /* if (!fileName.equals("")){
                databaseHandler.insertChatRoom(new Messages("", myuser_id, Senderid, "", "", "", "", "", "", "", date, "location", "",fileName,""));
            }*/
           // Toast.makeText(getApplicationContext(), fileName, Toast.LENGTH_LONG).show();
           /* databaseHandler.insertChatRoom(new Messages("", myuser_id, Senderid, "", "", "", "", "", "", "", date, "location", "",fileSelectedPath,""));
            chatroom = databaseHandler.getCHatRoomMessage(idvalue, myuser_id, myuser_id, idvalue);
            itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, SenderId);
            chatList.setAdapter(itemListAdaptor);*/
            // Get the path
            if(!GroupId.equals("")){
                databaseHandler.insertGroupChat(new Messages(GroupId,"",myuser_id,"",date,"file","","","","",fileSelectedPath,"","","",""));

            }else {
                databaseHandler.insertChatRoom(new Messages("", myuser_id, Senderid, "", "", "", "", "", "", "", date, "file", "",fileSelectedPath,""));
            }
          /*  Intent intent = new Intent(FragmentChat.this, FileViewActivity.class);
            intent.putExtra("FileString", uri.toString());
            intent.putExtra("ReciverId", idvalue);
            intent.putExtra("GroupId",GroupId);
            intent.putExtra("SenderName",SendernameTitleBar);
            intent.putExtra("SenderImage",SenderImageTitleBar);
            intent.putExtra("id",idvalue);
            startActivity(intent);*/
            String path = null;
            try {
                path = FileUtils.getPath(this, uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        }
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri contactData = data.getData();
            String _ID = ContactsContract.Contacts._ID;
            ContentResolver contentResolver = getContentResolver();
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
            Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                contact_id = c.getString(c.getColumnIndex(_ID));
                String phoneNumber = null;
                name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                Log.d("ContactName", name);
                int hasPhoneNumber = Integer.parseInt(c.getString(c.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {

                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                    }phoneCursor.close();
                    PhoneNumber=phoneNumber;
                Uri photoUri = Uri.parse("");
                if (c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)) != null) {
                    photoUri = Uri.parse(c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
                    try {
                        bitmapContact = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("Imageview", String.valueOf(bitmapContact));

                    ImageContactInfo = encodeToBase64(bitmapContact, Bitmap.CompressFormat.JPEG, 40);
                }


                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                if(GroupId.equals("")){
                    if (contact_id != null) {
                databaseHandler.insertChatRoom(new Messages("", myuser_id, idvalue, "", "", "", "", ImageContactInfo, name, contact_id, date, "text", myNumber,"","","",""));

                chatroom = databaseHandler.getCHatRoomMessage(idvalue, myuser_id, myuser_id, idvalue);
                itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, SenderId);
                chatList.setAdapter(itemListAdaptor);
                        loadContact();
                    }
                }else {
                    if (contact_id != null) {
                        databaseHandler.insertGroupChat(new Messages(GroupId, "", myuser_id, "", date, "contact", "", "", "", "", "", "", contact_id, name, ImageContactInfo));

                        GroupChat = databaseHandler.getGroupChat(GroupId);
                        itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat, idvalue);

                        chatList.setAdapter(itemListAdaptor);
                        chatList.scrollToPosition(GroupChat.size() - 1);


                        loadContact();
                    }
                }
                }
            }
        }

    }

    public byte[] openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    contactInfoPhoto = data;
                    return new byte[0];

                }
            }
        } finally {
            cursor.close();
        }
        return contactInfoPhoto;
    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null
                , MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else return null;
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    //menuitem
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (ChatList.class != null) {
            inflater.inflate(R.menu.menu_main3, menu);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_wallpaper:
                Toast.makeText(getApplicationContext(), "wallpaper clicked", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                   // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST_);
                return true;
            case R.id.action_blockuser:
                Intent intent1=new Intent(getApplicationContext(),DashbordActivity.class);
                intent1.putExtra("Key","BlockUser");
                startActivity(intent1);
                return true;
            case R.id.action_clear_Chat:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do You Want to Clear all Chat");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if(!GroupId.equals("")){
                                    databaseHandler.deleteDataGroup(GroupId);
                                    GroupChat=databaseHandler.getGroupChat(GroupId);
                                        itemListAdaptor = new ItemListAdaptor(FragmentChat.this, GroupChat, idvalue);
                                        chatList.setAdapter(itemListAdaptor);


                                }else {
                                    databaseHandler.deleteData(myuser_id,idvalue,idvalue,myuser_id);
                                    chatroom = databaseHandler.getCHatRoomMessage(idvalue, myuser_id, myuser_id, idvalue);
                                    itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, idvalue);
                                    chatList.setAdapter(itemListAdaptor);
                                    itemListAdaptor.notifyDataSetChanged();

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SHOW_PROCESS_DIALOG:

                    break;
                case HIDE_PROCESS_DIALOG:

                    break;
            }
            return false;
        }
    });

    @Override
    public void onButtonClickListner(int position, String value) {
        Toast.makeText(getApplicationContext(),position+value,Toast.LENGTH_LONG).show();

    }

    class MyAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);

            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/oneToOneText");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                if (SenderId.equals("")) {
                    jsonObject.accumulate("user_id", idvalue);
                } else {
                    jsonObject.accumulate("user_id", SenderId);
                }
                jsonObject.accumulate("sender_id", myuser_id);
                jsonObject.accumulate("message", TextMessage);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);

            ImagestringGallery = "";
            nestedScrollView.clearFocus();
            textchat.requestFocus();
        }
    }
    class GetUserStatus extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            //handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);

            String s = "";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getCurrentStatus");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", idvalue);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String message=jsonObject.getString("message");

                    JSONObject jsonObject1=new JSONObject(message);

                    CurrentStatus=jsonObject1.getString("current_status");
                Log.d("hbhcbhb",""+CurrentStatus);
                if(CurrentStatus.equals("online")){
                    userstatus.setText(CurrentStatus);
                }else if(CurrentStatus.equals("typing")){
                    userstatus.setText(" is typing.. ");
                }else {
                  //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    try {
                        /*Date mDate = sdf.parse(CurrentStatus);

                        long timeInMilliseconds = mDate.getTime();
                        System.out.println("Date in milli :: " + timeInMilliseconds);*/
                        String myDate = CurrentStatus;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = sdf.parse(myDate);
                        long millis = date.getTime();
                        timertime.setReferenceTime(millis);
                        String kmfkm= String.valueOf(timertime.getText());
                        Log.d("bfejvnjs",""+kmfkm);
                        if(kmfkm.contains("Completed")) {
                            String dvjn = kmfkm.replaceAll("Completed", "LastSeen");
                            userstatus.setText(dvjn);
                        }else {
                            String dvjn ="Last seen "+kmfkm;
                            userstatus.setText(dvjn);
                            Log.d("bfejvnj",""+dvjn);
                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                    PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("OTHERUSERSTATUS", CurrentStatus).commit();



            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public void loadData() {
        myAsync = new MyAsync();
        postImage = new PostImage();
        if (Utils.isNetworkAvailable(FragmentChat.this) && myAsync.getStatus() != AsyncTask.Status.RUNNING) {
            if (!TextMessage.equals("")) {
                myAsync.execute();
            }
        } else {
            Toast.makeText(FragmentChat.this, "NO net available", Toast.LENGTH_LONG).show();
        }
    } public void loadstatus() {
        getUserStatus = new GetUserStatus();
        if (Utils.isNetworkAvailable(FragmentChat.this) && getUserStatus.getStatus() != AsyncTask.Status.RUNNING) {

            getUserStatus.execute();

        } else {
            Toast.makeText(FragmentChat.this, "NO net available", Toast.LENGTH_LONG).show();
        }
    }


    private String readResponse(HttpResponse httpResponse) {
        InputStream is = null;
        String return_text = "";
        try {
            is = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
            Log.d("return_text", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;


    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentChat.activityPaused();
        if (myAsync.getStatus() == AsyncTask.Status.RUNNING) {
            myAsync.cancel(true);
        }
        if (postAudio.getStatus() == AsyncTask.Status.RUNNING) {
            postAudio.cancel(true);
        }
        if (timer != null) {
            timer.cancel();
        }
        OwnStatus="offline";

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (myAsync.getStatus() == AsyncTask.Status.RUNNING) {
            myAsync.cancel(true);
        }
        if (postAudio.getStatus() == AsyncTask.Status.RUNNING) {
            postAudio.cancel(true);
        }
    }

    public void updateMessage() {
        databaseHandler = new DatabaseHandler(getApplicationContext());
        Chat_List = databaseHandler.getMessage();
        for (int i = 0; i < Chat_List.size(); i++) {
try {
    if (((Chat_List.get(i).getMessageSenderId().equals(SenderId)) && Chat_List.get(i).getMessageReciverId().equals(myuser_id)) || ((Chat_List.get(i).getMessageSenderId().equals(myuser_id)) && Chat_List.get(i).getMessageReciverId().equals(SenderId))) {

        position = i;

    }
}catch (Exception e){}


        }

        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Messages c = Chat_List.get(position);
        c.setMessageSenderId(myuser_id);
        c.setMessageReciverId(idvalue);
        if (!TextMessage.equals("")) {
            c.setMessageText(TextMessage);
        }
        c.setMessageType("text");
        c.setMessageInTime(date);
        Log.d("insetmessage", "" + myuser_id + idvalue);
        databaseHandler.updateMessage(c);

    }
    public void updateMessageGroup() {
        databaseHandler = new DatabaseHandler(getApplicationContext());
        GroupList = databaseHandler.getGroupwithId(GroupId);
        for (int i = 0; i < GroupList.size(); i++) {

            if ((GroupList.get(i).getGroupId().equals(SenderId))) {

                position = i;

            }

        }

        DateFormat df = new SimpleDateFormat(" HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        Messages c = GroupList.get(position);
        c.setGroupMessageText(TextMessage);
        c.setGroupMessageType("text");
        c.setMessageInTime(date);
        Log.d("insetmessage", "" + myuser_id + idvalue);
        databaseHandler.updateGroup(c);

    }

    //Show popup

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    //encode bitmanp data
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    //Image

    class PostImage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                String URL="";
                if(GroupId.equals("")) {
                     URL = "http://erachat.condoassist2u.com/api/oneToOneContactRefer";
                }else {
                    URL = "http://erachat.condoassist2u.com/api/groupContactRefer";
                }

                HttpPost httpPost = new HttpPost(URL);

                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                if(!GroupId.equals("")) {
                    jsonObject.accumulate("group_id",GroupId);
                }else {

                    if (SenderId.equals("")) {
                        jsonObject.accumulate("receiver_id", REGSID);
                    } else {
                        jsonObject.accumulate("receiver_id", SenderId);
                    }
                }
                jsonObject.accumulate("sender_id", myuser_id);
                jsonObject.accumulate("contact_id", contact_id);
                Log.d("nmaebald",""+PhoneNumber);
                jsonObject.accumulate("contact_no", PhoneNumber);
                jsonObject.accumulate("contact_image", ImageContactInfo);
                jsonObject.accumulate("contact_name", name);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcuteContact", "" + s);
            contact_id="";
            name="";
            nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            nestedScrollView.clearFocus();
        }
    }

    class GroupMessage extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/sendGroupMessage");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();

                jsonObject.accumulate("group_id", GroupId);

                jsonObject.accumulate("sender_id", myuser_id);
                if(!AudioString.equals("")){
                    jsonObject.accumulate("message",AudioString );
                    jsonObject.accumulate("type", "voice");
                }else if(!TextMessage.equals("")) {
                    jsonObject.accumulate("message", TextMessage);
                    jsonObject.accumulate("type", "text");
                }

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcuteGroup", "" + s);
            AudioString="";
            TextMessage="";


        }
    }

    class PostAudio extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s = "";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/oneToOneVoice");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", idvalue);

                jsonObject.accumulate("sender_id", myuser_id);
                jsonObject.accumulate("voice", AudioString);

                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            mchronometer.setVisibility(View.GONE);
            Log.d("onPostExcuteAudio", "" + s);


        }
    }

    public void loadDataGroup() {

        groupMessage = new GroupMessage();
        if (Utils.isNetworkAvailable(FragmentChat.this) && groupMessage.getStatus() != AsyncTask.Status.RUNNING) {
            if (!TextMessage.equals("")) {
                groupMessage.execute();
            }else if(!AudioString.equals("")){
                groupMessage.execute();
            }
        } else {
            Toast.makeText(FragmentChat.this, "NO net available", Toast.LENGTH_LONG).show();
        }
    }

    public void loadContact() {

        postImage = new PostImage();
        if (Utils.isNetworkAvailable(FragmentChat.this) && postImage.getStatus() != AsyncTask.Status.RUNNING) {

            postImage.execute();

        } else {
            Toast.makeText(FragmentChat.this, "NO net available", Toast.LENGTH_LONG).show();
        }
    }

    public void loadAudio() {

        postAudio = new PostAudio();
        if (Utils.isNetworkAvailable(FragmentChat.this) && postImage.getStatus() != AsyncTask.Status.RUNNING) {

            postAudio.execute();

        } else {
            Toast.makeText(FragmentChat.this, "NO net available", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentChat.activityResumed();


    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(FragmentChat.this, DashbordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private File getDir1() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date1 = dateFormat.format(new Date());
        File sdDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(sdDir, "EraChat/" + date1);

    }

    private void encodeAudio(String selectedPath) {

        byte[] audioBytes;
        try {

            // Just to check file size.. Its is correct i-e; Not Zero
            File audioFile = new File(selectedPath);
            long fileSize = audioFile.length();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(new File(selectedPath));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
            audioBytes = baos.toByteArray();

            // Here goes the Base64 string
            AudioString = Base64.encodeToString(audioBytes, Base64.DEFAULT);
            Log.d("sdbhbuy",""+AudioString);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
   public void loadMoreItems(){
       if (chatroom.size() <= 10||GroupChat.size()<=10) {
          // chatroom.add(null);
          // itemListAdaptor.notifyItemInserted(chatroom.size() - 1);
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                 //  chatroom.remove(chatroom.size() - 1);
                 //  itemListAdaptor.notifyItemRemoved(chatroom.size());
                   //Generating more data
                   /*chatroom = databaseHandler.getCHatRoomMessageLoadMore(idvalue, myuser_id, myuser_id, idvalue);
                   Log.d("sjsugggssj",""+chatroom.size());

                   for(int i=10;i<chatroom.size();i=i+10){
                       end = i + 10;

                   }*/
                   end=end+10;
                   if(GroupId.equals("")) {
                       chatroom = databaseHandler.getCHatRoomMessagewithLimit(idvalue, myuser_id, myuser_id, idvalue,end);
                       itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, idvalue);
                       chatList.setAdapter(itemListAdaptor);
                       //  itemListAdaptor.notifyDataSetChanged();
                       nestedScrollView.clearFocus();
                   }else {
                       GroupChat=databaseHandler.getGroupChat1(GroupId,end);
                       Log.d("@@@@@@@",""+end+idvalue);
                       itemListAdaptor = new ItemListAdaptor(FragmentChat.this,GroupChat , idvalue);
                       chatList.setAdapter(itemListAdaptor);
                       nestedScrollView.clearFocus();
                   }


               }
           }, 400);
       } else {
           Toast.makeText(FragmentChat.this, "Loading data completed", Toast.LENGTH_SHORT).show();
       }
   }
      /* chatroom = databaseHandler.getCHatRoomMessage1(idvalue, myuser_id, myuser_id, idvalue,15);
       itemListAdaptor = new ItemListAdaptor(FragmentChat.this, chatroom, idvalue);
       chatList.setAdapter(itemListAdaptor);
       itemListAdaptor.notifyDataSetChanged();*/

    boolean isLastVisible() {
        LinearLayoutManager layoutManager = ((LinearLayoutManager)chatList.getLayoutManager());
        int pos = layoutManager.findLastCompletelyVisibleItemPosition();
        int numItems = chatList.getAdapter().getItemCount();
        return (pos >= numItems);
    }
}
