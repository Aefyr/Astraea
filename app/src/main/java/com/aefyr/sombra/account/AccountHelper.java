package com.aefyr.sombra.account;

import com.aefyr.sombra.common.BaseCallback;
import com.aefyr.sombra.common.Cancelable;
import com.aefyr.sombra.common.Constants;
import com.aefyr.sombra.common.GsonRequest;
import com.aefyr.sombra.common.JsonHelper;
import com.aefyr.sombra.common.SombraCore;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

/**
 * Created by Aefyr on 09.09.2017.
 */

public class AccountHelper {
    private static final String AUTHORIZATION_URL = Constants.EMP_URL + "/v0.1/auth/virtualLogin?" + Constants.EMP_API_KEY_PARAM;
    private static final String PROFILE_DATA_URL =  Constants.EMP_URL + "/v0.1/profile/get?" + Constants.EMP_API_KEY_PARAM;
    private SombraCore core;

    public AccountHelper(SombraCore sombraCore){
        core = sombraCore;
    }

    public interface AuthorizationListener extends BaseCallback<String>{
        void onInvalidCredentials();
    }

    public Cancelable authorize(String username, String password, final AuthorizationListener listener){
        JsonObject credentials = new JsonObject();
        credentials.addProperty("login", username);
        credentials.addProperty("password", password);
        JsonObject data = new JsonObject();
        data.add("auth", credentials);
        System.out.println(data);

        GsonRequest request = new GsonRequest(AUTHORIZATION_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(response.get("errorCode").getAsInt()!=0){
                    listener.onInvalidCredentials();
                    return;
                }
                listener.onSuccess(response.get("session_id").getAsString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkError();
            }
        });

        core.getQueue().add(request);
        return new Cancelable(request);
    }

    public interface ProfileListener extends BaseCallback<AccountData>{}

    public Cancelable getProfileInfo(final ProfileListener listener){

        GsonRequest request = new GsonRequest(PROFILE_DATA_URL, core.getBaseData(), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                JsonObject result = response.get("result").getAsJsonObject();

                AccountData accountData = new AccountData();

                String firstName = result.get("firstname")==null?Constants.UNKNOWN:result.get("firstname").getAsString();
                String middleName = result.get("middlename")==null?Constants.UNKNOWN:result.get("middlename").getAsString();
                String lastName = result.get("lastname")==null?Constants.UNKNOWN:result.get("lastname").getAsString();
                accountData.setName(firstName, middleName, lastName);

                accountData.setMail(result.get("email")==null?Constants.UNKNOWN:result.get("email").getAsString());

                listener.onSuccess(accountData);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkError();
            }
        });

        core.getQueue().add(request);

        return new Cancelable(request);
    }

}
