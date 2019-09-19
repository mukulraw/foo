package com.ratna.foosip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import SharedPreferences.SavedParameter;
import Utils.BlurImage;

/**
 * Created by ratna on 1/5/2017.
 */
public class ProfileEdit extends Activity {

    EditText txt_name, txt_mob, txt_email;
    SavedParameter savedParameter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit);

        savedParameter = new SavedParameter(this);

        txt_name = (EditText) findViewById(R.id.ed_name);
        txt_mob = (EditText) findViewById(R.id.ed_mob);

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

                new ProfileGet(ProfileEdit.this,txt_name.getText().toString(),txt_mob.getText().toString());

            }
        });


    }
}
