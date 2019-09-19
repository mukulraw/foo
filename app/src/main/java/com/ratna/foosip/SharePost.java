package com.ratna.foosip;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SharedPreferences.SavedParameter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SharePost extends AppCompatActivity {

    Toolbar toolbar;
    Button post;

    EditText comment;
    ProgressBar progress;

    String type;
    SavedParameter savedParameter;

    TextView text;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        type = getIntent().getStringExtra("type");

        savedParameter = new SavedParameter(this);




        toolbar = findViewById(R.id.toolbar4);
        post = findViewById(R.id.button3);

        comment = findViewById(R.id.editText7);
        progress = findViewById(R.id.progressBar7);
        text = findViewById(R.id.textView56);


        if (type.equals("ask"))
        {
            text.setText("I want to ask for a Recommendation");
        }
        else
        {
            text.setText("I want to share a Recommendation");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Share");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });






        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String c = comment.getText().toString();

                if (c.length() > 0)
                {

                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://foosip.com/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Log.d("userid" , savedParameter.getUID());
                    Log.d("rid" , savedParameter.getRID());
                    Log.d("type" , type);
                    Log.d("message" , c);


                    Call<String> call = cr.postText(savedParameter.getUID() , savedParameter.getRID() , type , c);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.body().equals("success"))
                            {
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SharePost.this , "Error uploading file" , Toast.LENGTH_SHORT).show();
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });



                }


            }
        });



    }
}
