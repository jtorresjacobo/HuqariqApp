package com.lolisapp.gametraductor.util.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by josejurado on 31/08/15.
 */
public class UnSuccessException extends VolleyError {

    public UnSuccessException(NetworkResponse networkResponse) {
        super(networkResponse);
    }

    public UnSuccessException(String detailMessage) {
        super(detailMessage);
    }

}
