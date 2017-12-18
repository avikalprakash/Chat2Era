package com.example.lue.erachat.Activity.updateLocation;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class YourWakefulReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            Intent service = new Intent(context, YourService.class);
            startWakefulService(context, service);
        }
    }
