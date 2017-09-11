package com.aefyr.sombra.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class JsonHelper {

    public static boolean checkResponse(JsonObject response, BaseCallback listener){
        if(response.get("result")==null||response.get("errorCode").getAsInt()!=0){
            if(response.get("errorCode").getAsInt()==401)
                listener.onInvalidTokenError();
            else
                listener.onApiError();
            return false;
        }else
            return true;
    }
}
