package com.aefyr.sombra.common;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class ApiError extends Exception {
    public ApiError(String message) {
        super(message);
    }
}
