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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Events.OrderEvent;
import Model.Item;
import Model.OrderJson;
import SharedPreferences.SavedParameter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 9/15/2016.
 */
public class Feedback extends Activity {

    SavedParameter savedParameter;
    RatingBar ratingBar1,ratingBar2;
    float netRate = 0f;
    EditText ed_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_feedback);

        savedParameter = new SavedParameter(this);
        ratingBar1 = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar)findViewById(R.id.ratingBar2);
        ed_feedback = (EditText)findViewById(R.id.ed_feedback);

        netRate = (ratingBar1.getRating()+ratingBar2.getRating())/2;


        setView();


    }

    private void setView() {

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        LinearLayout ll_submit = (LinearLayout) findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CallApi();

            }
        });
    }

    public void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/ordersapi/feedback_rating";
            new SendFeedback(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(Feedback.this, t_alert, m_alert);
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
            jsonObject.put("net_rate",netRate);
            jsonObject.put("feedback", ed_feedback.getText().toString());
            jsonObject.put("service_rating", String.valueOf(ratingBar1.getRating()));
            jsonObject.put("food_rating", String.valueOf(ratingBar2.getRating()));
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

    public class SendFeedback extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public SendFeedback(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Feedback.this);
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

//            Toast.makeText(Feedback.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String mssg = jsonObject.getString("message");
                      if(mssg.equals("feedback updated"))
                      {
                          Intent intent = new Intent(Feedback.this,CheckOutSuccess.class);
                          startActivity(intent);
                      }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(Feedback.this, t_alert, m_alert);
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
