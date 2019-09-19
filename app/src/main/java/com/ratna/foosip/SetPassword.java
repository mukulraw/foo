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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import androidx.annotation.NonNull;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ratna on 10/15/2016.
 */
public class SetPassword extends Activity {

    SavedParameter savedParameter;
    FirebaseAuth mAuth;
    private EditText ed_email, ed_pass, ed_confirm_pass;
    private String email, pass, confirm_pass;
    private DatabaseReference mDatabase;
    //ProgressDialog
    private ProgressDialog mRegProgress;
    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_set_password);


        // Firebase Auth
        userSession = new UserSession(this);
        mAuth = FirebaseAuth.getInstance();

        mRegProgress = new ProgressDialog(this);

        setView();
    }

    public void setView() {

        savedParameter = new SavedParameter(SetPassword.this);
        ed_email = (EditText) findViewById(R.id.ed_pass);
        ed_pass = (EditText) findViewById(R.id.ed_password);
        ed_confirm_pass = (EditText) findViewById(R.id.ed_confirm_password);

        ed_email.setText(savedParameter.getEMAIL(SetPassword.this));

        LinearLayout ll_next = (LinearLayout) findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = ed_email.getText().toString();
                boolean valid = isValidEmail(email);
                pass = ed_pass.getText().toString();
                confirm_pass = ed_confirm_pass.getText().toString();
                if (valid && !pass.equals("") && !confirm_pass.equals("")) {
                    if (pass.equals(confirm_pass)) {
                        register_user(savedParameter.getUSERNAME(SetPassword.this), email, confirm_pass);

                    } else {
                        AlertClassRetry alert = new AlertClassRetry();
                        String t_alert = getResources().getString(R.string.error);
                        String m_alert = "Check Password";
                        alert.showAlert(SetPassword.this, t_alert, m_alert);
                    }
                } else {
                    AlertClassRetry alert = new AlertClassRetry();
                    String t_alert = getResources().getString(R.string.error);
                    String m_alert = "Required all fields!!!";
                    alert.showAlert(SetPassword.this, t_alert, m_alert);
                }


            }
        });


    }


    private void register_user(final String display_name, String email, String password) {

        mRegProgress.setMessage("Please Wait...");
        mRegProgress.setCancelable(false);
        mRegProgress.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {



                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", display_name);
                    userMap.put("status", "Hi there I'm using Foosip Chat App.");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");
                    userMap.put("device_token", device_token);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                mRegProgress.dismiss();
                                CallApi();



                            }

                        }
                    });


                } else {

                    mRegProgress.hide();
                    Toast.makeText(SetPassword.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                }

            }

        });

    }

    private void CallApi() {

        ConnectionDetector connectionDetector = new ConnectionDetector(this);
        boolean isInternet = connectionDetector.isConnectingToInternet();

        if (isInternet) {
            String url = "http://foosip.com/usersapi/password";
            new PasswordApi(url).execute();
        } else {
            AlertClassRetry alert = new AlertClassRetry();
            String t_alert = getResources().getString(R.string.error);
            String m_alert = getResources().getString(R.string.no_internet);
            alert.showAlert(SetPassword.this, t_alert, m_alert);
        }
    }

    private String doPost(String url) throws IOException {
        MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", email);
            jsonObject.put("password", confirm_pass);
            if(!mAuth.getCurrentUser().getUid().equals(null)) {
                jsonObject.put("uid", mAuth.getCurrentUser().getUid());
            }
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

            progressDialog = new ProgressDialog(SetPassword.this);
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

//            Toast.makeText(SetPassword.this, result.toString(), Toast.LENGTH_SHORT).show();


            if (result != null && !result.equals("")) {

                try {
//                    Toast.makeText(Home.this, result, Toast.LENGTH_LONG).show();
                    Log.i("MY INFO", "Json Parser started..");
                    JSONObject jsonObject = new JSONObject(result);
                    String mssg = jsonObject.getString("message");

                    if (mssg.equals("Password Saved")) {

                        savedParameter.setPASSWORD(confirm_pass);
                        userSession.setUserSignUp(true);
                        Intent mainIntent = new Intent(SetPassword.this, SignIn.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                        finish();


//                        Intent intent = new Intent(SetPassword.this, SignIn.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                AlertClassRetry alert = new AlertClassRetry();
                String t_alert = getResources().getString(R.string.error);
                String m_alert = getResources().getString(R.string.server_error);
                alert.showAlert(SetPassword.this, t_alert, m_alert);
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
