package com.example.lue.erachat.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Fragment.BlockedUsers;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Fragment.Fragment_Invitation;
import com.example.lue.erachat.Activity.GroupChat.ChatList;
import com.example.lue.erachat.Activity.GroupChat.FragmentGroup_List;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.More.MoreFragment;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.updateLocation.MMPermission;
import com.example.lue.erachat.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.sample.calling.LoginActivity;
import com.sinch.android.rtc.sample.calling.SinchService;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.lue.erachat.Activity.Fragment.FragmentChat.MyPref;


public class DashbordActivity extends com.sinch.android.rtc.sample.calling.BaseActivity implements SinchService.StartFailedListener {
    private DashbordActivity bind;
    ArrayList<Messages>messageList=new ArrayList<>();
    ArrayList<String>UnreadMeaage=new ArrayList<>();
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    String status="";
    String latitude = "";
    String longitude = "";
    double Latitude;
    double Longitude;
    MMPermission permission;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    ProgressDialog progressDialog;
    Context context;
    boolean isLocation;
    String user_id;
    String userName;
    //private Boolean firstTime = null;
    private static boolean RUN_ONCE = true;
    public static final String MY_NUMBER = "myNumber";
    private static final String LOCATION_SEND_URL = "http://erachat.condoassist2u.com/api/setLocation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbord);

