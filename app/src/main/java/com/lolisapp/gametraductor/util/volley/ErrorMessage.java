package com.lolisapp.gametraductor.util.volley;

import org.json.JSONObject;

/**
 * Created by Carlos on 04/08/2015.
 */
public class ErrorMessage {
    String message;
    JSONObject jsonError;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JSONObject getJsonError() {
        return jsonError;
    }

    public void setJsonError(JSONObject jsonError) {
        this.jsonError = jsonError;
    }
}
