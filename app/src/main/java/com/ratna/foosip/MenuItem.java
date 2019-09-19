package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.RecycleViewAdapterCart;
import Adapter.RecycleViewAdapterMenuItems;
import Events.MenuEvent;
import Model.ItemJson;
import Model.MenuJson;
import Model.SubCatJson;
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
 * Created by ratna on 9/22/2016.
 */
public class MenuItem extends Activity {

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView_items;
    SavedParameter savedParameter;
    TabLayout tabLayout;
    DatabaseHelper databaseHelper;
    Spinner spinner_cat;
    String qr_code = "";
    String order_id = "";
    private DrawerLayout mDrawerLayout;
    private LinearLayout ll_drawer;
    private FrameLayout frame_dialog;
    private ImageView img_photo;
    private LinearLayout ll_back,ll_proceed,ll_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hotel_menu);

        databaseHelper = new DatabaseHelper(this);
        savedParameter = new SavedParameter(MenuItem.this);
        ll_drawer = (LinearLayout) findViewById(R.id.ll_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView_items);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        frame_dialog = (FrameLayout) findViewById(R.id.frame_dialog);
        spinner_cat = (Spinner) findViewById(R.id.spnr_category);
        img_photo = (ImageView)findViewById(R.id.img_photo);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_proceed = (LinearLayout) findViewById(R.id.ll_proceed);
        ll_next = (LinearLayout) findViewById(R.id.ll_next);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            qr_code = extras.getString("qr_code");
            savedParameter.setQrCode(qr_code);
        }


        setView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        onClose();

    }

    private void setView() {

        if(savedParameter.getMainUser(this) || savedParameter.getSubUser(this))
        {
            CallApiSubUser();
            ll_back.setEnabled(true);

        }else{
            ll_back.setEnabled(false);
            CallApi();
        }


        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MenuItem.this,OrderSummary.class);
                startActivity(intent);
                finish();

            }
        });


        ImageView img_cart = (ImageView) findViewById(R.id.img_cart);
        img_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onLeft(view);

            }
        });

        ImageView img_order_id = (ImageView) findViewById(R.id.img_order_id);
        img_order_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogShowId(savedParameter.getTempOrderId(MenuItem.this));
            }
        });



        ll_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = databaseHelper.getCartProductCount();
                if (count >= 1) {
                    new InsertItem(MenuItem.this);
                } else {
                    Toast.makeText(MenuItem.this, "Please Add atleast one item", Toast.LENGTH_SHORT).show();
                }


            }
        });


        if(savedParameter.getSubUser(MenuItem.this))
        {
            ll_next.setVisibility(View.VISIBLE);

        }


        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuItem.this,OrderScreen.class);
                startActivity(intent);
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

    private void setItems(ArrayList<HashMap<String, String>> items) {

        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecycleViewAdapterMenuItems(this, items,img_photo);
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

    public void onLeft(View view) {
        mDrawerLayout.openDrawer(ll_drawer);
        cartAdapter();
//        getFragmentManager().beginTransaction().add(R.id.frame_layout_0, new FragmentCart().newInstance(MenuItem.this)).commit();
    }

    public void onClose() {
        mDrawerLayout.closeDrawer(ll_drawer);
    }

    private void cartAdapter() {
        ArrayList<HashMap<String, String>> items = new ArrayList<>();
        items = databaseHelper.getCartProduct();
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecycleViewAdapterCart(this, items);
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

        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.GRAY)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.divider, R.dimen.divider)
                        .build());

    }

    public void dialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_temp_order_id);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.x = -50;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.setCancelable(false);

        ImageView img_del = (ImageView) dialog.findViewById(R.id.img_del);
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();


            }
        });

        final EditText ed_order_id = (EditText) dialog.findViewById(R.id.ed_order_id);
        final TextView txt_order_send = (TextView)dialog.findViewById(R.id.txt_order_send);
        txt_order_send.setVisibility(View.GONE);

        LinearLayout ll_done = (LinearLayout) dialog.findViewById(R.id.ll_done);
        ll_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String local_order_id = ed_order_id.getText().toString();
                String temp_order_id = savedParameter.getTempOrderId(MenuItem.this);
                String sub_order_id = temp_order_id.substring(temp_order_id.length()-4);
