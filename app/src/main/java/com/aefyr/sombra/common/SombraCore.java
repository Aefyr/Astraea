package com.aefyr.sombra.common;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;


/**
 * Created by Aefyr on 10.09.2017.
 */

public class SombraCore {
    private static SombraCore instance;
    private RequestQueue queue;
    private String token;

    private SombraCore(Context c){
        initialize(c);
    }

    private SombraCore(Context c, String token){
        this.token = token;
        initialize(c);
    }

    private void initialize(Context c){
        queue = Volley.newRequestQueue(c);
        instance = this;
    }

    public static SombraCore getInstance(Context c){
        return instance == null?new SombraCore(c):instance;
    }

    public static SombraCore getInstance(Context c, String token){
        if(instance!=null){
            instance.token = token;
            return instance;
        }
        return new SombraCore(c, token);
    }

    public void setToken(String token){
        this.token = token;
    }

    public RequestQueue getQueue(){
        return queue;
    }

    public String getToken(){
        return token;
    }

    public void addTokenToData(JsonObject data){
        JsonObject auth = new JsonObject();
        auth.addProperty("session_id", token);
        data.add("auth", auth);
    }

    public JsonObject getBaseData(){
        JsonObject data = new JsonObject();
        addTokenToData(data);
        return data;
    }
}
