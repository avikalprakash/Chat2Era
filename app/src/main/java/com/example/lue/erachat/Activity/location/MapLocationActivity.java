package com.example.lue.erachat.Activity.location;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.Activity.ImageViewActivity;
import com.example.lue.erachat.Activity.Activity.LocationAddress;
import com.example.lue.erachat.Activity.Fragment.FragmentChat;
import com.example.lue.erachat.Activity.Models.Messages;
import com.example.lue.erachat.Activity.Other.Utils;
import com.example.lue.erachat.Activity.database.DatabaseHandler;
import com.example.lue.erachat.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MapLocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener{
    private GoogleApiClient googleApiClient;

    GoogleApiClient mGoogleApiClient;
    LocationAddress locationAddress;

    GoogleMap mGoogleMap;
    SupportMapFragment mFragment;
    Marker currLocationMarker;
    TextView loctext;
    double lat = 0.0;
    double longitude = 0.0;
    DatabaseHandler databaseHandler;
    String myuser_id="";
    String Senderid="";
    String GroupId="";
    LinearLayout locdtail;
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    MyAsync myAsync;
    AVLoadingIndicatorView dialog;
    String latitude="";
    String longitud="";
    Button send;
    String SendernameTitleBar;
    String SenderImageTitleBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        loctext=(TextView)findViewById(R.id.loctext);
        send=(Button)findViewById(R.id.buttonsend);
        dialog=(AVLoadingIndicatorView)findViewById(R.id.avi) ;
        databaseHandler=new DatabaseHandler(getApplicationContext());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        myuser_id = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("myuserid", "");

        //Initializing googleapi client
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        loctext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //Intent
        Intent intent = getIntent();
        Senderid=intent.getStringExtra("id");
        GroupId=intent.getStringExtra("GroupId");
        if(intent.getStringExtra("SenderName")!=null){
            SendernameTitleBar=intent.getStringExtra("SenderName");
        }
        if(intent.getStringExtra("SenderName")!=null){
            SenderImageTitleBar=intent.getStringExtra("SenderName");
        }
        locdtail=(LinearLayout)findViewById(R.id.locdetail);
        locdtail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude= String.valueOf(lat);
                longitud= String.valueOf(longitude);
                DateFormat df = new SimpleDateFormat(" HH:mm");
                String date = df.format(Calendar.getInstance().getTime());
                if(!GroupId.equals("")){
                    databaseHandler.insertGroupChat(new Messages(GroupId,"",myuser_id,"",date,"location","","","","",latitude,longitud,"","",""));

                }else {
                    databaseHandler.insertChatRoom(new Messages("", myuser_id, Senderid, "", "", "", "", "", "", "", date, "location", "",latitude,longitud,"",""));
                }


                loadData();

            }
        });

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void getCurrentLocation() {
        mGoogleMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            lat = location.getLatitude();
            locationAddress.getAddressFromLocation(lat, longitude,
                    getApplicationContext(), new MapLocationActivity.GeocoderHandler());
            //moving the map to location
            moveMap();
        }
    }
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = lat + ", "+longitude;

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(lat, longitude);

        //Adding marker to map
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //Displaying current coordinates in toast
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng latLng = new LatLng(lat, longitude);
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.setOnMarkerDragListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        lat = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            loctext.setText(locationAddress);

        }
    }

    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch(message.what){
                case SHOW_PROCESS_DIALOG :
                    // ItemListAdaptor.confmtick.setVisibility(View.GONE);
                    dialog.show();
                    break;
                case HIDE_PROCESS_DIALOG :
                    // ItemListAdaptor.confmtick.setVisibility(View.VISIBLE);
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
            String datavalue=latitude+","+longitud;
            String s="";
            String Groupw="http://erachat.condoassist2u.com/api/sendGroupMessage";
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost;
                if(!GroupId.equals("")){
                    httpPost= new HttpPost(Groupw);
                    Log.d("hbdhbh","Group");
                }else {
                    Log.d("hbdhbh","Onetoone");
                     httpPost = new HttpPost("http://erachat.condoassist2u.com/api/oneToOneLocation");
                }
                httpPost.setHeader("Content-type", "application/json");

                JSONObject jsonObject= new JSONObject();
                if(!GroupId.equals("")) {
                    Log.d("cjjcsn",""+GroupId+"@"+myuser_id);
                    jsonObject.accumulate("group_id", GroupId);

                    jsonObject.accumulate("sender_id", myuser_id);
                    jsonObject.accumulate("message",datavalue );
                    jsonObject.accumulate("type", "location");


                }else {
                    Log.d("cjjcn",""+Senderid+"@"+myuser_id);
                    jsonObject.accumulate("user_id", Senderid);

                    jsonObject.accumulate("sender_id", myuser_id);
                    jsonObject.accumulate("message", datavalue);
                }

                StringEntity stringEntity= new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag11"," "+s);
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            return s;

        }

            @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            handler.sendEmptyMessage(HIDE_PROCESS_DIALOG);
                if(!GroupId.equals("")){
                    Intent intent = new Intent(MapLocationActivity.this, FragmentChat.class);
                    intent.putExtra("GroupId",GroupId);
                    intent.putExtra("SenderName", SendernameTitleBar);
                    intent.putExtra("SenderImage", SenderImageTitleBar);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MapLocationActivity.this, FragmentChat.class);
                    intent.putExtra("SenderId", Senderid);
                    intent.putExtra("SenderName", SendernameTitleBar);
                    intent.putExtra("SenderImage", SenderImageTitleBar);
                    startActivity(intent);
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

        myAsync=new MyAsync();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        myAsync=new MyAsync();
        if ( myAsync.getStatus()== AsyncTask.Status.RUNNING)
        {
            myAsync.cancel(true);
        }
    }
}
