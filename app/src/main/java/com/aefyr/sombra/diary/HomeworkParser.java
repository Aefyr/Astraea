package com.aefyr.sombra.diary;

import android.os.AsyncTask;

import com.aefyr.sombra.common.AsyncParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Aefyr on 18.09.2017.
 */

class HomeworkParser extends AsyncParser<ArrayList<HomeworkDay>> {
    @Override
    protected ArrayList<HomeworkDay> parseData(ParseTaskParams params) {
        JsonElement result = params.getJsonObject().get("result");
        if(!result.isJsonArray()||result.getAsJsonArray().size()==0)
            return new ArrayList<HomeworkDay>(0);


        JsonArray jHomeworkDays = result.getAsJsonArray();

        ArrayList<HomeworkDay> days = new ArrayList<>(7);

        for(JsonElement jDayEl: jHomeworkDays) {
            JsonObject jDay = jDayEl.getAsJsonObject();

            ArrayList<Hometask> tasks = new ArrayList<>(7);
            for(JsonElement jTaskEl: jDay.get("tasks").getAsJsonArray()){
                JsonObject jTask = jTaskEl.getAsJsonObject();

                tasks.add(new Hometask(jTask.get("subject_name").getAsString(), jTask.get("homework").getAsString(), jTask.get("hw_file").getAsBoolean()));
            }

            days.add(new HomeworkDay(jDay.get("date").getAsString(), tasks));
        }

        return days;
    }

    AsyncTask parse(JsonObject response, AsyncParserListener<ArrayList<HomeworkDay>> listener){
        bindListener(listener);
        return parse(new ParseTaskParams(response));
    }
}
