package com.aefyr.sombra.diary;

import android.os.AsyncTask;

import com.aefyr.sombra.common.AsyncParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Aefyr on 16.09.2017.
 */

class MarksParser extends AsyncParser<ArrayList<Period>> {
    @Override
    protected ArrayList<Period> parseData(ParseTaskParams params) {

        JsonElement result = params.getJsonObject().get("result");
        if(!result.isJsonArray()||result.getAsJsonArray().size()==0)
            return new ArrayList<>(0);

        JsonArray jPeriods = result.getAsJsonArray();
        ArrayList<Period> periods = new ArrayList<>(jPeriods.size());

        for(JsonElement jPeriodEl: jPeriods){
            JsonObject jPeriod = jPeriodEl.getAsJsonObject();
            Period period = new Period(jPeriod.get("period_id").getAsString(),
                    jPeriod.get("period_name").getAsString(),
                    jPeriod.get("subject_name").getAsString(),
                    jPeriod.get("subject_id").getAsInt(),
                    jPeriod.get("periods_from_date").getAsString(),
                    jPeriod.get("periods_to_date").getAsString());

            period.finalMark = jPeriod.get("result_mark").getAsString();
            period.averageMark = jPeriod.get("average_mark").getAsFloat();

            periods.add(period);
        }
        return periods;
    }

    AsyncTask parse(JsonObject response, AsyncParserListener<ArrayList<Period>> listener){
        bindListener(listener);
        return parse(new ParseTaskParams(response));
    }
}