//                Toast.makeText(MenuItem.this,sub_order_id,Toast.LENGTH_SHORT).show();
                if(local_order_id.equals(sub_order_id)) {
                    dialog.dismiss();
                    txt_order_send.setVisibility(View.GONE);
//                    order_id = temp_order_id;
                    CallApiSubUser();
                }else{
                    txt_order_send.setVisibility(View.VISIBLE);
                    txt_order_send.setText("Check order id!!!");
//                    Toast.makeText(MenuItem.this,"Check order id!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });


        dialog.show();

    }

    public void dialogShowId(String order_id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_show_temp_order_id);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.x = -50;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);

        ImageView img_del = (ImageView) dialog.findViewById(R.id.img_del);
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();


            }
        });

        final TextView txt_order_id = (TextView) dialog.findViewById(R.id.txt_order_id);
        String sub_order_id = order_id.substring(order_id.length()-4);
        txt_order_id.setText(sub_order_id);

        dialog.show();

    }


    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/book_table";
            new MenuItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(MenuItem.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("qr_code", savedParameter.getQrCode());
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

    private void CallApiSubUser() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/rest_menu_display";
            new MenuItemsSubUser(url).execute();
        } else {
            AlertClassRetrySubUser alert = new AlertClassRetrySubUser();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(MenuItem.this, t_alert, m_alert);
        }
    }

    private String doPostSubUser(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("temp_order_id", savedParameter.getTempOrderId(this));
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

            progressDialog = new ProgressDialog(MenuItem.this);
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
                    tabLayout.removeAllTabs();
                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    MenuEvent objs = gson.fromJson(reader, MenuEvent.class);


                    savedParameter.setTempOrderId(objs.getTemp_order_id());
                    String message = objs.getMessage();

                    if (message.equals("Table already in use")) {
                        savedParameter.setSubUser(true);
                        savedParameter.setMain_user(false);
                        ll_next.setVisibility(View.VISIBLE);
                        dialog();
                    } else {
//                        Toast.makeText(MenuItem.this,savedParameter.getTempOrderId(MenuItem.this),Toast.LENGTH_LONG).show();
                        savedParameter.setSubUser(false);
                        savedParameter.setMain_user(true);
                        dialogShowId(savedParameter.getTempOrderId(MenuItem.this));
                        objs.setQr_code(objs.getQr_code());
                        objs.setRid(objs.getRid());
                        objs.setTid(objs.getTid());
                        objs.setTableno(objs.getTableno());
                        objs.setWaiter_id(objs.getWaiter_id());
                        savedParameter.setMenueventObj(objs);

                        for (MenuJson mr : objs.getMenu()) {
                            String id = mr.getId();
                            String name = mr.getName();
                            Log.i("MY INFO", name);

                            for (SubCatJson sr : mr.getSub_cat()) {
                                String id_sub_cat = sr.getId();
                                String name_sub_cat = sr.getName();
                                String photo = sr.getPhoto();
                                String sub_category = sr.getSub_category();
                                String status = sr.getStatus();
                                Log.i("MY INFO", name_sub_cat);

                                for (ItemJson im : sr.getItems()) {
                                    String name_item = im.getName();
                                    String price = im.getPrice();
                                    String item_id = im.getId();
                                    String r_id = im.getRid();
                                    Log.i("MY INFO", price);
                                    savedParameter.setrId(r_id);
                                    databaseHelper.insertProduct(name, id, id_sub_cat, name_sub_cat, photo, status, sub_category, item_id, name_item, price);

                                }

                            }

                            tabLayout.addTab(tabLayout.newTab().setText(name));
                            tabLayout.setActivated(true);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(MenuItem.this, t_alert, m_alert);
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
                    databaseHelper.clearCartTable();
                    CallApi();
                    //activity.finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

    public class MenuItemsSubUser extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public MenuItemsSubUser(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MenuItem.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... arg0) {
            String response = "";

            try {
                response = doPostSubUser(url);
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
                    tabLayout.removeAllTabs();

                    Log.i("MY INFO", "Json Parser started..");
                    Gson gson = new Gson();

                    Log.i("MY INFO", result.toString());
                    StringReader reader = new StringReader(result);
                    MenuEvent objs = gson.fromJson(reader, MenuEvent.class);
                    String message = objs.getMessage();

//                    Toast.makeText(MenuItem.this, message, Toast.LENGTH_LONG).show();
//                    if (message.equals("redirect")) {
//                        Intent intent = new Intent(MenuItem.this, OrderSummary.class);
//                        startActivity(intent);
//                    }
                    if (message.equals("Invalid order sharing number")) {
                        dialog();
                    } else {
                        for (MenuJson mr : objs.getMenu()) {
                            String id = mr.getId();
                            String name = mr.getName();
                            Log.i("MY INFO", name);

                            for (SubCatJson sr : mr.getSub_cat()) {
                                String id_sub_cat = sr.getId();
                                String name_sub_cat = sr.getName();
                                String photo = sr.getPhoto();
                                String sub_category = sr.getSub_category();
                                String status = sr.getStatus();
                                Log.i("MY INFO", name_sub_cat);

                                for (ItemJson im : sr.getItems()) {
                                    String name_item = im.getName();
                                    String price = im.getPrice();
                                    String item_id = im.getId();
                                    String r_id = im.getRid();
                                    Log.i("MY INFO", price);
                                    savedParameter.setrId(r_id);
                                    databaseHelper.insertProduct(name, id, id_sub_cat, name_sub_cat, photo, status, sub_category, item_id, name_item, price);

                                }

                            }

                            tabLayout.addTab(tabLayout.newTab().setText(name));
                            tabLayout.setActivated(true);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetrySubUser alert = new AlertClassRetrySubUser();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(MenuItem.this, t_alert, m_alert);
            }
        }
    }

    public class AlertClassRetrySubUser {

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
                    databaseHelper.clearCartTable();
                    CallApiSubUser();
                    //activity.finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }


    @Override
    public void onBackPressed() {
        // Write your code here
        Intent intent = new Intent(MenuItem.this,Home.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}

