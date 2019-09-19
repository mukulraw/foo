package com.ratna.foosip;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by ratna on 2/7/2017.
 */
public class SplashScreen extends AppCompatActivity {

    String[] PERMISSIONS = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.CAMERA , Manifest.permission.RECEIVE_SMS , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.READ_PHONE_STATE};
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    SharedPreferences fcmPref;
    SharedPreferences.Editor fcmEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //checkPermission();

        /*fcmPref = getSharedPreferences(Config.SHARED_PREF, Context.MODE_PRIVATE);
        fcmEdit = fcmPref.edit();

        if (checkPermission()) {


            try {

                String tok = FirebaseInstanceId.getInstance().getToken();

                Log.d("token", tok);

                fcmEdit.putString("token", tok);

                fcmEdit.apply();

                displayFirebaseRegId();

            } catch (Exception e) {

                new Thread() {
                    @Override
                    public void run() {
                        //If there are stories, add them to the table
                        //try {
                        // code runs in a thread
                        //runOnUiThread(new Runnable() {
                        //  @Override
                        //public void run() {
                        new MyFirebaseInstanceIDService().onTokenRefresh();

                        displayFirebaseRegId();


                        //}
                        //});
                        //} catch (final Exception ignored) {
                        //}
                    }
                }.start();

                e.printStackTrace();
            }

        }


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                Log.d("intent", "1");

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    //FirebaseMessaging.getInstance().subscribeToTopic(app.Config.TOPIC_GLOBAL);

                    displayFirebaseRegId();
                    Log.d("intent", "2");


                }


            }
        };

        //displayFirebaseRegId();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
*/


        if(hasPermissions(this , PERMISSIONS))
        {
            startApp();
        }
        else
        {
            ActivityCompat.requestPermissions(this , PERMISSIONS , REQUEST_CODE_ASK_PERMISSIONS);
        }


    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        //SharedPreferences pref = getApplicationContext().getSharedPreferences(app.Config.SHARED_PREF, 0);
        //String regId = fcmPref.getString("token", null);

        //Log.e(TAG, "Firebase reg id: " + regId);

        //if (!TextUtils.isEmpty(regId)){

        Intent intent = new Intent(SplashScreen.this, SignIn.class);
        startActivity(intent);
        finish();
//            Toast.makeText(this,regId.toString(),Toast.LENGTH_SHORT).show();
        /*}
        else{
            Toast.makeText(this,"reg id not recieved yet!!",Toast.LENGTH_SHORT).show();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver


        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    public void startApp()
    {
        Intent intent = new Intent(SplashScreen.this, SignIn.class);
        startActivity(intent);
        finish();
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS)
        {
            if (
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getApplicationContext() , Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED

                    )
            {

                startApp();

            }
            else
            {
                if (
                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.ACCESS_COARSE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.ACCESS_FINE_LOCATION) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.CAMERA) ||
//                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.RECEIVE_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.READ_PHONE_STATE)
                        ) {

                    Toast.makeText(getApplicationContext() , "Permissions are required for this app" , Toast.LENGTH_SHORT).show();
                    finish();

                }
                //permission is denied (and never ask again is  checked)
                //shouldShowRequestPermissionRationale will return false
                else {
                    Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                            .show();
                    finish();
                    //                            //proceed with logic by disabling the related features or quit the app.
                }
            }

        }

    }
}

