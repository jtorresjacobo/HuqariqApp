package com.lolisapp.gametraductor.client;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lolisapp.gametraductor.R;
import com.lolisapp.gametraductor.bean.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by USUARIO on 10/09/2017.
 */

public class LoginClient {
    public static final String RESOURCE = "login_app";
    private static final String TAG = "UserByEmailClient";
    VerificarUsuarioListener listener;
    Context context;

    public LoginClient(Context context, VerificarUsuarioListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void verificarUser(String correo, String pass) {

        HashMap<String, String> parametros = new HashMap();
        parametros.put("email", correo);
        parametros.put("password", pass);

        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion), context.getString(R.string.codigoAutorizacion));

        RequestQueue requestQueue = Volley.newRequestQueue(context);



        JSONObject bodyRequest=new JSONObject(parametros);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.ubas)+RESOURCE,
                bodyRequest
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("correcto",response.toString());
                        try {
                            User user=getUserFromJson(response);
                            if (user.getUserExternId()==0L){
                                listener.onError(context.getString(R.string.message_fail_login));
                            }else {
                                listener.onSuccess(user);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        String   message = context.getResources().getString(R.string.generic_error);
                        listener.onError(message);

                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }




    private User getUserFromJson(JSONObject jsonObject) throws JSONException {
        User user=new User();
        user.setEmail(jsonObject.getString("email"));
        user.setUserExternId(jsonObject.getLong("user_app_id"));
        user.setLastName(jsonObject.getString("last_name"));
        user.setFirstName(jsonObject.getString("first_name"));
        if (jsonObject.has("count")){
            user.setAvance(
                    Integer.parseInt(jsonObject.getString("count").replace("[","").replace("]","")));
        }

        if (jsonObject.has("dni")) user.setDni(jsonObject.getString("dni"));

        return user;
    }


    public interface VerificarUsuarioListener {
        void onSuccess(User user);

        void onError(String message);
    }


}