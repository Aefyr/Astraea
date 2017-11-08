package com.aefyr.sombra.common;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aefyr on 09.09.2017.
 */

public class GsonRequest extends Request<JsonObject> {
    private JsonObject data;
    private Response.Listener<JsonObject> listener;
    private static final String TAG = "GsonRequest";

    public GsonRequest(String url, JsonObject data, Response.Listener<JsonObject> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        this.data = data;
        this.listener = listener;
        Log.d(TAG, "Making request:\nURL: "+url+"\nDATA: "+data.toString());
    }

    @Override
    protected Response<JsonObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, "UTF-8");
            return Response.success(new JsonParser().parse(json).getAsJsonObject(), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException | ClassCastException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        return headers;
    }

    @Override
    protected void deliverResponse(JsonObject response) {
        System.out.println(response.toString());
        listener.onResponse(response);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return data.toString().getBytes();
    }
}
