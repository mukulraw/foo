package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import Events.ProfileEvent;
import Model.ProfileJson;
import SharedPreferences.SavedParameter;
import Utils.BlurImage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ratna on 9/15/2016.
 */
public class Profile extends Activity {

    TextView txt_name,txt_mob,txt_email,txt_change_pass;
    SavedParameter  savedParameter;
    ImageView img_edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);

        savedParameter = new SavedParameter(this);

        txt_name = (TextView)findViewById(R.id.txt_name);
        txt_mob = (TextView)findViewById(R.id.txt_mob);
        txt_email = (TextView)findViewById(R.id.txt_email);
        txt_change_pass = (TextView)findViewById(R.id.txt_sign_up);
        img_edit = (ImageView)findViewById(R.id.img_edit);

        setView();


    }

    private void setView() {


        SpannableString click_here = new SpannableString("Change Password");
        click_here.setSpan(new UnderlineSpan(), 0,click_here.length(), 0);
        txt_change_pass.setText(click_here);

        txt_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this, ChangePassword.class);
                startActivity(intent);

            }
        });

        LinearLayout ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        Bitmap input_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_dinner);
        Bitmap bitmap = BlurImage.blur(this, input_bitmap);

        ImageView img = (ImageView) findViewById(R.id.img_blurr);
        img.setImageBitmap(bitmap);


        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Profile.this,ProfileEdit.class);
                startActivity(intent);

            }
        });

        CallApi();
    }

    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/profile";
            new getProfile(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", savedParameter.getTOKEN())
                .url(url)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public class getProfile extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public getProfile(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(Profile.this);
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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject or = jsonObject.getJSONObject("profile");


                        String name = or.getString("name");
                        String mobile = or.getString("mobile");
                        String email = or.getString("email");
                        String id = or.getString("id");
                        Log.i("MY INFO", name);

                        txt_name.setText(name);
                        txt_mob.setText(mobile);
                        txt_email.setText(email);




                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(Profile.this, t_alert, m_alert);
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
