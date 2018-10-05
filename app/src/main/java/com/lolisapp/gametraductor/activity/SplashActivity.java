package com.lolisapp.gametraductor.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.lolisapp.gametraductor.R;
import com.lolisapp.gametraductor.activity.LoginActivity;
import com.lolisapp.gametraductor.activity.MainActivity;
import com.lolisapp.gametraductor.util.session.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                finish();


                if (SessionManager.getInstance(SplashActivity.this).isLogged()){

                    Intent mainIntent = new Intent().setClass(
                            SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }else{

                    Intent mainIntent = new Intent().setClass(
                            SplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                }


                // Close the activity so the user won't able to go back this
                // activity pressing Back button
            }
        };

        // Simulate a long loading process on application startup.
        Timer timer = new Timer();
        timer.schedule(task, 4000);
    }

}
