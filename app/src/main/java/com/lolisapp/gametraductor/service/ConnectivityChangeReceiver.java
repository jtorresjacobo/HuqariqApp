package com.lolisapp.gametraductor.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lolisapp.gametraductor.asynctask.SyncronizeData;


public class ConnectivityChangeReceiver extends BroadcastReceiver {
    Context context;


    @Override
    public void onReceive(Context context, Intent intent) {

        // Explicitly specify that which service class will handle the intent.
      //  Bundle extras = intent.getExtras();
        //Log.d("test","exsit key"+extras.containsKey("isNetworkConnected"));
        //boolean isNetworkConnected = extras.getBoolean("isNetworkConnected");
        this.context=context;

        SyncronizeData internetCheck=new SyncronizeData(context);




        //startService(context, (intent.setComponent(comp)));
    }





}




