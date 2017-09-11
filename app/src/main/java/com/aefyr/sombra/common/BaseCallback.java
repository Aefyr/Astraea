package com.aefyr.sombra.common;

/**
 * Created by Aefyr on 09.09.2017.
 */

public interface BaseCallback<T> {
    void onSuccess(T result);
    void onNetworkError();
    void onInvalidTokenError();
    void onApiError();
}
