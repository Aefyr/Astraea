package com.aefyr.sombra.common;

import android.os.AsyncTask;

import com.google.gson.JsonObject;

/**
 * Created by Aefyr on 16.09.2017.
 */

public abstract class AsyncParser<T> {
    private String error;
    private AsyncParserListener<T> listener;

    public interface AsyncParserListener<T>{
        void onDone(T result);
        void onError(ApiError error);
    }

    protected void bindListener(AsyncParserListener<T> listener){
        this.listener = listener;
    }

    protected void setError(String errorMessage){
        error = errorMessage;
    }

    protected void parse(ParseTaskParams params){
        new ParseTask().execute(params);
    }

    protected abstract T parseData(ParseTaskParams params);

    protected class ParseTaskParams{
        private JsonObject jsonObject;

        public ParseTaskParams(JsonObject jsonObject){
            this.jsonObject = jsonObject;
        }

        public JsonObject getJsonObject(){
            return jsonObject;
        }
    }

    private class ParseTask extends AsyncTask<ParseTaskParams, Void, T>{

        @Override
        protected T doInBackground(ParseTaskParams... params) {
            return parseData(params[0]);
        }

        @Override
        protected void onPostExecute(T t) {
            super.onPostExecute(t);
            if(listener==null)
                return;

            if(t==null)
                listener.onError(new ApiError(error));
            else
                listener.onDone(t);
        }
    }
}