        SharedPreferences sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        userName=sharedpreferences.getString(MY_NUMBER,"");
        runOnce();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                loginClicked();
            }
        }, 2000);
        context=this;
        alert = new android.support.v7.app.AlertDialog.Builder(this);
      //  progressDialog = new ProgressDialog(this);
      //  progressDialog.setMessage("Please wait for location");
        permission = new MMPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission.result == -1 || permission.result == 0) {
            try {
                registerForGPS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (permission.result == 1) {
            registerForGPS();
        }
        user_id = PreferenceManager.getDefaultSharedPreferences(this).getString("myuserid", "");
        context=this;
        alert = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait for location");
        permission = new MMPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
       /* if (permission.result == -1 || permission.result == 0) {
            try {
                registerForGPS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (permission.result == 1) {
            registerForGPS();
        }*/

        myAsync = new MyAsync();
        status="online";
        //Intent
        Intent intent=getIntent();
       // Log.d("ffjn",""+intent.getStringExtra("Key")) ;
        if(intent.getStringArrayListExtra("UnreadMessage")!=null){
            UnreadMeaage=intent.getStringArrayListExtra("UnreadMessage");
        }if(intent.getStringExtra("Invitation")!=null){
            Log.d("cdndj","dnd");
            if(intent.getStringExtra("Invitation").equals("true")) {
                Fragment_Invitation fragment_invitation = new Fragment_Invitation();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.containerdashbord, fragment_invitation);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
            }
        }else if(intent.getStringExtra("EditProfile")!=null){
            FragmentProfile fragmentProfile=new FragmentProfile();
            FragmentTransaction fragmentTransaction3=getSupportFragmentManager().beginTransaction();
            fragmentTransaction3.replace(R.id.containerdashbord,fragmentProfile);
            fragmentTransaction3.addToBackStack(null);
            fragmentTransaction3.commit();
        }else if(intent.getStringExtra("ExitGroup")!=null){
            FragmentGroup_List groupList =new FragmentGroup_List();
            FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
            fragmentTransaction1.replace(R.id.containerdashbord,groupList);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();
        }else if(intent.getStringExtra("Key")!=null){
            BlockedUsers blockedUsers =new BlockedUsers();
            FragmentTransaction fragmentTransaction3=getSupportFragmentManager().beginTransaction();
            fragmentTransaction3.replace(R.id.containerdashbord,blockedUsers);
            fragmentTransaction3.addToBackStack(null);
            fragmentTransaction3.commit();
        }
        else {
            ChatList fragmentChat=new ChatList();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.containerdashbord,fragmentChat);
            Bundle bundle=new Bundle();
            bundle.putStringArrayList("UnreadMeaage",UnreadMeaage);
            fragmentChat.setArguments(bundle);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        //actionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Era Chat");

        loadData();

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);

        bnve.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_favorites:
                                ChatList fragmentChat=new ChatList();
                                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.containerdashbord,fragmentChat);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                            case R.id.action_music:
                                FragmentContactInfo fragmentChat1=new FragmentContactInfo();
                                FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction1.replace(R.id.containerdashbord,fragmentChat1);
                                fragmentTransaction1.addToBackStack(null);
                                fragmentTransaction1.commit();
                                break;
                            case R.id.action_schedules:
                                MoreFragment moreFragment=new MoreFragment();
                                FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction2.replace(R.id.containerdashbord,moreFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break; case R.id.action_profile:
                                FragmentProfile fragmentProfile=new FragmentProfile();
                                FragmentTransaction fragmentTransaction3=getSupportFragmentManager().beginTransaction();
                                fragmentTransaction3.replace(R.id.containerdashbord,fragmentProfile);
                                fragmentTransaction3.addToBackStack(null);
                                fragmentTransaction3.commit();
                                break;

                        }
                        return true;
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(ChatList.class!=null) {
            inflater.inflate(R.menu.menu_dashbord, menu);
            return true;
        }else {
            return false;
        }

    }

    private void runOnce() {
        if (RUN_ONCE) {
            RUN_ONCE = false;
           // Toast.makeText(getApplicationContext(), "First", Toast.LENGTH_LONG).show();
            // do something
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_groupList:
                FragmentGroup_List groupList =new FragmentGroup_List();
                FragmentTransaction fragmentTransaction1=getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.containerdashbord,groupList);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                return true;
            case R.id.invitation:
                Fragment_Invitation fragment_invitation =new Fragment_Invitation();
                FragmentTransaction fragmentTransaction2=getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.containerdashbord,fragment_invitation);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                return true;
            case R.id.blocked_users:
                BlockedUsers blockedUsers =new BlockedUsers();
                FragmentTransaction fragmentTransaction3=getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.containerdashbord,blockedUsers);
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.commit();
                return true;
            /*case R.id.login:
                Intent intent = new Intent(DashbordActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;*/
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
    public void onStartFailed(SinchError error) {

    }

    @Override
    public void onStarted() {

    }

    class MyAsync extends AsyncTask<String, Void, String> {

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
                jsonObject.accumulate("current_status",status);
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

    public void loadData() {
        myAsync = new MyAsync();

        if (Utils.isNetworkAvailable(DashbordActivity.this) && myAsync.getStatus() != AsyncTask.Status.RUNNING) {

                myAsync.execute();

        } else {
            Toast.makeText(DashbordActivity.this, "NO net avalable", Toast.LENGTH_LONG).show();
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

        if (myAsync.getStatus() == AsyncTask.Status.RUNNING) {
            myAsync.cancel(true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        status="offline";
        loadData();
        if (myAsync.getStatus() == AsyncTask.Status.RUNNING) {
            myAsync.cancel(true);
        }
    }

    @Override
    public void onBackPressed() {
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        int seletedItemId = bnve.getSelectedItemId();
        if (R.id.action_favorites == seletedItemId) {
           status="offline";
            loadData();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    finish();
                }
            }, 2500);
        } else {
            setHomeItem(DashbordActivity.this);
        }
    }

    public static void setHomeItem(Activity activity) {
        BottomNavigationViewEx bnve = (BottomNavigationViewEx)activity. findViewById(R.id.bottom_navigation);
        bnve.setSelectedItemId(R.id.action_favorites);
    }


    private void registerForGPS() {
      /*  Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);*/


        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            alert.setTitle("GPS");
            alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..");
            alert.setPositiveButton("Turn on GPS",
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                return;
                            }
                            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                    (float) 0.01, (android.location.LocationListener) listener);
                            setCriteria();

                            mlocManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 0, (float)
                                            0.01, (android.location.LocationListener) listener);

                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(I);

                        }
                    });

            alert.show();
        }

        else {

            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    (float) 0.01, listener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    (float) 0.01, listener);
            if(!progressDialog.isShowing())progressDialog.show();
        }
    }

    public String setCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = mlocManager.getBestProvider(criteria, true);
        return provider;
    }



    private final LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {

            if (location.getLatitude() > 0.0) {
                if (location.getAccuracy()>0 && location.getAccuracy()<500) {
                   // if(progressDialog.isShowing()) progressDialog.dismiss();
                    if(!isLocation)
                    {
                        latitude=String.valueOf(location.getLatitude());
                        longitude=String.valueOf(location.getLongitude());
                        Latitude=location.getLatitude();
                        Longitude=location.getLongitude();
                       //  Toast.makeText(getApplicationContext(), latitude+", "+longitude, Toast.LENGTH_LONG).show();
                        new LocationSend().execute();
                    }
                    isLocation=true;
                } else {
                   // if(!progressDialog.isShowing()) progressDialog.show();
                }
            }
        }
        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    class LocationSend extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* pDialog = new ProgressDialog(DashbordActivity.this);
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
                HttpPost httpPost = new HttpPost(LOCATION_SEND_URL);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", user_id);
                jsonObject.accumulate("location", latitude+","+longitude);


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
           // pDialog.dismiss();
            try {
                JSONObject object = new JSONObject(json);
                boolean check  = object.getBoolean("error");
                if(check) {
                    String message = object.getString("message");
                    /*android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DashbordActivity.this);
                    builder.setMessage(message)
                            .setNegativeButton("ok", null)
                            .create()
                            .show();*/
                }else{

                    String s= object.getString("message");

                  //  Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
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


    private void loginClicked() {
       // String userName = "8804286695";

        if (userName.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
            return;
        }

        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(userName);
           // showSpinner();
        } else {
            //  openPlaceCallActivity();
        }
    }

}


