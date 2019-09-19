package com.ratna.foosip;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import app.Config;

/**
 * Created by ratna on 10/18/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();


        try {
            String token = FirebaseInstanceId.getInstance().getToken();

            //for now we are displaying the token in the log
            //copy it as this method is called only when the new token is generated
            //and usually new token is only generated when the app is reinstalled or the data is cleared
            Log.d("MyRefreshedToken", token);


            pref = getSharedPreferences(Config.SHARED_PREF , Context.MODE_PRIVATE);
            edit = pref.edit();

            edit.putString("token" , token);
            edit.apply();

            /*Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
            registrationComplete.putExtra("token", token);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
*/
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        /*String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);*/
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
//        new SendGcmRegIdToServer(getApplicationContext(),token);
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}