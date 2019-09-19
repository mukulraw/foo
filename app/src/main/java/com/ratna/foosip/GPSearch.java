//package com.ratna.foosip;
//
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import com.google.android.gms.location.LocationListener;
//
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.Manifest;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.text.DateFormat;
//import java.util.Date;
//
//import SharedPreferences.SavedParameter;
//
///**
// * Created by ratna on 1/3/2017.
// */
//public class GPSearch extends Activity implements
//        LocationListener,
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
//
//    private static final String TAG = "LocationActivity";
//    private static final long INTERVAL = 1000 * 10;
//    private static final long FASTEST_INTERVAL = 1000 * 5;
//    LocationRequest mLocationRequest;
//    GoogleApiClient mGoogleApiClient;
//    Location mCurrentLocation;
//    String mLastUpdateTime;
//
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_look_table);
////        Log.d(TAG, "onCreate ...............................");
////        //show error dialog if GoolglePlayServices not available
////        if (!isGooglePlayServicesAvailable()) {
////            finish();
////        }
//        createLocationRequest();
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
//
//
////        tvLocation = (TextView) findViewById(R.id.tvLocation);
////
////        btnFusedLocation = (Button) findViewById(R.id.btnShowLocation);
////        btnFusedLocation.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View arg0) {
////                updateUI();
////            }
////        });
//
//        updateUI();
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        Log.d(TAG, "onStart fired ..............");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        Log.d(TAG, "onStop fired ..............");
//        mGoogleApiClient.disconnect();
//        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
//    }
//
//    private boolean isGooglePlayServicesAvailable() {
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        if (ConnectionResult.SUCCESS == status) {
//            return true;
//        } else {
//            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
//            return false;
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
//        startLocationUpdates();
//    }
//
//    protected void startLocationUpdates() {
//        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        Log.d(TAG, "Location update started ..............: ");
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.d(TAG, "Connection failed: " + connectionResult.toString());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Log.d(TAG, "Firing onLocationChanged..............................................");
//        mCurrentLocation = location;
//        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//        updateUI();
//    }
//
//
//    private void updateUI() {
//        Log.d(TAG, "UI update initiated .............");
//        if (null != mCurrentLocation) {
//            String lat = String.valueOf(mCurrentLocation.getLatitude());
//            String lng = String.valueOf(mCurrentLocation.getLongitude());
//            System.out.print("At Time: " + mLastUpdateTime + "\n" +
//                    "Latitude: " + lat + "\n" +
//                    "Longitude: " + lng + "\n" +
//                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
//                    "Provider: " + mCurrentLocation.getProvider());
//            Toast.makeText(this,lat+" "+lng,Toast.LENGTH_LONG).show();
//        } else {
//            Log.d(TAG, "location is null ...............");
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//    }
//
//    protected void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        Log.d(TAG, "Location update stopped .......................");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//            Log.d(TAG, "Location update resumed .....................");
//        }
//    }
//}