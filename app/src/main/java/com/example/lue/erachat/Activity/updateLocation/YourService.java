package com.example.lue.erachat.Activity.updateLocation;

import android.app.IntentService;
import android.content.Intent;

public class YourService extends IntentService {

    private static String tagName = "YourService";

    public YourService() {
        super("YourService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Start your location
      //  LocationUtil.startLocationListener();
        try {
        // Wait for 10 seconds
            Thread.sleep(1000*10);
        } catch (InterruptedException e) {
        }
        //Stop location listener
       // LocationUtil.stopLocationListener();
        // upload or save location
       // uploadGps();

        YourWakefulReceiver.completeWakefulIntent(intent);
    }

}