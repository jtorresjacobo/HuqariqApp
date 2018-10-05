package com.lolisapp.gametraductor.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.lolisapp.gametraductor.service.AsyncLocal;
import com.lolisapp.gametraductor.util.Util;



public class SyncronizeData extends AsyncTask<Void,Void,Boolean> {

    Context context;


    public SyncronizeData(Context context) {
        this.context=context;
        execute();
    }

    @Override protected Boolean doInBackground(Void... voids) {
        Log.d("inic","init async");
        AsyncLocal asyncLocal=new AsyncLocal(context);
        asyncLocal.syncronizeData();
        return true;

    }

    @Override protected void onPostExecute(Boolean internet) {
        Log.d("inic","finish async");
    }
}

///////////////////////////////////////////////////////////////////////////////////
// Usage

