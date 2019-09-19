package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.RecycleViewResQueue;
import SharedPreferences.SavedParameter;
import Utils.DatabaseHelper;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 12/5/2016.
 */
public class PutInQueue extends Activity {


    SavedParameter savedParameter;
    ListView listView;
    ImageView img_list, img_icon;
    LinearLayout ll_queue;
    String rid, seats, name, address, phone, rating, waiting, logo, available,open_time,close_time,pin;
    StringBuilder details;
    TextView txt_name, txt_address,txt_ph,txt_seats,txt_rating,txt_waiting,txt_available,txt_open,txt_close,txt_pin;
    String token_no;
    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView_items;
    LinearLayout ll_nav;
    DatabaseHelper databaseHelper;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.put_in_queue);


        savedParameter = new SavedParameter(this);
        ll_queue = (LinearLayout) findViewById(R.id.ll_queue);

        txt_name = (TextView) findViewById(R.id.txt_name);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_ph = (TextView) findViewById(R.id.txt_ph);
        txt_seats = (TextView) findViewById(R.id.txt_seats);
        txt_rating = (TextView) findViewById(R.id.txt_rating);
        txt_waiting = (TextView) findViewById(R.id.txt_waiting);
        txt_available = (TextView) findViewById(R.id.txt_available);
        txt_open = (TextView) findViewById(R.id.txt_open_time);
        txt_close = (TextView) findViewById(R.id.txt_close_time);
        txt_pin = (TextView) findViewById(R.id.txt_pin);


        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        img_list = (ImageView) findViewById(R.id.img_list);
        img_icon = (ImageView) findViewById(R.id.icon);
        ll_nav = (LinearLayout) findViewById(R.id.ll_nav);
        databaseHelper = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rid = extras.getString("rid");
            seats = extras.getString("seats");
            name = extras.getString("name");
            address = extras.getString("address");
            phone = extras.getString("phone");
            rating = extras.getString("rating");
            waiting = extras.getString("waiting");
            logo = extras.getString("logo");
            available = extras.getString("available");
            open_time = extras.getString("open_time");
            close_time = extras.getString("close_time");
            pin = extras.getString("pin");
        }

        details = new StringBuilder();

