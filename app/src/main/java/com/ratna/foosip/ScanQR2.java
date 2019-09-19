package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanQR2 extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    SavedParameter savedParameter;

    UserSession userSession;

    Toolbar toolbar;
    LinearLayout ll_scan2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanqr_2);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Scan QR");
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        mScannerView = new ZXingScannerView(this);
        savedParameter = new SavedParameter(this);
        savedParameter.setMain_user(false);
        savedParameter.setSubUser(false);


        userSession = new UserSession(this);

        ll_scan2 = findViewById(R.id.ll_scan2);

        /*boolean flag = userSession.getQR(this);
        if (flag) {
            Intent intent = new Intent(ScanQR.this, HomeChat.class);
            startActivity(intent);
            finish();
        }*/

        setView();
    }

    public void setView() {

//
//        LinearLayout ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
//        ll_scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });


    }

    public void QrScanner(View view) {
        // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }

    @Override
    protected void onResume() {
        super.onResume();
        QrScanner(ll_scan2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        Log.e("", rawResult.getText()); // Prints scan results
        Log.e("", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.
//        Intent intent = new Intent(ScanQR.this, MenuItem.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("qr_code", rawResult.getText());
//        intent.putExtras(bundle);
//        startActivity(intent);
//        finish();
        savedParameter.setQrCode(rawResult.getText());

        CallApi();

    }


//    private void CallApi() {
//
//        ConnectionDetector connectionDetector = new ConnectionDetector(this);
//        boolean isInternet = connectionDetector.isConnectingToInternet();
//
//        if (isInternet) {
//            String url = "http://foosip.com/usersapi/book_table";
//            new MenuItems(url).execute();
//        } else {
//            AlertClassRetry alert = new AlertClassRetry();
//            String t_alert = getResources().getString(R.string.error);
//            String m_alert = getResources().getString(R.string.no_internet);
//            alert.showAlert(ScanQR.this, t_alert, m_alert);
//        }
//    }
//
//
//    public class MenuItems extends AsyncTask<Void, Void, String> {
//
//        int response_code;
//        String url;
//        ProgressDialog progressDialog;
//
//        public MenuItems(String url) {
//            this.url = url;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            progressDialog = new ProgressDialog(ScanQR.this);
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
//
//                try {
//                    JSONObject object = new JSONObject(result);
//
//                    String message = object.getString("message");
//
//                    if (message.equals("first"))
//                    {
//
//                        String rid = object.getString("rid");
//                        savedParameter.setrId(rid);
//                        String toid = object.getString("temp_order_id");
//                        savedParameter.setTempOrderId(toid);
//
//                        userSession.setQR(true);
//
//
//
//                        /*Toast.makeText(ScanQR.this, result.toString(), Toast.LENGTH_SHORT).show();
//                        Intent mainIntent = new Intent(ScanQR.this, HomeChat.class);
//                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(mainIntent);*/
//                        finish();
//
//                    }
//                    else if (message.equals("Table already in use"))
//                    {
//                        Toast.makeText(ScanQR.this , message , Toast.LENGTH_SHORT).show();
//                    }
//                    else if (message.equals("Invalid QRcode"))
//                    {
//                        Toast.makeText(ScanQR.this , message , Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(ScanQR.this , message , Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            } else {
//                AlertClassRetry alert = new AlertClassRetry();
//                String t_alert = getResources().getString(R.string.error);
//                String m_alert = getResources().getString(R.string.server_error);
//                alert.showAlert(ScanQR.this, t_alert, m_alert);
//            }
//        }
//    }
//
//
//    private String doPost(String url) throws IOException {
//        MediaType JSON
//                = MediaType.parse("application/json; charset=utf-8");
//        OkHttpClient client = new OkHttpClient();
//        JSONObject jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("qr_code", savedParameter.getQrCode());
//            Log.i("json_format", jsonObject.toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
//        Request request = new Request.Builder()
//                .header("Authorization", savedParameter.getTOKEN())
//                .url(url)
//                .post(body)
//                .build();
//
//
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }




    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/menu_scan";
            new MenuItems(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(ScanQR2.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("qr_code", savedParameter.getQrCode());
//            jsonObject.put("rid", savedParameter.getQrCode());
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

            progressDialog = new ProgressDialog(ScanQR2.this);
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
                    userSession.setQR(true);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.getString("message");
                    if(message.equals("success"))
                    {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String rid = jsonObject1.getString("rid");
                        String qrcode = jsonObject1.getString("qr_code");
                        savedParameter.setrId(rid);
//                        Toast.makeText(ScanQR2.this, result.toString(), Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(ScanQR2.this, HomeChat.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();

                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                }

            } else {
               AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(ScanQR2.this, t_alert, m_alert);
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

