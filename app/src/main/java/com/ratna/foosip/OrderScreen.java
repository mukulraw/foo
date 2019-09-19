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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Adapter.RecyclerViewAdapterOrder;
import Events.OrderEvent;
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
 * Created by ratna on 9/23/2016.
 */
public class OrderScreen extends Activity {


    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView_items;
    SavedParameter savedParameter;
    ArrayList<HashMap<String, String>> items_placed;
    TextView txt_no_items, txt_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_order);

        savedParameter = new SavedParameter(this);
        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView_items);
        txt_no_items = (TextView) findViewById(R.id.txt_no_items);
        txt_next = (TextView) findViewById(R.id.txt_next);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
    }

    private void setView() {

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderScreen.this,MenuItem.class);
                startActivity(intent);
                finish();

            }
        });

        CallApi();

        boolean sub_user = savedParameter.getSubUser(OrderScreen.this);
        if (sub_user) {
            txt_next.setText("Next");
        }

        LinearLayout ll_place_order = (LinearLayout) findViewById(R.id.ll_place_order);
        ll_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sub_user = savedParameter.getSubUser(OrderScreen.this);
                if (sub_user) {
                    Intent intent = new Intent(OrderScreen.this, OrderSummary.class);
                    startActivity(intent);

                } else {
                    new PlaceOrder(OrderScreen.this, items_placed);
                }

            }
        });

        LinearLayout ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh);
        ll_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CallApi();
            }
        });

    }

    private void setItems(ArrayList<HashMap<String, String>> items) {

        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecyclerViewAdapterOrder(this, items);
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

    public void NoItems() {
        mRecyclerView_items.setVisibility(View.GONE);
        txt_no_items.setVisibility(View.VISIBLE);
        txt_no_items.setText("NO ITEMS");
    }

    public void HasItems() {
        mRecyclerView_items.setVisibility(View.VISIBLE);
        txt_no_items.setVisibility(View.GONE);
    }


    public void popUpWindow(View view, final String id, final String name, final String qty, final String user_order_status) {
        PopupMenu popup = new PopupMenu(getApplicationContext(), view);
        popup.getMenuInflater().inflate(R.menu.menu_item,
                popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        dialogShowEdit(id, name, qty, user_order_status);
                        return true;

                    case R.id.del:
                        dialogDel(id);
                        return true;

                    default:
                        return onMenuItemClick(item);
                }
            }

        });


    }


    public void dialogShowEdit(final String id, final String name, final String qty, final String user_order_status) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_menu);
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
        final TextView txt_prod = (TextView) dialog.findViewById(R.id.txt_prod);
        final TextView txt_item = (TextView) dialog.findViewById(R.id.txt_item);
        final TextView txt_quan = (TextView) dialog.findViewById(R.id.txt_quan);
        txt_prod.setText(name);
        txt_item.setText(name);
        txt_quan.setText(qty);
        LinearLayout ll_minus = (LinearLayout) dialog.findViewById(R.id.ll_minus);
        LinearLayout ll_plus = (LinearLayout) dialog.findViewById(R.id.ll_plus);
        LinearLayout ll_done = (LinearLayout) dialog.findViewById(R.id.ll_done);
        ll_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String value = txt_quan.getText().toString();
                int i = Integer.parseInt(value);
                if (i > 1 && i != 1) {
                    txt_quan.setText(String.valueOf(--i));

                }
                if (i == 0) {
//                    txt_quan.setText(String.valueOf(--i));
                }


            }
        });
        ll_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String value = txt_quan.getText().toString();
                int i = Integer.parseInt(value);
                txt_quan.setText(String.valueOf(++i));


            }
        });


        ll_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONArray jsonArray = new JSONArray();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", id);
                    jsonObject.put("qty", txt_quan.getText().toString());
                    jsonObject.put("user_order_status", user_order_status);

                    jsonArray.put(jsonObject);
                    new UpdateItem(OrderScreen.this, jsonArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void dialogDel(final String id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_del);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.x = -50;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);


        LinearLayout ll_ok = (LinearLayout) dialog.findViewById(R.id.ll_ok);
        ll_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new DeleteItem(OrderScreen.this, id);


            }
        });

        LinearLayout ll_cancel = (LinearLayout) dialog.findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();


            }
        });


        dialog.show();

    }


    public void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/ordersapi/view_all_order_items";
            new OrderItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(OrderScreen.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
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

    public class OrderItems extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public OrderItems(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(OrderScreen.this);
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
                    OrderEvent objs = gson.fromJson(reader, OrderEvent.class);
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
                    items_placed = new ArrayList<>();
                    for (OrderJson or : objs.getOrderitems()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String id = or.getId();
                        String name = or.getName();
                        String price = or.getPrice();
                        String qty = or.getQty();
                        String user_order_status = or.getUser_order_status();
                        Log.i("MY INFO", name);

                        hashMap.put("item_id", id);
                        hashMap.put("item_name", name);
                        hashMap.put("item_price", price);
                        hashMap.put("item_qty", qty);
                        hashMap.put("user_order_status", user_order_status);

                        if (user_order_status.equals("p")) {
                            HasItems();
                            items.add(hashMap);
                            items_placed.add(hashMap);
                        } else {
                            NoItems();
                        }

                    }

                    setItems(items);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(OrderScreen.this, t_alert, m_alert);
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
    @Override
    public void onBackPressed() {
        // Write your code here
        Intent intent = new Intent(OrderScreen.this,MenuItem.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
}
