package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileEditCall2 {
    Activity activity;
    SavedParameter savedParameter;
    private String email,name,interest,first_name,last_name,profile_pic,profession,dob;

    UserSession userSession;

    public ProfileEditCall2(Activity activity,String email, String name,String interest,String first_name,String last_name, String profile_pic,String profession,String dob) {
        this.activity = activity;
        this.email = email;
        this.name = name;
        this.interest = interest;
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_pic = profile_pic;
        this.profession = profession;
        this.dob = dob;
        savedParameter = new SavedParameter(activity);
        userSession = new UserSession(activity);
        CallApi();

    }

    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(activity);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/editprofile";
            new ProfileEditCall2.getProfile(url).execute();
        } else {
            ProfileEditCall2.AlertClassRetry alert = new ProfileEditCall2.AlertClassRetry();
            String t_alert = activity.getResources().getString(R.string.error);
            String m_alert = activity.getResources().getString(R.string.no_internet);
            alert.showAlert(activity, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email",email);
            jsonObject.put("name",name);
            jsonObject.put("interest",interest);
            jsonObject.put("first_name",first_name);
            jsonObject.put("last_name",last_name);
            jsonObject.put("profile_pic",profile_pic);
            jsonObject.put("profession",profession);
            jsonObject.put("dob",dob);
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

            progressDialog = new ProgressDialog(activity);
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

//            Toast.makeText(activity, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {


                try {

                        userSession.setPROFILE(true);
                        Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show();
                        Intent mainIntent = new Intent(activity, HomeChat.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(mainIntent);
                        activity.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ProfileEditCall2.AlertClassRetry alert = new ProfileEditCall2.AlertClassRetry();
                String t_alert = activity.getResources().getString(R.string.error);
                String m_alert = activity.getResources().getString(R.string.server_error);
                alert.showAlert(activity, t_alert, m_alert);
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


