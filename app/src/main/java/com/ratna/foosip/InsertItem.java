package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Events.MenuEvent;
import Model.ItemJson;
import Model.MenuJson;
import Model.SubCatJson;
import SharedPreferences.SavedParameter;
import Utils.DatabaseHelper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 11/16/2016.
 */

public class InsertItem {
    public ArrayList<HashMap<String, String>> items;
    public JSONArray jsonArray;
    DatabaseHelper databaseHelper;
    Activity activity;
    SavedParameter savedParameter;

    public InsertItem(Activity activity) {
        this.activity = activity;
        this.databaseHelper = new DatabaseHelper(activity);
        items = new ArrayList<>();
        savedParameter = new SavedParameter(activity);
        AddItems();

    }

    public InsertItem(Activity activity, ArrayList<HashMap<String, String>> items)
    {
        this.activity = activity;
        this.items = items;
        savedParameter = new SavedParameter(activity);
        Add();
    }

    private void AddItems() {
        items = databaseHelper.getCartProduct();
        jsonArray = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("prd_id", items.get(i).get("item_id"));
                jsonObject.put("price", items.get(i).get("item_price"));
                jsonObject.put("qty", items.get(i).get("item_quantity"));
                jsonObject.put("temp_order_id",savedParameter.getTempOrderId(activity));

                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CallApi();
    }

    private void Add() {
        jsonArray = new JSONArray();
        for (int i = 0; i < items.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("prd_id", items.get(i).get("item_id"));
                jsonObject.put("price", items.get(i).get("item_price"));
                jsonObject.put("qty", items.get(i).get("item_qty"));
                jsonObject.put("temp_order_id",savedParameter.getTempOrderId(activity));

                jsonArray.put(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CallApi();
    }

    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(activity);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/ordersapi/insert_order_items";
            new PlaceItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = activity.getResources().getString(R.string.error);
            String m_alert = activity.getResources().getString(R.string.no_internet);
            alert.showAlert(activity, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {

            jsonObject.put("rid", savedParameter.getrId(activity));
            jsonObject.put("orderitems", jsonArray);
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

    public class PlaceItems extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public PlaceItems(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(activity);
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
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if(message.equals("Items placed")) {
                        databaseHelper.clearCartTable();
                        databaseHelper.clearTable();
                        Intent intent = new Intent(activity, OrderScreen.class);
                        activity.startActivity(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = activity.getResources().getString(R.string.error);
                String m_alert = activity.getResources().getString(R.string.server_error);
                alert.showAlert(activity, t_alert, m_alert);
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
