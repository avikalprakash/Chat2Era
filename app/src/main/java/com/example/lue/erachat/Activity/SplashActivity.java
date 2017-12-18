package com.example.lue.erachat.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lue.erachat.Activity.updateLocation.MMPermission;
import com.example.lue.erachat.R;

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


public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static final String MyLoginPREFERENCES = "loginpreference" ;
    public static final String Login = "login";
    AlertDialog alertDialog;
    String latitude = "";
    String longitude = "";
    double Latitude;
    double Longitude;
    MMPermission permission;
    LocationManager mlocManager;
    android.support.v7.app.AlertDialog.Builder alert;
    ProgressDialog progressDialog;
    Context context;
    boolean isLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MultiDex.install(this);
        setContentView(R.layout.activity_splash_screen);
        call();
    }

  /*  @Override
    protected void onRestart() {
        super.onRestart();
        call();
    }*/

    private void call(){
        ImageView spaceshipImage = (ImageView) findViewById(R.id.splash_img);
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this, R.anim.myanimation);
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        new Handler().postDelayed(new Runnable(){
        @Override
        public void run() {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.d("userVerification"," "+android_id);
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            String code= sharedPreferences.getString("CheckRegister", "");

            if (code.equals("true")) {
                Intent intent=new Intent(SplashActivity.this,DashbordActivity.class);
                intent.putExtra("lat", latitude);
                intent.putExtra("long", longitude);
                startActivity(intent);
            } else {
                Intent intent=new Intent(SplashActivity.this,GetStarted.class);
                startActivity(intent);
            }

            finish();
        }
    }, 4000);
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
                if (location.getAccuracy()>0 && location.getAccuracy()<1000) {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    if(!isLocation)
                    {
                        latitude=String.valueOf(location.getLatitude());
                        longitude=String.valueOf(location.getLongitude());
                        Latitude=location.getLatitude();
                        Longitude=location.getLongitude();
                      //  Toast.makeText(getApplicationContext(), latitude+", "+longitude, Toast.LENGTH_LONG).show();

                    }
                    isLocation=true;
                } else {
                    if(!progressDialog.isShowing()) progressDialog.show();
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

}