//        details.append("Seats "+seats);
//        details.append("\n");
//        details.append(address);
//        details.append("\n");
//        details.append(res_area);
//        details.append("\n");
//        details.append("Rating " + rating);
//        details.append("\n");
//        details.append("Waiting " + waiting);
//        details.append("\n");
//        details.append("Available "+available);

        setView();




    }

    private void setView() {

        Picasso.with(this).load(logo)
                .placeholder(R.drawable.ic_watermark).fit().into(img_icon);

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        LinearLayout ll_menu = (LinearLayout) findViewById(R.id.ll_menu);
        ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseHelper.clearTable();
                Intent intent = new Intent(PutInQueue.this, MenuManagement.class);
                Bundle bundle = new Bundle();
                bundle.putString("rid", rid);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        txt_name.setText(name);
        txt_address.setText("Address- "+address);
        txt_ph.setText("Phone- "+phone);
//        txt_seats.setText("Seats- "+seats);
        if(rating.equals("0")) {
            txt_rating.setText("Rating- NA");

        }else{
            txt_rating.setText("Rating- " + rating);
        }
//        txt_waiting.setText("Waiting- "+waiting);
        txt_open.setText("Open- "+open_time);
        txt_close.setText("Close- "+close_time);
        txt_pin.setText("Pincode- "+pin);
        if(available.equals("0"))
        {
            txt_available.setText("Tables- Available");
        }else{
            txt_available.setText("Tables- Not Available");
        }

//        if (!savedParameter.getQUEUE(this)) {
//            ll_queue.setVisibility(View.GONE);
//        }


        ll_queue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (available.equals("0")) {
                    CallApi();
                }

            }
        });

        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLeft(view);

            }
        });

    }

    public void onLeft(View view) {
        mDrawerLayout.openDrawer(ll_nav);
    }


    private void setItems(ArrayList<HashMap<String, String>> items, String token_no) {
        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecycleViewResQueue(this, items, token_no);
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


    public void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/putin_queue";
            new Restaurants(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(PutInQueue.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("rid", rid);
            jsonObject.put("seats", seats);
            Log.i("json_format", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .header("Authorization", savedParameter.getTOKEN())
                .url(url)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public class Restaurants extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public Restaurants(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(PutInQueue.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String response = "";

            try {
                response = doPost(url);
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
                    Log.i("MY INFO", "Json Parser started..");


                    Log.i("MY INFO", result.toString());
//                    Toast.makeText(PutInQueue.this, result.toString(), Toast.LENGTH_SHORT).show();


                    JSONObject jsonObject = new JSONObject(result);
                    token_no = jsonObject.getString("token_no");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(PutInQueue.this, t_alert, m_alert);
            }
        }
    }

    public class AlertClassRetry {

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
                    CallApi();
                    //activity.finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }


//    private void CallQueueList()
//    {
//        ConnectionDetector connectionDetector = new ConnectionDetector(this);
//        boolean isInternet = connectionDetector.isConnectingToInternet();
//
//        if (isInternet) {
//            String url = "http://foosip.com/usersapi/view_user_queue";
//            new QueueList(url).execute();
//        } else {
//            AlertClassRetryQueue alert = new AlertClassRetryQueue();
//            String t_alert = getResources().getString(R.string.error);
//            String m_alert = getResources().getString(R.string.no_internet);
//            alert.showAlert(PutInQueue.this, t_alert, m_alert);
//        }
//    }
//
//    private String doPostQueue(String url) throws IOException {
//        MediaType JSON
//                = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//
//
//        Request request = new Request.Builder()
//                .header("Authorization", savedParameter.getTOKEN(this))
//                .url(url)
//                .build();
//
//
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }
//
//    public class QueueList extends AsyncTask<Void, Void, String> {
//
//        int response_code;
//        String url;
//        ProgressDialog progressDialog;
//
//        public QueueList(String url) {
//            this.url = url;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(PutInQueue.this);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Void... arg0) {
//            String response = "";
//
//            try {
//                response = doPostQueue(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //dismiss prgress dialogue
//            progressDialog.dismiss();
//            // Check server response
//            Log.i("result", result);
//
////            Toast.makeText(SignIn.this, result.toString(), Toast.LENGTH_SHORT).show();
//
//
//            if (result != null && !result.equals("")) {
//
//                try {
//                    Log.i("MY INFO", "Json Parser started..");
//                    Gson gson = new Gson();
//
//                    Log.i("MY INFO", result.toString());
//                    StringReader reader = new StringReader(result);
//                    WaitingEvent objs = gson.fromJson(reader, WaitingEvent.class);
//                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
//
//                    for (WaitingJson or : objs.getWaiting()) {
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        String token_n = or.getToken_no();
//                        String seats = or.getNo_seats();
//                        String status = or.getStatus();
//                        String name = or.getUname();
//                        Log.i("MY INFO", name);
//
//                        hashMap.put("token_n", token_n);
//                        hashMap.put("seats", seats);
//                        hashMap.put("status", status);
//                        hashMap.put("uname", name);
//
//
//
//                        items.add(hashMap);
//
//
//                    }
//
//                    setItems(items,token_no);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                AlertClassRetryQueue alert = new AlertClassRetryQueue();
//                String t_alert = getResources().getString(R.string.error);
//                String m_alert = getResources().getString(R.string.server_error);
//                alert.showAlert(PutInQueue.this, t_alert, m_alert);
//            }
//        }
//    }
//
//    public class AlertClassRetryQueue {
//
//        Activity activity;
//
//        public void showAlert(final Activity activity, String title, String msg) {
//            AlertDialog alertDialog = new AlertDialog.Builder(
//                    activity).create();
//
//            this.activity = activity;
//            // Setting Dialog Title
//            alertDialog.setTitle(title);
//
//            // Setting Dialog Message
//            alertDialog.setMessage(msg);
//            alertDialog.setCancelable(false);
//
//            // Setting Icon to Dialog
//            //alertDialog.setIcon(R.drawable.tick);
//
//            // Setting OK Button
//            alertDialog.setButton("Retry", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    // Write your code here to execute after dialog closed
//                    //Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
//                    CallQueueList();
//                    //activity.finish();
//                }
//            });
//
//            // Showing Alert Message
//            alertDialog.show();
//        }
//    }
}

