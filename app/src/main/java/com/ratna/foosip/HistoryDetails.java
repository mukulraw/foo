package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.HistoryAdapter;
import Adapter.RecycleViewAdapterCheckOut;
import Adapter.RecycleViewAdapterHistoryItemDetails;
import Events.HistoryEvent;
import Events.ItemEvent;
import Model.HistoryJson;
import Model.ItemHistoryJson;
import Model.OrderJson;
import SharedPreferences.SavedParameter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 1/4/2017.
 */
public class HistoryDetails extends Activity {
    SavedParameter savedParameter;
    private String temp_order_id;
    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView_items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);


        savedParameter = new SavedParameter(this);
        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView_items);

        Bundle extras = getIntent().getExtras();
        if(extras!=null)
        {
            temp_order_id = extras.getString("temp_order_id");

        }

        setView();


    }

    private void setView()
    {

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        CallApi();

    }
    private void setItems(ArrayList<HashMap<String, String>> items) {

        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecycleViewAdapterHistoryItemDetails(this, items);
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


    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/order_item_details";
            new HistoryGet(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("temp_order_id",temp_order_id );
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

    public class HistoryGet extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public HistoryGet(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(HistoryDetails.this);
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
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    ItemEvent objs = gson.fromJson(reader, ItemEvent.class);
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
                    for (ItemHistoryJson or : objs.getItemHistoryJsons()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String id = or.getId();
                        String name = or.getName();
                        String price = or.getPrice();
                        String qty = or.getQty();
                        Log.i("MY INFO", name);

                        hashMap.put("item_id", id);
                        hashMap.put("item_name", name);
                        hashMap.put("item_price", price);
                        hashMap.put("item_qty", qty);


                        items.add(hashMap);
                    }

                        setItems(items);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(HistoryDetails.this, t_alert, m_alert);
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
}

