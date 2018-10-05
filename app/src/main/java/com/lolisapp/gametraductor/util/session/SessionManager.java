package com.lolisapp.gametraductor.util.session;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.lolisapp.gametraductor.activity.LoginActivity;
import com.lolisapp.gametraductor.bean.User;
import com.lolisapp.gametraductor.util.Constants;


/**
 * Created by Carlos on 04/08/2015.
 */
public class SessionManager {

    private static final String TAG = "SessionManager";
    private static SessionManager _sessionManager;
    private Context _applicationContext;
    private SharedPreferences _pref;

    private SessionManager(Context applicationContext) {
        _applicationContext = applicationContext;
        _pref = applicationContext.getSharedPreferences(Constants.USER_PRIVATE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context applicationContext) {
        if (_sessionManager == null) {
            _sessionManager = new SessionManager(applicationContext);
        }
        return (_sessionManager);
    }

    public void createUserSession(User user) {

        SharedPreferences.Editor prefsEditor = _pref.edit();
        Gson gson = new Gson();
        String s_userr = gson.toJson(user);
        prefsEditor.putString(Constants.USER_SESSION_KEY, s_userr);
        prefsEditor.commit();
    }

    public void removeUserSession() {
        //eliminar session de usuario creada.
        SharedPreferences.Editor prefsEditor = _pref.edit();
        prefsEditor.clear();
        prefsEditor.commit();

    }


    public User getUserLogged() {
        String s_user = _pref.getString(Constants.USER_SESSION_KEY, null);
        User client = (s_user == null) ? null : new Gson().fromJson(s_user, User.class);
        return client;
    }


    public boolean isLogged() {
        return getUserLogged() != null;

    }

    public void logoutApp() {
        removeUserSession();

        Intent i=new Intent((_applicationContext), LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _applicationContext.startActivity(i);
    }

}
