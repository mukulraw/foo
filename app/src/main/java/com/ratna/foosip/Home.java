package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;

import SharedPreferences.SavedParameter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;


import Adapter.RecycleViewAdapterItemNavDrawer;
import Adapter.RecycleViewResQueue;
import Events.WaitingEvent;
import Model.WaitingJson;
import Utils.DatabaseHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by ratna on 9/12/2016.
 */
public class Home extends FragmentActivity {
    private static final String TAG = Home.class.getSimpleName();
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter,mAdapter_items;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    DatabaseHelper databaseHelper;
    String Titles[] = {"History", "Refer App", "Feedback", "About Us", "Logout"};
    int ICONS[] = {R.drawable.ic_watched_videos, R.drawable.ic_share_black_24dp, R.drawable.ic_feedback_black_24dp,
            R.drawable.abt_us, R.drawable.ic_exit_to_app_black_24dp};
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView_items;
    SavedParameter savedParameter;
    LinearLayout ll_queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        databaseHelper = new DatabaseHelper(this);
        savedParameter = new SavedParameter(this);




        LinearLayout ll_nav = (LinearLayout) findViewById(R.id.ll_nav_home);
        ll_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLeft(view);

            }
        });

        ll_queue = (LinearLayout) findViewById(R.id.ll_queue);
        ll_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onRight(view);

            }
        });


        LinearLayout ll_activate = (LinearLayout) findViewById(R.id.ll_activate);
        ll_activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.clearTable();
                Intent intent = new Intent(Home.this, ScanQR.class);
                startActivity(intent);

            }
        });

        LinearLayout ll_look_table = (LinearLayout) findViewById(R.id.ll_look_table);
        ll_look_table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Home.this, LookForTable.class);
                startActivity(intent);

            }
        });

//        LinearLayout ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
//        ll_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new CancelQueue(Home.this);
//
//            }
//        });
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // checking for type intent filter
//                if (intent.getAction().equals(app.Config.REGISTRATION_COMPLETE)) {
//                    // gcm successfully registered
//                    // now subscribe to `global` topic to receive app wide notifications
//                    FirebaseMessaging.getInstance().subscribeToTopic(app.Config.TOPIC_GLOBAL);
//
//
//
//                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
//                    // new push notification is received
//
//                    String message = intent.getStringExtra("message");
//
//                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
//                }
//            }
//        };



        setView();
    }


    public void setView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView2);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecycleViewAdapterItemNavDrawer(Home.this, Titles, ICONS, "");
        mRecyclerView.setAdapter(mAdapter);
        // mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        /*
        mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        */
        mRecyclerView.setLayoutManager(mLayoutManager);
        //Add MyItemDecoration
        //  mRecyclerView.addItemDecoration(new DividerItemDecoration(Home.this, null));

        final int s = 3;
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.WHITE)
                        .sizeResId(R.dimen.activity_horizontal_margin)
                        .marginResId(R.dimen.activity_horizontal_margin, R.dimen.activity_horizontal_margin)
                        .build());


        CallQueueList();

    }


    public void onLeft(View view) {
        mDrawerLayout.closeDrawer(mRecyclerView_items);
        mDrawerLayout.openDrawer(mRecyclerView);
    }

    public void onRight(View view) {
        mDrawerLayout.closeDrawer(mRecyclerView);
        mDrawerLayout.openDrawer(mRecyclerView_items);
    }

    public void onClose() {
        mDrawerLayout.closeDrawer(mRecyclerView);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
//    private void displayFirebaseRegId() {
//
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(app.Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//
//        Log.e(TAG, "Firebase reg id: " + regId);
//
////        if (!TextUtils.isEmpty(regId))
////            Toast.makeText(this,regId.toString(),Toast.LENGTH_SHORT).show();
////        else
////            Toast.makeText(this,"reg id not recieved yet!!",Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onResume() {
        super.onResume();

//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(app.Config.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(app.Config.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void CallQueueList()
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/view_user_queue";
            new QueueList(url).execute();
        } else {
            AlertClassRetryQueue alert = new AlertClassRetryQueue();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(Home.this, t_alert, m_alert);
        }
    }

    private String doPostQueue(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .header("Authorization", savedParameter.getTOKEN())
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public class QueueList extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public QueueList(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Home.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String response = "";

            try {
                response = doPostQueue(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //dismiss prgress dialogue
            progressDialog.dismiss();
            // Check server response
            Log.i("result", result);

//            Toast.makeText(SignIn.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
                    int i=0,count=0;
                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    WaitingEvent objs = gson.fromJson(reader, WaitingEvent.class);
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();

                    String rname = objs.getR_name();
                    String logo = objs.getLogo();

                    for (WaitingJson or : objs.getWaiting()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String token_n = or.getToken_no();
                        String seats = or.getNo_seats();
                        String status = or.getStatus();
                        String name = or.getUname();
                        String uid = or.getUid();
                        Log.i("MY INFO", name);

                        i++;


                        if(savedParameter.getUID(Home.this).equals(uid)) {
                            hashMap.put("token_n", token_n);
                            hashMap.put("seats", seats);
                            hashMap.put("status", status);
                            hashMap.put("uname", name);
                            hashMap.put("rname",rname);
                            hashMap.put("logo",logo);
                            count = i;
                            items.add(hashMap);

                            if(status.equals("a"))
                            {
                                savedParameter.setQUEUE(true);
                            }


                        }






                    }
                    ll_queue.setVisibility(View.VISIBLE);
                    setItems(items, String.valueOf(count));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetryQueue alert = new AlertClassRetryQueue();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(Home.this, t_alert, m_alert);
            }
        }
    }

    public class AlertClassRetryQueue {

        Activity activity;

        public void showAlert(final Activity activity, String title, String msg) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    activity).create();

            this.activity = activity;
            // Setting Dialog Title
            alertDialog.setTitle(title);

            // Setting Dialog Message
            alertDialog.setMessage(msg);
            alertDialog.setCancelable(false);

            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    CallQueueList();
                    //activity.finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    private void setItems(ArrayList<HashMap<String, String>> items,String token_no)
    {
        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecycleViewResQueue(this, items,token_no);
        mRecyclerView_items.setAdapter(mAdapter_items);
        mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView_items.setLayoutManager(mLayoutManager);
        //Add MyItemDecoration
        //  mRecyclerView.addItemDecoration(new DividerItemDecoration(Home.this, null));

        mRecyclerView_items.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.divider, R.dimen.divider)
                        .build());


    }

    @Override
    public void onBackPressed() {
        // Write your code here
        finishAffinity();

        super.onBackPressed();
    }

}
