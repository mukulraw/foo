package com.ratna.foosip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by ratna on 9/23/2016.
 */
public class AskMore extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ask_for);

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

        LinearLayout ll_check_out = (LinearLayout) findViewById(R.id.ll_check_out);
        ll_check_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AskMore.this, CheckOut.class);
                startActivity(intent);
            }
        });
    }

}
