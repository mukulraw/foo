package com.ratna.foosip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by ratna on 9/22/2016.
 */
public class Welcome extends Activity {

    // splash screen timer
    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                Intent i = new Intent(Welcome.this, ScanQR.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}
