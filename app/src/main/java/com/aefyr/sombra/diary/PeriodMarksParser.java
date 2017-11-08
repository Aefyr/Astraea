package com.aefyr.sombra.diary;

import android.os.AsyncTask;

import com.aefyr.sombra.common.AsyncParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Aefyr on 08.11.2017.
 */

public class PeriodMarksParser extends AsyncParser<ArrayList<DeepMark>> {
    @Override
    protected ArrayList<DeepMark> parseData(ParseTaskParams params) {

        JsonElement result = params.getJsonObject().get("result").getAsJsonObject().get("marks");
        if(!result.isJsonArray()||result.getAsJsonArray().size()==0)
            return new ArrayList<>(0);

        JsonArray jMarks = result.getAsJsonArray();
        ArrayList<DeepMark> marks = new ArrayList<>(jMarks.size());
        for(JsonElement jMarkEl: jMarks){
            JsonObject jMark = jMarkEl.getAsJsonObject();
            marks.add(new DeepMark(jMark.get("weight").getAsInt(), jMark.get("value").getAsInt(), jMark.get("mark_5").getAsInt(), jMark.get("mark_100").getAsInt(), jMark.get("type").getAsString(), jMark.get("exam").getAsBoolean(), jMark.get("mark_comment").getAsString(), jMark.get("lesson_topic").getAsString(), jMark.get("date").getAsString()));
        }

        return marks;
    }

    AsyncTask parse(JsonObject response, AsyncParserListener<ArrayList<DeepMark>> listener){
        bindListener(listener);
        return parse(new ParseTaskParams(response));
    }
}
