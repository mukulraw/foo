package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.HistoryAdapter;
import Adapter.RestaurantAdapter;
import Events.OrderEvent;
import Events.RestaurantEvent;
import Model.Item;
import Model.OrderJson;
import Model.RestaurantJson;
import SharedPreferences.SavedParameter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 12/5/2016.
 */
public class LookForTable extends Activity {

    EditText ed_loc,ed_loc2;
    SavedParameter savedParameter;
    ListView listView;
    ImageView img_search,img_loc;
    Spinner spinner_people,spinner_city,ed_city,ed_seats;
    LinearLayout ll_1,ll_2,ll_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_look_table);

        ed_city = (Spinner) findViewById(R.id.sp_city1);
        ed_seats = (Spinner) findViewById(R.id.sp_people1);

        ed_loc = (EditText) findViewById(R.id.ed_key);
        ed_loc2 = (EditText) findViewById(R.id.ed_key2);

        savedParameter = new SavedParameter(this);
        listView = (ListView) findViewById(R.id.listView_history);
        img_search = (ImageView) findViewById(R.id.img_search);
        img_loc = (ImageView)findViewById(R.id.img_loc);
        spinner_people = (Spinner) findViewById(R.id.sp_people2);
        spinner_city = (Spinner) findViewById(R.id.sp_city);
        ll_1 = (LinearLayout)findViewById(R.id.ll_1);
        ll_2 = (LinearLayout)findViewById(R.id.ll_2);
        ll_search = (LinearLayout)findViewById(R.id.ll_look_table);

        ll_2.setVisibility(View.VISIBLE);
        ll_1.setVisibility(View.GONE);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallApi1();

            }
        });

        setView();


    }

    private void setView() {

        setCategory();
        setCategory2();
        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CallApi();

            }
        });
//
//        img_loc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(LookForTable.this, GPS.class);
//                startActivity(intent);
//
//            }
//        });


    }

    private void setItems(ArrayList<HashMap<String, String>> items) {
        ll_1.setVisibility(View.VISIBLE);
        ll_2.setVisibility(View.GONE);
        ListView listView = (ListView) findViewById(R.id.listView_history);
        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, items);
        listView.setAdapter(restaurantAdapter);
    }

    public void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/search";
            new Restaurants(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(LookForTable.this, t_alert, m_alert);
        }
    }

    public void CallApi1() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/search";
            new Restaurants1(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(LookForTable.this, t_alert, m_alert);
        }
    }

    public void setCategory() {
        // Creating adapter for spinner
        ArrayList<String> list_cat = new ArrayList<>();
        list_cat.add("Delhi");
        list_cat.add("Bangalore");
        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_cat);

        // Drop down layout style - list view with radio button
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setPrompt("Choose");

        // attaching data adapter to spinner
        spinner_city.setAdapter(adapter_city);
        ed_city.setAdapter(adapter_city);



    }

    public void setCategory2() {
        // Creating adapter for spinner
        ArrayList<String> list_cat = new ArrayList<>();
        list_cat.add("Select Number of People");
        list_cat.add("1");
        list_cat.add("2");
        list_cat.add("3");
        list_cat.add("4");
        list_cat.add("5");
        list_cat.add("6");
        list_cat.add("7");
        list_cat.add("8");
        list_cat.add("9");
        list_cat.add("10");
        list_cat.add("11");
        list_cat.add("12");
        ArrayAdapter<String> adapter_city = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_cat);

        // Drop down layout style - list view with radio button
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_people.setPrompt("Choose");

        // attaching data adapter to spinner
        spinner_people.setAdapter(adapter_city);
        ed_seats.setAdapter(adapter_city);


    }



    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("city", ed_city.getSelectedItem().toString());
            jsonObject.put("keyword", ed_loc.getText().toString());
            jsonObject.put("seats", ed_seats.getSelectedItem().toString());
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

            progressDialog = new ProgressDialog(LookForTable.this);
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

//            Toast.makeText(LookForTable.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    RestaurantEvent objs = gson.fromJson(reader, RestaurantEvent.class);
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();

                    for (RestaurantJson or : objs.getRestaurant()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String rid = or.getRid();
                        String seats = or.getAvailable();
                        String name = or.getName();
                        String address = or.getTxt_address();
                        String res_area = or.getRes_area();
                        String available = or.getAvailable();
                        String rating = or.getTotal_rating();
                        String waiting = or.getWaiting();
                        String phone = or.getPhone();
                        String logo = or.getLogo();
                        String pin = or.getPin();
                        String open_time=  or.getOpen_time();
                        String close_time = or.getClose_time();
                        Log.i("MY INFO", name);

                        hashMap.put("rid", rid);
                        hashMap.put("seats", seats);
                        hashMap.put("name", name);
                        hashMap.put("address", address);
                        hashMap.put("res_area", res_area);
                        hashMap.put("available", available);
                        hashMap.put("rating", rating);
                        hashMap.put("waiting", waiting);
                        hashMap.put("logo", logo);

                        hashMap.put("extras", "avail "+available+" rat "+rating+" wait "+waiting+" ph "+phone+" "+pin+" time "+open_time+" "+close_time);


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
                alert.showAlert(LookForTable.this, t_alert, m_alert);
            }
        }
    }


    private String doPost1(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("city", spinner_city.getSelectedItem().toString());
            jsonObject.put("keyword", ed_loc2.getText().toString());
            jsonObject.put("seats", spinner_city.getSelectedItem().toString());
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

    public class Restaurants1 extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public Restaurants1(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LookForTable.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String response = "";

            try {
                response = doPost1(url);
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

//            Toast.makeText(LookForTable.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    RestaurantEvent objs = gson.fromJson(reader, RestaurantEvent.class);
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();

                    for (RestaurantJson or : objs.getRestaurant()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String rid = or.getRid();
                        String seats = or.getAvailable();
                        String name = or.getName();
                        String address = or.getTxt_address();
                        String res_area = or.getRes_area();
                        String available = or.getAvailable();
                        String rating = or.getTotal_rating();
                        String waiting = or.getWaiting();
                        String phone = or.getPhone();
                        String logo = or.getLogo();
                        String pin = or.getPin();
                        String open_time=  or.getOpen_time();
                        String close_time = or.getClose_time();
                        Log.i("MY INFO", name);

                        hashMap.put("rid", rid);
                        hashMap.put("seats", seats);
                        hashMap.put("name", name);
                        hashMap.put("address", address);
                        hashMap.put("res_area", res_area);
                        hashMap.put("available", available);
                        hashMap.put("rating", rating);
                        hashMap.put("waiting", waiting);
                        hashMap.put("logo", logo);
                        hashMap.put("open_time",open_time);
                        hashMap.put("close_time",close_time);
                        hashMap.put("pin",pin);
                        hashMap.put("phone",phone);

                        hashMap.put("extras", "avail "+available+" rat "+rating+" wait "+waiting+" ph "+phone+" "+pin+" time "+open_time+" "+close_time);


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
                alert.showAlert(LookForTable.this, t_alert, m_alert);
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

