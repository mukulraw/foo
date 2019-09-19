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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.RecycleViewAdapterMenuItemsWaiter;

import Events.MenuEvent;
import Model.ItemJson;
import Model.MenuJson;
import Model.SubCatJson;
import SharedPreferences.SavedParameter;
import Utils.DatabaseHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 10/9/2016.
 */
public class MenuManagement extends Activity {
    Spinner spinner_cat;
    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    SavedParameter savedParameter;
    TabLayout tabLayout;
    DatabaseHelper databaseHelper;
    String rid;
    ImageView img_photo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_mgt);


        savedParameter = new SavedParameter(MenuManagement.this);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_items);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        databaseHelper = new DatabaseHelper(this);
        img_photo = (ImageView)findViewById(R.id.img_photo);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            rid = extras.getString("rid");

        }
        setView();
    }

    public void setView() {

        CallApi();
        spinner_cat = (Spinner) findViewById(R.id.spnr_category);


        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setCategory(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        spinner_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayList<HashMap<String, String>> items = new ArrayList<>();
//                ArrayList<String> list_item = new ArrayList<>();
//                ArrayList<String> list_item_price = new ArrayList<>();
                items = databaseHelper.getItems(spinner_cat.getSelectedItem().toString());
//                Toast.makeText(MenuManagement.this, spinner_cat.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
//                for (int i = 0; i < items.size(); i++) {
//                    String name = items.get(i).get("item_name");
//                    String price = items.get(i).get("item_price");
//                    list_item.add(name);
//                    list_item_price.add(price);
//                }
                setItems(items);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });


    }



    private void setItems(ArrayList<HashMap<String, String>> items) {
        mRecyclerView.setHasFixedSize(true);

        mAdapter_items = new RecycleViewAdapterMenuItemsWaiter(this, items,img_photo);
        mRecyclerView.setAdapter(mAdapter_items);
        mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        //Add MyItemDecoration
        //  mRecyclerView.addItemDecoration(new DividerItemDecoration(Home.this, null));

        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.divider, R.dimen.divider)
                        .build());
    }

    public void setCategory(String menu_name) {
        // Creating adapter for spinner
        ArrayList<HashMap<String, String>> list_cat = new ArrayList<>();
        ArrayList<String> list = new ArrayList<>();
        list_cat = databaseHelper.getProduct(menu_name);
        for (int i = 0; i < list_cat.size(); i++) {
            String name = list_cat.get(i).get("sub_cat_name");
            list.add(name);
        }

        ArrayAdapter<String> adapter_menu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);

        // Drop down layout style - list view with radio button
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_cat.setPrompt("Choose");

        // attaching data adapter to spinner
        spinner_cat.setAdapter(adapter_menu);


    }


    public void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/search_result_menu_display";
            new MenuItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(MenuManagement.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("rid",rid);
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

    public class MenuItems extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public MenuItems(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MenuManagement.this);
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
                tabLayout.removeAllTabs();

                try {
                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    MenuEvent objs = gson.fromJson(reader, MenuEvent.class);

                    for (MenuJson mr : objs.getMenu()) {
                        String id = mr.getId();
                        String name = mr.getName();


                        for (SubCatJson sr : mr.getSub_cat()) {
                            String id_sub_cat = sr.getId();
                            String name_sub_cat = sr.getName();
                            String photo = sr.getPhoto();
                            String sub_category = sr.getSub_category();
                            String status = sr.getStatus();


                            for (ItemJson im : sr.getItems()) {
                                String name_item = im.getName();
                                String price = im.getPrice();
                                String item_id = im.getId();
                                String item_type = im.getItem_type();


                                databaseHelper.insertProduct(name, id, id_sub_cat, name_sub_cat, photo, status, sub_category, item_id,name_item, price);
                            }

                        }

                        tabLayout.addTab(tabLayout.newTab().setText(name));
                        tabLayout.setActivated(true);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(MenuManagement.this, t_alert, m_alert);
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
