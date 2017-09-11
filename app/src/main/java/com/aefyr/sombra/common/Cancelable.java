package com.aefyr.sombra.common;

import android.os.AsyncTask;

import com.android.volley.Request;

/**
 * Created by Aefyr on 10.09.2017.
 */

public class Cancelable {
    private Request request;
    private AsyncTask task;

    public Cancelable(Request request){
        this.request = request;
    }

    public void addTask(AsyncTask task){
        this.task = task;
    }

    public void cancel(){
        if(request!=null)
            request.cancel();
        if(task!=null)
            task.cancel(true);
    }
}
