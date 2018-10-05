package com.lolisapp.gametraductor.client;

import android.app.IntentService;
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

public class RegistrarUsuarioClient {

    public static final String RESOURCE = "account_app";
    private static final String TAG = "insertar usuario nuevo";
    Context context;
    RegistrarUsuarioCorreoListener listener;
    User myUSer;

    public RegistrarUsuarioClient(Context context, RegistrarUsuarioCorreoListener listener) {
        this.context = context;
        this.listener = listener;
    }


    public void insertarusuarioPorCorreo(final User user) {
        myUSer=user;
        HashMap<String, String> parametros = new HashMap();
        HashMap<String, String> cabecera = new HashMap();
        cabecera.put(context.getString(R.string.autorizacion),context.getString(R.string.codigoAutorizacion));
        cabecera.put(context.getString(R.string.tipoContenido), context.getString(R.string.tipoJson));
        parametros.put("email", user.getEmail());
        parametros.put("password", user.getPassword());
        parametros.put("first_name", user.getFirstName());
        parametros.put("last_name", user.getLastName());
        parametros.put("dni", user.getDni());
        parametros.put("phone", user.getPhone());
        parametros.put("region_id", user.getCodeDepartamento().toString());
        parametros.put("provincia_id", user.getCodeProvincia().toString());
        parametros.put("distrito_id", user.getCodeDistrito().toString());

        Log.d("body",parametros.toString());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        Log.d("tag","register user");



        JSONObject bodyRequest=new JSONObject(parametros);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                context.getString(R.string.ubas)+RESOURCE,
                bodyRequest
                ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("correcto register user",response.toString());
                        try {
                            User user=getUserFromJson(response);
                            if (user.getUserExternId()==0L){
                                listener.onError(context.getString(R.string.message_ya_registrado));
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
                        Log.d("incorrecto", String.valueOf(error.networkResponse.data));

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
        if (user.getEmail().equals("0")){
            user.setUserExternId(0L);
            return user;
        }
        user.setUserExternId(jsonObject.getJSONArray("user_id").getLong(0));
        user.setLastName(jsonObject.getString("last_name"));
        user.setFirstName(jsonObject.getString("first_name"));
        user.setDni(myUSer.getDni());
        user.setCodeDepartamento(myUSer.getCodeDepartamento());
        user.setCodeProvincia(myUSer.getCodeProvincia());
        user.setCodeDistrito(myUSer.getCodeDistrito());
        user.setPassword(myUSer.getPassword());

        return user;
    }




    public interface RegistrarUsuarioCorreoListener {
        void onSuccess(User user);

        void onError(String message);

    }
}