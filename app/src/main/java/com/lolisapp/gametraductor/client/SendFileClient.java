package com.lolisapp.gametraductor.client;


import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.lolisapp.gametraductor.R;
import com.lolisapp.gametraductor.util.volley.VolleyMultipartRequest;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class SendFileClient {
    public static final String RESOURCE = "upload_app";
    SendFileClientListener _listener;
    Context context;

    public SendFileClient(Context context,SendFileClientListener _listener) {
        this._listener = _listener;
        this.context = context;
    }



    public void uploadFile(final byte[] bites, final String nameFile) {

        Log.d("s","init upload");
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                context.getString(R.string.ubas) + RESOURCE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            Log.d("response send file",new String(response.data));
                            JSONObject obj = new JSONObject(new String(response.data));
                            _listener.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error send file",error.toString());

                        _listener.onError(error.getMessage());
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long audioName = System.currentTimeMillis();
                params.put("files", new DataPart(nameFile,bites ));

                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(volleyMultipartRequest);
    }

    public interface SendFileClientListener {
        void onSuccess();

        void onError(String message);
    }

}
