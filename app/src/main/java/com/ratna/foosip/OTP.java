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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import SharedPreferences.SavedParameter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 10/2/2016.
 */
public class OTP extends Activity {

    SavedParameter savedParameter;
    private EditText ed_OTP;
    private String OTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_verify_email_otp);

        setView();
    }

    public void setView() {

        savedParameter = new SavedParameter(OTP.this);
        ed_OTP = (EditText) findViewById(R.id.ed_otp);

        LinearLayout ll_next = (LinearLayout) findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                OTP = ed_OTP.getText().toString();
                CallApi();


            }
        });
    }


    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/verifymobile";
            new OTP.VerifyOTP(url).execute();
        } else {
            OTP.AlertClassRetry alert = new OTP.AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(OTP.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", savedParameter.getEMAIL(OTP.this));
            jsonObject.put("mobile", savedParameter.getMobile(OTP.this));
            jsonObject.put("otp", OTP);
            Log.i("json_format", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
//                .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJyaWQiOiI1IiwidHlwZSI6Im0iLCJpYXQiOjE0NzUxMzQ5ODcsImV4cCI6MTQ3NTM1MDk4Nywic2VsX3R5cGUiOiJtIn0.2acwVK25WGremtjyA6PUiTMAydRJx5T3FxrazoERsqE")
                .url(url)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }


    public class VerifyOTP extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public VerifyOTP(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(OTP.this);
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

//            Toast.makeText(OTP.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
//                    Toast.makeText(Home.this, result, Toast.LENGTH_LONG).show();
                    Log.i("MY INFO", "Json Parser started..");
                    JSONObject jsonObject = new JSONObject(result);
                    String mssg = jsonObject.getString("message");

                    if (mssg.equals("Mobile Verified")) {

                        Intent intent = new Intent(OTP.this, SetPassword.class);
                        startActivity(intent);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                OTP.AlertClassRetry alert = new OTP.AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(OTP.this, t_alert, m_alert);
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
                    //activity.finish();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
    }

}


