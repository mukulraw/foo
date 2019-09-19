package com.ratna.foosip;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.statusRequestBean;

import java.util.HashMap;
import java.util.Map;

import SharedPreferences.SavedParameter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StatusActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private TextInputLayout mStatus;
    private Button mSavebtn;



    ProgressBar progress;
    private SavedParameter savedParameter;

    //Progress


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Firebase
        savedParameter = new SavedParameter(StatusActivity.this);

        mToolbar = (Toolbar) findViewById(R.id.status_appBar);
        progress = (ProgressBar)findViewById(R.id.progress);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");

        mStatus = (TextInputLayout) findViewById(R.id.status_input);
        mSavebtn = (Button) findViewById(R.id.status_save_btn);

        mStatus.getEditText().setText(status_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Progress

                String status = mStatus.getEditText().getText().toString();


                progress.setVisibility(View.VISIBLE);

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://foosip.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                statusRequestBean body = new statusRequestBean();

                body.setId(savedParameter.getUID());
                body.setProfilePic(status);

                Map<String, String> map = new HashMap<>();

                map.put("Content-Type" , "application/json");
                map.put("Authorization" , savedParameter.getTOKEN());

                Call<profileBean> call = cr.updateStatus(body , map);


                call.enqueue(new Callback<profileBean>() {
                    @Override
                    public void onResponse(Call<profileBean> call, Response<profileBean> response) {


                        //if (response.body().getMessage().equals("1"))
                        //{
                            progress.setVisibility(View.GONE);
                            Toast.makeText(StatusActivity.this , "Status updated successfully" , Toast.LENGTH_SHORT).show();
                            finish();
                        //}


                    }

                    @Override
                    public void onFailure(Call<profileBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                    }
                });



            }
        });



    }
}
