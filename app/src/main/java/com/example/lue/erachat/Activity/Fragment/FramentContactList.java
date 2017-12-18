package com.example.lue.erachat.Activity.Fragment;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Adaptor.ContactListAdaptor;
import com.example.lue.erachat.Activity.Adaptor.ContactListMeoBookAdaptor;
import com.example.lue.erachat.Activity.AddToContact;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Models.User;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.RadarSearch;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lue on 21-06-2017.
 */

public class FramentContactList extends Fragment {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS =1 ;
    private static final int PERMISSION_REQUEST_CONTACT =1 ;
    ListView contactList;
    ArrayList<User> StoreContacts=new ArrayList<User>() ;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    ArrayList<User>StoreContactMeo=new ArrayList<User>();
    ArrayList<User>compareList=new ArrayList<User>();
    ContactListAdaptor contactListAdaptor ;
    DatabaseHandler databaseHandler;
    ArrayList<String>dataSend=new ArrayList<>();
    ArrayList<User> user=new ArrayList<>();
    MyAsync myAsync;
    String name, phonenumber ;
    private ListView mListView;
    private ProgressDialog pDialog;
    private Handler updateBarHandler;
    AVLoadingIndicatorView dialog1;
    Cursor cursor;
    private int position = 0;
    int counter;
    public static int flag=0;
    ImageView search;
    EditText searchText;
    public  static final int RequestPermissionCode  = 1 ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_list, container, false);


    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHandler=new DatabaseHandler(getActivity());
        getContacts();
        myAsync=new MyAsync();
        dialog1=(AVLoadingIndicatorView)view.findViewById(R.id.avi) ;
       // askForContactPermission();
        contactList=(ListView)view.findViewById(R.id.contactList);
        searchText=(EditText)view.findViewById(R.id.searchText) ;
        search=(ImageView)view.findViewById(R.id.Search);

        updateBarHandler =new Handler();

        user=databaseHandler.getContact();
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                contactListAdaptor.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        loadData();
        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                /*String ida="";
                String num="";
                for(int a=0;a<user.size();a++){
                  //  Log.d("value###",""+StoreContacts.get(position).getContactNumber()+user.get(a).getContactNumber());
                    if(StoreContacts.get(position).getContactNumber().equals(user.get(a).getContactNumber())){
                        Log.d("value###",""+StoreContacts.get(position).getContactNumber()+user.get(a).getContactNumber());
                        ContactListAdaptor.add.setVisibility(View.VISIBLE);
                         ida=user.get(a).getContactId();
                        num=user.get(a).getContactNumber();
                        Intent intent=new Intent(getActivity(),AddToContact.class);
                        intent.putExtra("name",num);
                        intent.putExtra("id",ida);
                        startActivity(intent);
                    }
                }*/
            }
        });

    }

    public void getContacts() {
        StoreContacts.clear();
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
                    Log.d("outcjjsuicb"," "+name);
                    //This is to read multiple phone numbers associated with the same contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Phone number:" + phoneNumber);
                    }
                    phoneCursor.close();
                    String ssss =phoneNumber.trim().replaceAll("\\s+", " ");
                    String onlyDigit=getOnlyDigits(ssss);
                    Log.d("gyguhiuj",""+onlyDigit);
                    ssss.trim();
                    dataSend.add(onlyDigit);
                    User user=new User();
                    user.setContactNumber(onlyDigit);
                    user.setContactName(name);
                    StoreContacts.add(user);

                }

            }


        }
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
            String myuser_id = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("myuserid", "");

            String s = "";
            try {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 100000);
                HttpConnectionParams.setSoTimeout(httpParameters, 100000);
                HttpClient Client = new DefaultHttpClient(httpParameters);

                HttpPost httpPost = new HttpPost("http://erachat.condoassist2u.com/api/getRegisteredContact");
                httpPost.setHeader("Content-type", "application/json");
                httpPost.setHeader("Connection", "Keep-Alive");

                JSONObject jsonObject = new JSONObject();
                StringBuilder stringBuilder = new StringBuilder();

                for (int z = 0; z < dataSend.size(); z++) {
                    Log.d("arraydata", "" + dataSend.get(z));
                    if (z == (dataSend.size() - 1)) {
                        stringBuilder.append(dataSend.get(z));
                    } else {
                        stringBuilder.append(dataSend.get(z) + ",");
                    }
                }
                Log.d("cyhduijed", "" + stringBuilder.toString());
                String valcnn=stringBuilder.toString();
                valcnn.replaceAll(",,",",");
                jsonObject.accumulate("user_id",myuser_id);
                jsonObject.accumulate("contact", stringBuilder.toString());

                Log.d("valueinarray", jsonObject.toString());


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);

                HttpResponse httpResponse = Client.execute(httpPost);
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
            compareList.clear();
            StoreContactMeo.clear();
            contactListAdaptor=new ContactListAdaptor(getActivity(),StoreContacts);
            contactList.setAdapter(contactListAdaptor);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("message");
                Log.d("sizee", "kk1111 " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.d("object", "kk1111 " + jsonArray.getJSONObject(i).toString());
                    JSONObject jobject = jsonArray.getJSONObject(i);
                    User itemEntity = new User();
                    itemEntity.setContactNumber(jobject.getString("mobile"));
                    itemEntity.setUserId(jobject.getString("user_id"));
                    itemEntity.setUsercontactid(jobject.getString("user_id"));
                    itemEntity.setContactName(jobject.getString("user_name"));
                    itemEntity.setContactStatus(jobject.getString("in_contact"));
                    compareList.add(itemEntity);


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (compareList.size() > 0) {

                if (flag == 0) {

                    for (int i = 0; i < compareList.size(); i++) {
                        String contactNumber=getOnlyDigits(compareList.get(i).getContactNumber());

                        databaseHandler.inserRecord(new User(contactNumber, "", compareList.get(i).getContactStatus(), compareList.get(i).getContactEmail(), "", compareList.get(i).getUsercontactid()));
                    }
                    Log.d("***", "%%%%%%%%" + compareList.size());
                    flag = 1;
                    Log.d("logCheck", "*******" + flag);
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
                            databaseHandler.inserRecord(new User(compareList.get(i).getContactNumber(), "", compareList.get(i).getContactStatus(), compareList.get(i).getContactEmail(), "", compareList.get(i).getUsercontactid()));
                        }
                    }
                }


            }
        }
    }
    public void  loadData(){
        myAsync=new MyAsync();
        if (Utils.isNetworkAvailable(getActivity()) && myAsync.getStatus()!= AsyncTask.Status.RUNNING)
        {
            String serverURL = "http://erachat.condoassist2u.com/api/users";

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

        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }



    public static String getOnlyDigits(String s) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(s);
        String number = matcher.replaceAll("");
        return number;
    }
}
