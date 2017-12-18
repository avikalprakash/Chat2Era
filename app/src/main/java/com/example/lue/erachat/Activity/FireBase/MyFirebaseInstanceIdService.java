
package com.example.lue.erachat.Activity.FireBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lue.erachat.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by lue on 16-06-2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    public static String refreshedToken;
    private static final String TAG = "MyFirebaseIIDService";
    private static final String FRIENDLY_ENGAGE_TOPIC = "friendly_engage";

    /**
     * The Application's current Instance ID token is no longer valid and thus a new one must be requested.
     */
    public void onTokenRefresh() {

        refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString(getString(R.string.FCM_TOKEN), refreshedToken);
        // editor.putString("user", MainActivity.user);
        editor.commit();
    }

}


