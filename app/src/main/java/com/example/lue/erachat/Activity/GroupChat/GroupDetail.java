package com.example.lue.erachat.Activity.GroupChat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.EditGroupName;
import com.example.lue.erachat.Activity.Adaptor.AdaptorGroupDetail;
import com.example.lue.erachat.Activity.Adaptor.GroupList_Adaptor;
import com.example.lue.erachat.Activity.CreateTeam.SearchTeamParticipant;
import com.example.lue.erachat.Activity.DashbordActivity;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.R;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetail extends AppCompatActivity {
    AdaptorGroupDetail adaptorGroupDetail;
    ListView memberList;
    CircleImageView GroupImage;
    TextView GroupName,NoOFMember,Meaasge,admin;
    LinearLayout lblinvite,laveGroup;
    String myBase64Image="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    MyAsync1 myAsync1;
    ChangeGroupIcon changeGroupIcon;
    AVLoadingIndicatorView dialog;
    LinearLayout lavegroup;
    String GroupId="";
    String Group_Name="";
    String Group_Photo="";
    String GroupAdmin="";
    ImageView editName;
    TextView message;
    String groupid="";
    PopupWindow popupWindow;
    private int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    ImageView profile_takepic, profile_choosepic;
    Uri imageuri;
    ArrayList<Messages>GroupMemberDetail=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_group_list);


       // Intent
        final Intent intent=getIntent();
        GroupId=intent.getStringExtra("GroupId");
        Log.d("adhbhb"," "+GroupId);

        //Asynk Task
        myAsync=new MyAsync();
        myAsync1=new MyAsync1();

        //Initialization
        lblinvite=(LinearLayout)findViewById(R.id.lblinvite);
        lavegroup=(LinearLayout)findViewById(R.id.laveGroup);
        message=(TextView)findViewById(R.id.textView67);
        dialog=(AVLoadingIndicatorView)findViewById(R.id.avi) ;
        memberList=(ListView)findViewById(R.id.memberList);
        GroupImage=(CircleImageView)findViewById(R.id.imageView49);
        GroupName=(TextView)findViewById(R.id.textView65);
        admin=(TextView)findViewById(R.id.textView68);
        memberList=(ListView)findViewById(R.id.memberList);
        NoOFMember=(TextView)findViewById(R.id.textView70) ;
        lavegroup=(LinearLayout)findViewById(R.id.laveGroup);
        editName=(ImageView)findViewById(R.id.imageView47);
        lavegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData1();
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Working On it",Toast.LENGTH_LONG).show();
                 Intent intent1=new Intent(GroupDetail.this, EditGroupName.class);
                Log.d("groupid",""+groupid);
                intent1.putExtra("GroupId",groupid);
                startActivity(intent1);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2=new Intent(GroupDetail.this, FragmentChat.class);
                intent2.putExtra("GroupIdDetail",GroupId);
                intent2.putExtra("groupName",Group_Name);
                intent2.putExtra("groupPhoto",Group_Photo);
                startActivity(intent2);
            }
        });

       lblinvite.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent1=new Intent(getApplicationContext(), SearchTeamParticipant.class);
               intent1.putExtra("GROUP_ID",GroupId);
               startActivity(intent1);
               Toast.makeText(getApplicationContext(),"Working On it",Toast.LENGTH_LONG).show();
           }
       });
        GroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  onShowPopup(v);
            }
        });
       loadData();

    }



    //Handler
    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    dialog.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    dialog.hide();

                    break;
            }
            return false;
        }
    });




    class MyAsync extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getGroupDetails");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();

                jsonObject.accumulate("group_id",GroupId);
                Log.d("jcenjn"," "+GroupId);

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            JSONObject jsonObject1 = null;
            try {
                    jsonObject1 = new JSONObject(s);
                    String message = jsonObject1.getString("message");
                    JSONObject object = new JSONObject(message);
                  groupid = object.getString("group_id");
                  Group_Name = object.getString("group_name");
                  GroupAdmin=object.getString("group_admin");
                  Group_Photo=object.getString("photo");
                    String member = object.getString("member");
                    JSONArray jsonArray = new JSONArray(member);
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Messages messages = new Messages();
                        messages.setGroupMemberId(jsonObject.getString("user_id"));
                        messages.setGroupMemberName(jsonObject.getString("user_name"));
                        messages.setGroupMemberNumber(jsonObject.getString("mobile"));
                        messages.setGroupMemberPhoto(jsonObject.getString("photo"));
                        messages.setGroupMemberStatus(jsonObject.getString("status"));
                        GroupMemberDetail.add(messages);
                        GroupName.setText(Group_Name);
                        admin.setText(GroupAdmin);
                        Picasso.with(getApplicationContext()).load(Group_Photo).into(GroupImage);
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }if(GroupMemberDetail.size()>0){
                String size= String.valueOf(GroupMemberDetail.size());
                if(!Group_Name.equals("")) {

                }
                NoOFMember.setText(size+" Members");
                adaptorGroupDetail=new AdaptorGroupDetail(GroupDetail.this,GroupMemberDetail);
                memberList.setAdapter(adaptorGroupDetail);
            }



        }
    }
    public void  loadData(){
        myAsync=new MyAsync();

        if (Utils.isNetworkAvailable(getApplicationContext()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(getApplicationContext(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }

    class MyAsync1 extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/exitFromGroup");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();

                jsonObject.accumulate("group_id",groupid);
                jsonObject.accumulate("member_id",myuser_id);
                Log.d("jcenjn"," "+GroupId);

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcute", "" + s);
            try {
                JSONObject jsonObject1 = new JSONObject(s);
                String message=jsonObject1.getString("message");
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(), DashbordActivity.class);
                intent.putExtra("ExitGroup","true");
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }
    class ChangeGroupIcon extends AsyncTask<String,Void,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            handler.sendEmptyMessage(SHOW_PROCESS_DIALOG);
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/updateGroupPhoto");
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();

                jsonObject.accumulate("group_id",groupid);
                jsonObject.accumulate("photo",myBase64Image);
                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return s;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
            Log.d("onPostExcute", "" + s);
            try {
                JSONObject jsonObject1 = new JSONObject(s);
                String message=jsonObject1.getString("message");
                Toast.makeText(getApplicationContext(),"GroupIcon updated sucessfully",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getApplicationContext(), GroupDetail.class);
                intent.putExtra("GroupId",GroupId);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }





        }
    }
    public void  loadData1(){
        myAsync1=new MyAsync1();

        if (Utils.isNetworkAvailable(getApplicationContext()) && myAsync1.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync1.execute();
        }else{
            Toast.makeText(getApplicationContext(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }
    public void  UpdateGroupPhoto(){
        changeGroupIcon=new ChangeGroupIcon();

        if (Utils.isNetworkAvailable(getApplicationContext()) && changeGroupIcon.getStatus()!= AsyncTask.Status.RUNNING)
        {
            changeGroupIcon.execute();
        }else{
            Toast.makeText(getApplicationContext(),"NO net avalable",Toast.LENGTH_LONG).show();
        }
    }


    private String readResponse(HttpResponse httpResponse) {
        InputStream is=null;
        String return_text="";
        try {
            is=httpResponse.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
            Log.d("return_text",""+return_text);
        } catch (Exception e)
        {

        }
        return return_text;


    }
    @Override
    public void onPause() {
        super.onPause();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }if ( myAsync1.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync1.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }if ( myAsync1.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync1.cancel(true);
        }
    }
    public void onShowPopup(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // inflate the custom popup layout
        final View inflatedView = layoutInflater.inflate(R.layout.dialogchoose_profile, null, false);
        // find the ListView in the popup layout
        profile_takepic = (ImageView) inflatedView.findViewById(R.id.imageView40);
        profile_choosepic = (ImageView) inflatedView.findViewById(R.id.imageView42);
        // get device size
        Display display = getWindowManager().getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);
        profile_choosepic.setOnClickListener(new View.OnClickListener() {
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
        profile_takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "New Picture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageuri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            }
        });
        //mDeviceHeight = size.y;
        // fill the data to the list items
        // set height depends on the device size
        popupWindow = new PopupWindow(inflatedView, size.x - 0, size.y / 6, true);

        // set a background drawable with rounders corners
        popupWindow.setBackgroundDrawable(this.getResources().getDrawable(R.color.grey));
        // make it focusable to show the keyboard to enter in `EditText`
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // make it outside touchable to dismiss the popup window
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Log.d("Imageview", String.valueOf(bitmap));
                GroupImage.setImageBitmap(bitmap);
                myBase64Image = encodeToBase641(bitmap, Bitmap.CompressFormat.JPEG,40);
                UpdateGroupPhoto();
                if(popupWindow!=null){
                    popupWindow.dismiss();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            GroupImage.setImageBitmap(imageBitmap);
            try {
                Bitmap imagefromActivity = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                myBase64Image = encodeToBase64(imagefromActivity, Bitmap.CompressFormat.JPEG,40);
            } catch (IOException e) {
                e.printStackTrace();
            }

            UpdateGroupPhoto();
        }

    }
    public static String encodeToBase641(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        Matrix matrix = new Matrix();

        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image,image.getWidth(),image.getHeight(),true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        rotatedBitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
