package com.aefyr.sombra.common;

import com.google.gson.JsonObject;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class JsonHelper {

    public static boolean checkResponse(JsonObject response, BaseCallback listener) {
        int errorCode = response.get("errorCode").getAsInt();
        if (response.get("result") == null || errorCode != 0) {
            if (errorCode == 401)
                listener.onInvalidTokenError();
            else
                listener.onApiError(new ApiError(response.get("errorMessage") != null ? response.get("errorMessage").getAsString() : "Сервер не передал данных"));
            return false;
        } else
            return true;
    }
}
