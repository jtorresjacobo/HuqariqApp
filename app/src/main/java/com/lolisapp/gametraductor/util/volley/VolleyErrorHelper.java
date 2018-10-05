package com.lolisapp.gametraductor.util.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.lolisapp.gametraductor.R;

import org.json.JSONObject;

/**
 * Created by Carlos on 04/08/2015.
 */
public class VolleyErrorHelper {
    private static final String TAG = "VolleyErrorHelper";

    /**
     * Returns appropriate message which is to be displayed to the user
     * against the specified error object.
     *
     * @param error
     * @param context
     * @return
     */
    public static ErrorMessage getErrorMessage(Object error, Context context) throws Exception {
        ErrorMessage resErrorMessage = new ErrorMessage();
        if (error instanceof TimeoutError) {
            resErrorMessage.setMessage(context.getResources().getString(R.string.generic_server_down));
            return resErrorMessage;
        } else if (isServerProblem(error)) {
            return handleServerError(error, context);
        } else if (isNetworkProblem(error)) {
            resErrorMessage.setMessage(context.getResources().getString(R.string.generic_server_down));
            return resErrorMessage;
        } else if (isUnSuccessProblem(error)) {
            //resErrorMessage.setMessage("Hay un error enviado con success cero");
            //return resErrorMessage;

            VolleyError err = (VolleyError) error;
            NetworkResponse response = err.networkResponse;

            if (response != null) {
                Log.d(TAG, "response es diferente de null");
                String responseBody = null;
                responseBody = new String(err.networkResponse.data, "utf-8");
                JSONObject jsonObject = new JSONObject(responseBody);

                String msg = jsonObject.has("msg") ? jsonObject.getString("msg") : context.getResources().getString(R.string.generic_error);

                resErrorMessage.setMessage(msg);
                resErrorMessage.setJsonError(null);

                return resErrorMessage;

            } else {
                Log.d(TAG, "response es  es null");
                resErrorMessage.setMessage(context.getResources().getString(R.string.generic_error));
                return resErrorMessage;
            }


        }
        resErrorMessage.setMessage(context.getResources().getString(R.string.generic_error));
        return resErrorMessage;
    }


    /**
     * Determines whether the error is related to network
     *
     * @param error
     * @return
     */
    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    /**
     * Determines whether the error is related to server
     *
     * @param error
     * @return
     */
    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    /**
     * Handles the server error, tries to determine whether to show a stock message or to
     * show a message retrieved from the server.
     *
     * @param err
     * @param context
     * @return
     */
    private static ErrorMessage handleServerError(Object err, Context context) throws Exception {

        ErrorMessage resErrorMessage = new ErrorMessage();

        VolleyError error = (VolleyError) err;
        NetworkResponse response = error.networkResponse;

        if (response != null) {
            String responseBody = null;
            responseBody = new String(error.networkResponse.data, "utf-8");
            Log.d("responseBOdy",responseBody);
            JSONObject jsonObject = new JSONObject(responseBody);

            resErrorMessage.setMessage(null);
            resErrorMessage.setJsonError(jsonObject);

            return resErrorMessage;

        }
        resErrorMessage.setMessage(context.getResources().getString(R.string.generic_error));
        return resErrorMessage;
    }


    private static boolean isUnSuccessProblem(Object error) {

        return (error instanceof UnSuccessException);
    }


}