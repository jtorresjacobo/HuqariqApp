package com.lolisapp.gametraductor.util.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class GsonRequest<T> extends Request<T> {
    //private final Type type = new TypeToken<List<T>>(){}.getType();
    public static final int TIPO_RAW = 2;
    public static final int TIPO_DEFAULT = 1;
    // Atributos
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new UnixTimeDeserializer())
            .create();
    private final Listener<T> listener;
    private final Type type;
    private int tipoBody;
    private Map<String, String> headers;
    private Map<String, String> params;

    /**
     * Se predefine para el uso de peticiones GET
     */
    public GsonRequest(int method, int tipoBody, String url, Type type, Map<String, String> headers, Map<String, String> params,
                       Listener<T> listener, ErrorListener errorListener) {

        super(method, url, errorListener);

        Log.d("GsonRequest", "url= " + url);
        Log.d("GsonRequest", "params=" + new Gson().toJson(params));

        this.tipoBody = tipoBody;
        this.type = type;
        this.headers = headers;
        this.params = params;
        this.listener = listener;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

      /*  if(headers==null){ headers = Collections.emptyMap();}
        //Adding Basic Auth
        String creds = String.format("%s:%s", App.getContext().getString(R.string.ub),  App.getContext().getString(R.string.pb));
        String auth = "Basic "
                    + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        headers.put("Authorization", auth);

        return headers ;
*/
       /*
        Log.d("GsonRequest","headers="+new Gson().toJson(headers));
        return headers != null ? headers : super.getParams();*/

        // return headers != null ? headers : super.getHeaders();

        return headers;


//        return Collections.emptyMap();

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> ret = this.params != null ? this.params : super.getParams();
        return ret;
    }


    @Override
    public byte[] getBody() throws AuthFailureError {
        switch (tipoBody) {
            case TIPO_DEFAULT:
                return super.getBody();
            case TIPO_RAW:
                return new Gson().toJson(params).getBytes();
            default:
                return super.getBody();
        }

    }

    @Override
    public String getUrl() {

        String urlparam = "";
        String url = "";
        if (getMethod() == Method.GET) {
            urlparam = getPathParameters(params);
            url = super.getUrl() + "?" + urlparam;
        } else {
            url = super.getUrl();
        }

        return url;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        try {

            response.headers.put("Transfer-Encoding", "chunked");
            String json = new String(
                    response.data, HttpHeaderParser.parseCharset(response.headers));

            Log.d("GsonRequest", "json : " + json);

            return (Response<T>) Response.success(gson.fromJson(json, type), HttpHeaderParser.parseCacheHeaders(response));
            //Respuesta.success( gson.fromJson()

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));

        }

    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }


    private String getPathParameters(Map<String, String> params) {
        String pathParams = "";

        for (String key : params.keySet()) {
            pathParams += key + "=" + params.get(key) + "&";
        }

        return pathParams;
    }


}
