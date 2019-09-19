//package com.ratna.foosip;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import Adapter.RestaurantAdapter;
//import Events.RestaurantEvent;
//import Model.RestaurantJson;
//import SharedPreferences.SavedParameter;
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
///**
// * Created by ratna on 1/7/2017.
// */
//public class GPS extends Activity {
//
//    EditText ed_city, ed_loc, ed_seats;
//    SavedParameter savedParameter;
//    ListView listView;
//    ImageView img_search,img_loc;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout_look_table);
//
//        ed_city = (EditText) findViewById(R.id.ed_city);
//        ed_loc = (EditText) findViewById(R.id.ed_key);
//        ed_seats = (EditText) findViewById(R.id.ed_seats);
//        savedParameter = new SavedParameter(this);
//        listView = (ListView) findViewById(R.id.listView_history);
//        img_search = (ImageView) findViewById(R.id.img_search);
//        img_loc = (ImageView)findViewById(R.id.img_loc);
//
//        setView();
//
//
//    }
//
//    private void setView() {
//
//        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
//        ll_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                finish();
//
//            }
//        });
//
////        img_search.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////
////
////            }
////        });
//
//        CallApi();
//
////        img_loc.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                CallApi();
////
////            }
////        });
//
//
//    }
//
//    private void setItems(ArrayList<HashMap<String, String>> items) {
//        ListView listView = (ListView) findViewById(R.id.listView_history);
//        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, items);
//        listView.setAdapter(restaurantAdapter);
//    }
//
//    public void CallApi() {
//
//        ConnectionDetector connectionDetector = new ConnectionDetector(this);
//        boolean isInternet = connectionDetector.isConnectingToInternet();
//
//        if (isInternet) {
//            String url = "http://foosip.com/usersapi/gps_search";
//            new Restaurants(url).execute();
//        } else {
//            AlertClassRetry alert = new AlertClassRetry();
//            String t_alert = getResources().getString(R.string.error);
//            String m_alert = getResources().getString(R.string.no_internet);
//            alert.showAlert(GPS.this, t_alert, m_alert);
//        }
//    }
//
//    private String doPost(String url) throws IOException {
//        MediaType JSON
//                = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("lat","26.449227");
//            jsonObject.put("lng", "80.308260");
//            jsonObject.put("km", "10");
//            jsonObject.put("seats", "3");
//            Log.i("json_format", jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .header("Authorization", savedParameter.getTOKEN(this))
//                .url(url)
//                .post(body)
//                .build();
//
//
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }
//
//    public class Restaurants extends AsyncTask<Void, Void, String> {
//
//        int response_code;
//        String url;
//        ProgressDialog progressDialog;
//
//        public Restaurants(String url) {
//            this.url = url;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(GPS.this);
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
//                response = doPost(url);
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
//                    RestaurantEvent objs = gson.fromJson(reader, RestaurantEvent.class);
//                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
//
//                    for (RestaurantJson or : objs.getRestaurant()) {
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        String rid = or.getRid();
//                        String seats = or.getAvailable();
//                        String name = or.getName();
//                        String address = or.getTxt_address();
//                        String res_area = or.getRes_area();
//                        Log.i("MY INFO", name);
//
//                        hashMap.put("rid", rid);
//                        hashMap.put("seats", seats);
//                        hashMap.put("name", name);
//                        hashMap.put("address", address);
//                        hashMap.put("res_area", res_area);
//
//
//                        items.add(hashMap);
//
//
//                    }
//
//                    setItems(items);
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                AlertClassRetry alert = new AlertClassRetry();
//                String t_alert = getResources().getString(R.string.error);
//                String m_alert = getResources().getString(R.string.server_error);
//                alert.showAlert(GPS.this, t_alert, m_alert);
//            }
//        }
//    }
//
//    public class AlertClassRetry {
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
//                    CallApi();
//                    //activity.finish();
//                }
//            });
//
//            // Showing Alert Message
//            alertDialog.show();
//        }
//    }
//}
//
