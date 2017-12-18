package com.example.lue.erachat.Activity.Fragment;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ContactListMeoBookAdaptor;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.SharedPrf.SharedPreference;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lue on 23-06-2017.
 */

public class FragmentContactListMeoBook extends Fragment {

ListView MeoList;
    MyAsync myAsync;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    Dialog dialog;
    Cursor cursor ;
    String name, phonenumber ;
    private String Content;
    ContactListMeoBookAdaptor contactListMeoBookAdaptor;
    ArrayList<User> StoreContacts=new ArrayList<User>() ;
    ArrayList<String>dataSend=new ArrayList<>();
    ArrayList<User>StoreContactMeo=new ArrayList<User>();
    ArrayList<User>compareList=new ArrayList<User>();
    String ContactNumber;
    String ContactName;
    String ContactEmail;
    String ContactRegId;
    String userContactId;
    public static int flag=0;
    private int position = 0;
    AVLoadingIndicatorView dialog1;
   static int flag1=0;
      HttpClient Client;



    DatabaseHandler databaseHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list_meobook, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHandler=new DatabaseHandler(getActivity());
        MeoList=(ListView)view.findViewById(R.id.MeoList);

        MeoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name=compareList.get(i).getContactName();
                String image=compareList.get(i).getContactImage();
                String number = compareList.get(i).getContactNumber();
               /* SharedPreferences pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("SenderNumber", number);
                editor.commit();*/
                Log.d("yuhusdhc",""+name);
                if(name.equals("null")){
                    name=compareList.get(i).getContactNumber();
                }
                Intent intent=new Intent(getActivity(),FragmentChat.class);
                intent.putExtra("UserId",compareList.get(i).getUserId());
                intent.putExtra("Username",name);
                intent.putExtra("SenderName",name);
                intent.putExtra("SenderImage",image);
                intent.putExtra("SenderMobile",number);
                intent.putExtra("UserImage",compareList.get(i).getContactImage());
                Log.d("USERId"," "+compareList.get(i).getUserId());
                startActivity(intent);
            }
        });
       // getContacts();
        //asynktask
        myAsync=new MyAsync();
        //dialog
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog1=(AVLoadingIndicatorView)view.findViewById(R.id.avi) ;
        loadData();


    }



    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    // pb.setVisibility(View.VISIBLE);
                    dialog1.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    //  pb.setVisibility(View.GONE);
                    dialog1.hide();
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
            Log.d("werftghy","iiiiiiiiiiii");
            String s="";
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");
            try {

                HttpClient Client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getContact");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection","Keep-Alive");

                JSONObject jsonObject=new JSONObject();
               jsonObject.accumulate("user_id",myuser_id);



                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = Client.execute(httpPost);
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
            Log.d("onPostExcuteMeoBook", "" + s+"cs");
              compareList.clear();
            StoreContactMeo.clear();
            StoreContacts.clear();
            try {

                JSONObject jsonObject=new JSONObject(s);
                JSONArray jsonArray  = jsonObject.getJSONArray("message");
                Log.d("sizee", "kk1111 " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.d("object", "kk1111 " + jsonArray.getJSONObject(i).toString());
                    JSONObject jobject = jsonArray.getJSONObject(i);
                    User itemEntity = new User();
                    itemEntity.setContactNumber(jobject.getString("mobile"));
                    itemEntity.setUserId(jobject.getString("user_id"));
                    itemEntity.setUsercontactid(jobject.getString("user_id"));
                    itemEntity.setContactName(jobject.getString("user_name"));
                    itemEntity.setContactImage(jobject.getString("photo"));
                    compareList.add(itemEntity);



                }
            } catch (Exception e) {
                e.printStackTrace();
            }if(compareList.size()>0){
                contactListMeoBookAdaptor=new ContactListMeoBookAdaptor(getActivity(),compareList);
                MeoList.setAdapter(contactListMeoBookAdaptor);
                /*MeoList.setAdapter(contactListMeoBookAdaptor);
                    if(flag==0) {
                        Log.d("***","******"+compareList.size());
                        for (int i=0;i<compareList.size();i++){

                            databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                        }
                        Log.d("***","%%%%%%%%"+compareList.size());
                        flag=1;
                        Log.d("logCheck","*******"+flag);
                    } else {


                        ArrayList<User> ContactList = databaseHandler.getContact();
                        Log.d("Sizeof", " " + compareList.size() + "  " + ContactList.size());
                        if (compareList.size() > ContactList.size()) {
                            Log.d("Sizeof", " " + compareList.size() + "  " + ContactList.size());
                            databaseHandler.getWritableDatabase();
                            for (int i = ContactList.size(); i < compareList.size(); i++) {

                                Log.d("cjnjnc", "&&&*****");
                                position = i;


                                Log.d("cjnjnc", "&&&*****" + " " + position);
                                databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(), "", "", compareList.get(i).getContactEmail(), "", compareList.get(i).getUsercontactid()));
                            }
                        }
                    }

*/


                    }
            /*if (StoreContactMeo.size()>0){
              //  GetContactsIntoArrayList();
                getContacts();
                Log.d("***","******");
                for(User user1: StoreContactMeo){
                    for(User user2: StoreContacts){
                        if(user1.isSimilarButNotEqual(user2)){
                            compareList.add(user1);
                            Log.d("cyuhnu",""+compareList.size());


                           // compareList.add(user2);
                        }
                    }
                }

                if(flag==0) {
                    Log.d("***","******"+compareList.size());
                    for (int i=0;i<compareList.size();i++){

                        databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                    }
                    Log.d("***","%%%%%%%%"+compareList.size());
                    flag=1;
                    Log.d("logCheck","*******"+flag);
                }
                else  {


                    ArrayList<User>ContactList =databaseHandler.getContact();
                    Log.d("Sizeof"," "+compareList.size()+"  "+ ContactList.size());
                    if(compareList.size() > ContactList.size()){
                        Log.d("Sizeof"," "+compareList.size()+"  "+ ContactList.size());
                        databaseHandler.getWritableDatabase();
                        for (int i = ContactList.size(); i < compareList.size(); i++) {
                            *//*if (!(compareList.get(i).getContactNumber().equals(ContactList.get(i).getContactName()))) {
                                //  String t =contact.get(i).getInTime();
                                Log.d("cjnjnc","&&&*****");
                                position = i;

                            }*//*
                            position=i;
                            Log.d("cjnjnc","&&&*****"+" "+position);
                            databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(),"","",compareList.get(i).getContactEmail(),"",compareList.get(i).getUsercontactid()));
                        }



                    }

                }*/




        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(getActivity()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            myAsync.execute();
        }else{
            Toast.makeText(getActivity(),"NO net avalable",Toast.LENGTH_LONG).show();

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
        dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }


    public void getContacts() {
        String phoneNumber = null;
        String email = null;
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
        Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
        String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
        String DATA = ContactsContract.CommonDataKinds.Email.DATA;
        StringBuffer output;
        ContentResolver contentResolver = getActivity().getContentResolver();
        cursor = contentResolver.query(CONTENT_URI, null,null, null, null);
        // Iterate every contact in the phone
        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                output = new StringBuffer();
                // Update the progress message

                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    output.append("\n First Name:" + name);
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }
                    phoneCursor.close();
                    String ssss =phoneNumber.trim().replaceAll("\\s+", " ");
                    String onlyDigit=getOnlyDigits(ssss);
                    ssss.trim();
                    dataSend.add(onlyDigit);
                    User user=new User();
                    user.setContactNumber(phoneNumber);
                    user.setContactName(name);

                    StoreContacts.add(user);

                }

            }


        }
    }
    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}
