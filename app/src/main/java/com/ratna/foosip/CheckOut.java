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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Adapter.RecycleViewAdapterCheckOut;
import Adapter.RecyclerViewAdapterOrder;
import Events.BillEvent;
import Events.OrderEvent;
import Model.Item;
import Model.OrderJson;
import Model.Tax;
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
public class CheckOut extends Activity {


    RecyclerView.Adapter mAdapter_items;  // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView_items;
    SavedParameter savedParameter;
    ArrayList<HashMap<String, String>> items_placed;
    TextView txt_no_items;
    float total_price = 0f,total_food=0f,total_alcohol=0f;
    TextView txt_total, txt_ser_chrg, txt_ser_tax, txt_swach_cess, txt_kishi_cess, txt_vat_food,txt_vat_al, txt_grand_total;
    TextView txt_ser_chrg_rate, txt_ser_tax_rate, txt_swach_cess_rate, txt_kishi_cess_rate, txt_vat_rate,txt_vat_al_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);

        savedParameter = new SavedParameter(this);
        mRecyclerView_items = (RecyclerView) findViewById(R.id.RecyclerView_items);
        txt_no_items = (TextView) findViewById(R.id.txt_no_items);

        txt_total = (TextView) findViewById(R.id.txt_total);

        txt_ser_chrg = (TextView) findViewById(R.id.txt_ser_chrg);
        txt_ser_tax = (TextView) findViewById(R.id.txt_ser_tax);
        txt_swach_cess = (TextView) findViewById(R.id.txt_swach_cess);
        txt_kishi_cess = (TextView) findViewById(R.id.txt_kishi_cess);
        txt_vat_food = (TextView) findViewById(R.id.txt_vat);
        txt_vat_al = (TextView) findViewById(R.id.txt_vat_alcohol);

        txt_grand_total = (TextView) findViewById(R.id.txt_grand_total);


        txt_ser_chrg_rate = (TextView) findViewById(R.id.txt_ser_chrg_rate);
        txt_ser_tax_rate = (TextView) findViewById(R.id.txt_ser_tax_rate);
        txt_swach_cess_rate = (TextView) findViewById(R.id.txt_swach_cess_rate);
        txt_kishi_cess_rate = (TextView) findViewById(R.id.txt_kirishi_cess_rate);
        txt_vat_rate = (TextView) findViewById(R.id.txt_vat_rate);
        txt_vat_al_rate = (TextView)findViewById(R.id.txt_vat_al_rate);

        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setView() {

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        CallApi();

        LinearLayout ll_checkout = (LinearLayout) findViewById(R.id.ll_checkout);
        ll_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intent = new Intent(CheckOut.this,Feedback.class);
                startActivity(intent);

            }
        });

    }

    public void GetTotal( float total_food,float total_alcohol,float total, String ser_chrg, String ser_tax, String swach_cess,
                         String kirichi_cess, String vat_alcohol, String vat_food, String item_type) {

        txt_total.setText("Rs. " + String.valueOf(total));

        txt_ser_chrg.setText("Service Charge " + ser_chrg + " %");
        txt_ser_chrg_rate.setText(String.valueOf(getPercentage(total, Float.parseFloat(ser_chrg))));

        txt_ser_tax.setText("Service Tax " + ser_tax + " %");
        txt_ser_tax_rate.setText(String.valueOf(getPercentage(total, Float.parseFloat(ser_tax))));

        txt_swach_cess.setText("Swach Cess " + swach_cess + " %");
        txt_swach_cess_rate.setText(String.valueOf(getPercentage(total, Float.parseFloat(swach_cess))));

        txt_kishi_cess.setText("Kirishi Cess " + kirichi_cess + " %");
        txt_kishi_cess_rate.setText(String.valueOf(getPercentage(total, Float.parseFloat(kirichi_cess))));


            float vat_al=0f,vat_fd=0f;

            txt_vat_al.setText("Vat Alcohol " + vat_alcohol + " %");
            txt_vat_al_rate.setText(String.valueOf(getPercentage(total_alcohol, Float.parseFloat(vat_alcohol))));
            vat_al = getPercentage(total_alcohol, Float.parseFloat(vat_alcohol));

            txt_vat_food.setText("Vat Food " + vat_food + " %");
            txt_vat_rate.setText(String.valueOf(getPercentage(total_food, Float.parseFloat(vat_food))));
            vat_fd = getPercentage(total_food, Float.parseFloat(vat_food));


        txt_grand_total.setText(String.valueOf(total+
               (getPercentage(total, Float.parseFloat(ser_chrg)))+
               (getPercentage(total, Float.parseFloat(ser_tax)))+
               (getPercentage(total, Float.parseFloat(swach_cess)))+
               (getPercentage(total, Float.parseFloat(kirichi_cess)))+
                vat_al+
                vat_fd));

        Tax tax = new Tax();
        tax.setService_charge(txt_ser_chrg.getText().toString());
        tax.setService_tax(txt_ser_tax.getText().toString());
        tax.setSwach_cess(txt_swach_cess.getText().toString());
        tax.setKirishi_cess(txt_kishi_cess.getText().toString());
        tax.setVat_food(txt_vat_food.getText().toString());
        tax.setVat_alcohol(txt_vat_al.getText().toString());
        tax.setAmount(txt_grand_total.getText().toString());

        savedParameter.setTaxObj(tax);




    }


    private Float getPercentage(Float total, Float chrg) {
        float f = total * (chrg / 100);
        return f;
    }


    private void setItems(ArrayList<HashMap<String, String>> items) {

        List<String> arrayList = new ArrayList<>();
        ArrayList<HashMap<String, String>> items2=new ArrayList<>();

        for(int i=0;i<items.size();i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap = items.get(i);
            arrayList.add(hashMap.get("item_name"));


        }

        Set<String> uniqueSet = new HashSet<String>(arrayList);
        for (String temp : uniqueSet) {
//            String st = temp + ": " + Collections.frequency(arrayList, temp);
//            String qty = String.valueOf(Collections.frequency(arrayList, temp));
            int quan=0;
            for(int j=0;j<items.size();j++) {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap = items.get(j);

                if (hashMap.get("item_name").equals(temp)) {
                    quan = quan + Integer.parseInt(hashMap.get("item_qty"));

                }
            }
            for(int i=0;i<items.size();i++) {
                HashMap<String, String> hashMap1 = new HashMap<>();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap = items.get(i);

                if(hashMap.get("item_name").equals(temp))
                {


                    hashMap1.put("item_name",hashMap.get("item_name"));
                    hashMap1.put("item_price",hashMap.get("item_price"));
                    hashMap1.put("item_qty", String.valueOf(quan));

                    items2.add(hashMap1);
                    break;

                }

                //Toast.makeText(ViewLastBill.this, st, Toast.LENGTH_LONG).show();
            }


        }





        mRecyclerView_items.setHasFixedSize(true);

        mAdapter_items = new RecycleViewAdapterCheckOut(this, items2);
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
            String url = "http://foosip.com/ordersapi/view_bill";
            new OrderItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(CheckOut.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("temp_order_id", savedParameter.getTempOrderId(this));
            jsonObject.put("rid", savedParameter.getrId(this));
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

            progressDialog = new ProgressDialog(CheckOut.this);
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
                    BillEvent objs = gson.fromJson(reader, BillEvent.class);

                    String service_charge = objs.getService_charge();
                    String service_tax = objs.getService_tax();
                    String swach_cess = objs.getSwach_cess();
                    String kirishi_cess = objs.getKirishi_cess();
                    String vat_alcohol = objs.getVat_alcohol();
                    String vat_food = objs.getVat_food();
                    String item_type = "";
                    ArrayList<HashMap<String, String>> items = new ArrayList<>();
                    items_placed = new ArrayList<>();
                    for (OrderJson or : objs.getOrderitems()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String id = or.getId();
                        String name = or.getName();
                        item_type = or.getItem_type();
                        String price = or.getPrice();
                        String qty = or.getQty();
                        String user_order_status = or.getUser_order_status();
                        Log.i("MY INFO", name);

                        hashMap.put("service_charge", service_charge);
                        hashMap.put("service_tax", service_tax);
                        hashMap.put("swach_cess", swach_cess);
                        hashMap.put("kirishi_cess", kirishi_cess);
                        hashMap.put("vat_alcohol", vat_alcohol);
                        hashMap.put("vat_food", vat_food);

                        hashMap.put("item_id", id);
                        hashMap.put("item_name", name);
                        hashMap.put("item_type", item_type);
                        hashMap.put("item_price", price);
                        hashMap.put("item_qty", qty);
                        hashMap.put("user_order_status", user_order_status);


                        items.add(hashMap);
                        items_placed.add(hashMap);

                        float quan = Float.parseFloat(qty);
                        float item_price = Float.parseFloat(price);
                        total_price = total_price + quan * item_price;

                        if(item_type.equals("a"))
                        {
                            total_alcohol = total_alcohol + quan*item_price;
                        }else{

                            total_food = total_food + quan*item_price;
                        }

                    }

                    setItems(items);
                    GetTotal(total_food,total_alcohol,total_price, service_charge, service_tax, swach_cess, kirishi_cess, vat_alcohol, vat_food, item_type);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(CheckOut.this, t_alert, m_alert);
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
