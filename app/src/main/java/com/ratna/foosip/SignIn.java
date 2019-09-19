package com.ratna.foosip;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import Utils.GetUserProfile;
import androidx.annotation.NonNull;
import app.Config;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 10/2/2016.
 */
public class SignIn extends Activity {

    SavedParameter savedParameter;
    UserSession userSession;
    private EditText ed_email, ed_pass;
    private String email, pass;
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    SharedPreferences fcmPref;
    SharedPreferences.Editor fcmEdit;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_log_in);




        fcmPref = getSharedPreferences(Config.SHARED_PREF , Context.MODE_PRIVATE);
        fcmEdit = fcmPref.edit();

        try {

            String tok = FirebaseInstanceId.getInstance().getToken();

            Log.d("token", tok);

            fcmEdit.putString("token" , tok);

            fcmEdit.apply();



        } catch (Exception e) {

            new Thread() {
                @Override
                public void run() {
                    //If there are stories, add them to the table
                    //try {
                    // code runs in a thread
                    //runOnUiThread(new Runnable() {
                    //  @Override
                    //public void run() {
                    new MyFirebaseInstanceIDService().onTokenRefresh();






                    //}
                    //});
                    //} catch (final Exception ignored) {
                    //}
                }
            }.start();

            e.printStackTrace();
        }

        savedParameter = new SavedParameter(SignIn.this);
        userSession = new UserSession(this);

        boolean flag = userSession.getUserLogIn(this);
        if (flag) {
            Intent intent = new Intent(SignIn.this, HomeChat.class);
            startActivity(intent);
            finish();
        }


        mAuth = FirebaseAuth.getInstance();


        mLoginProgress = new ProgressDialog(this);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        setView();
    }

    public void setView() {

        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_pass = (EditText) findViewById(R.id.ed_pass);
        TextView txt_sign_up = (TextView) findViewById(R.id.txt_sign_up);
        TextView txt_forgot_pass = (TextView) findViewById(R.id.txt_forgot_password);

        SpannableString click_here = new SpannableString("For Sign Up  Click Here");
        click_here.setSpan(new UnderlineSpan(), 13, 23, 0);
        txt_sign_up.setText(click_here);

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignIn.this, EmailSignUp.class);
                startActivity(intent);

            }
        });


        SpannableString forgot_pass = new SpannableString("Forgot Password");
        forgot_pass.setSpan(new UnderlineSpan(), 0, forgot_pass.length(), 0);
        txt_forgot_pass.setText(forgot_pass);

        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignIn.this, ForgotPassword.class);
                startActivity(intent);
                finish();

            }
        });


        LinearLayout ll_next = (LinearLayout) findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = ed_email.getText().toString();
                boolean valid = isValidEmail(email);
                pass = ed_pass.getText().toString();
                if (valid) {
                    CallApi();

                } else {
                    AlertClassRetry alert = new AlertClassRetry();
                    String t_alert = getResources().getString(R.string.error);
                    String m_alert = "Required all fields!!!";
                    alert.showAlert(SignIn.this, t_alert, m_alert);
                }


            }
        });
    }


    private void loginUser(String email, String password) {

        mLoginProgress.setMessage("Please Wait...");
        mLoginProgress.setCancelable(false);
        mLoginProgress.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    String current_user_id = mAuth.getCurrentUser().getUid();
                    String deviceToken = FirebaseInstanceId.getInstance().getToken();

//
                    mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {


                            mLoginProgress.dismiss();
                            boolean flag = userSession.getUserSignUp(SignIn.this);
                            if (flag) {

                                Intent mainIntent = new Intent(SignIn.this, SettingsActivity.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }else{
                                Intent mainIntent = new Intent(SignIn.this, HomeChat.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();
                            }


                        }
                    });


                } else {

                    mLoginProgress.hide();

                    String task_result = task.getException().getMessage().toString();

                    Toast.makeText(SignIn.this, "Error : " + task_result, Toast.LENGTH_LONG).show();

                }

            }
        });


    }


    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/login";
            new PasswordApi(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(SignIn.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(app.Config.SHARED_PREF, 0);
            String regId = pref.getString("token", null);
            jsonObject.put("email", email);
            jsonObject.put("password", pass);
            jsonObject.put("fcm_reg_id",regId);
            GetUserProfile getUserProfile = new GetUserProfile(this);
            jsonObject.put("device_id",getUserProfile.getDeviceId());
            Log.i("json_format", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();


        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public class PasswordApi extends AsyncTask<Void, Void, String> {

        int response_code;
        String url;
        ProgressDialog progressDialog;

        public PasswordApi(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SignIn.this);
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
//                    Toast.makeText(Home.this, result, Toast.LENGTH_LONG).show();
                    Log.i("MY INFO", "Json Parser started..");
                    JSONObject jsonObject = new JSONObject(result);

//                    String status = jsonObject.getString("status");

//                    if (status.equals("1"))
//                    {



                        String flag = jsonObject.getString("flag");
                        String mssg = jsonObject.getString("token");
                        String uid = jsonObject.getString("uid");




                        if (!mssg.equals("")) {

                            if (flag.equals("2"))
                            {
                                userSession.setUserLogIn(true);

                                savedParameter.setTOKEN("Bearer " + mssg);
                                savedParameter.setUID(uid);

                                Log.d("Bearer" , "Bearer " + mssg);

                                loginUser(email, pass);
                            }
                            else if (flag.equals("0"))
                            {


                                Toast.makeText(SignIn.this , "Please complete your signup" , Toast.LENGTH_SHORT).show();
                                String em = jsonObject.getString("email");

                                savedParameter.setEMAIL(em);

                                Intent intent = new Intent(SignIn.this, MobileSignUp.class);
                                startActivity(intent);



                            }
                            else if (flag.equals("1"))
                            {
                                Toast.makeText(SignIn.this , "Please complete your signup" , Toast.LENGTH_SHORT).show();
                                String em = jsonObject.getString("email");

                                savedParameter.setEMAIL(em);

                                Intent intent = new Intent(SignIn.this, SetPassword.class);
                                startActivity(intent);
                            }



//                        Intent intent = new Intent(SignIn.this, Home.class);
//                        startActivity(intent);
//                        finishAffinity();


                        }
//                    }
                    else
                    {
                        Toast.makeText(SignIn.this , "Invalid Login or User doesn't exist" , Toast.LENGTH_SHORT).show();
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(SignIn.this, t_alert, m_alert);
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
