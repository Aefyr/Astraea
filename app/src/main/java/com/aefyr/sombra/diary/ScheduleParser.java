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

class ScheduleParser extends AsyncParser<ArrayList<ScheduleDay>> {
    @Override
    protected ArrayList<ScheduleDay> parseData(ParseTaskParams params) {

        JsonElement result = params.getJsonObject().get("result");
        if(!result.isJsonArray()||result.getAsJsonArray().size()==0)
            return new ArrayList<>(0);


        JsonArray schedule = result.getAsJsonArray();

        ArrayList<ScheduleDay> days = new ArrayList<>(schedule.size());

        for(JsonElement jDayEl: schedule){
            JsonObject jDay = jDayEl.getAsJsonObject();

            JsonArray jLessons = jDay.getAsJsonArray("lessons");
            ArrayList<ScheduleLesson> lessons = new ArrayList<>(jLessons.size());

            for(JsonElement jLessonEl: jLessons){
                JsonObject jLesson = jLessonEl.getAsJsonObject();

                ScheduleLesson lesson = new ScheduleLesson(jLesson.get("lesson_id").getAsInt(), jLesson.get("subject_counter").getAsInt(), jLesson.get("subject_name").getAsString());
                lesson.setTimes(jLesson.get("start_time").getAsString(), jLesson.get("ends_time").getAsString());
                lesson.homework = jLesson.get("homework").getAsBoolean();
                lesson.skip = jLesson.get("skip").getAsBoolean();
                lesson.setTeacher(new Teacher(jLesson.get("teacher_id").getAsInt(), jLesson.get("teacher_fio").getAsString()));

                JsonArray jMarks = jLesson.getAsJsonArray("marks");
                if(jMarks.size()>0){
                    ArrayList<BasicMark> marks = new ArrayList<>(jMarks.size());
                    for(JsonElement jMarkEl: jMarks){
                        JsonObject jMark = jMarkEl.getAsJsonObject();
                        marks.add(new BasicMark(jMark.get("weight").getAsInt(), jMark.get("value").getAsInt(), jMark.get("mark_5").getAsInt(), jMark.get("mark_100").getAsInt()));
                    }
                    lesson.addMarks(marks);
                }

                lessons.add(lesson);
            }

            days.add(new ScheduleDay(jDay.get("date").getAsString(), lessons));
        }
        return days;
    }

    AsyncTask parse(JsonObject response, AsyncParserListener<ArrayList<ScheduleDay>> listener){
        bindListener(listener);
        return parse(new ParseTaskParams(response));
    }

}
