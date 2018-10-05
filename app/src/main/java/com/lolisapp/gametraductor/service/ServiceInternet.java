package com.lolisapp.gametraductor.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by USUARIO on 15/07/2018.
 */

public class ServiceInternet extends IntentService {

    public ServiceInternet(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        // your code

    }

}