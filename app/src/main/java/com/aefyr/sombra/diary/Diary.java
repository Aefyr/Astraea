package com.aefyr.sombra.diary;

import com.aefyr.sombra.common.ApiError;
import com.aefyr.sombra.common.AsyncParser;
import com.aefyr.sombra.common.BaseCallback;
import com.aefyr.sombra.common.Cancelable;
import com.aefyr.sombra.common.Constants;
import com.aefyr.sombra.common.GsonRequest;
import com.aefyr.sombra.common.JsonHelper;
import com.aefyr.sombra.common.SombraCore;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Aefyr on 16.09.2017.
 */

public class Diary {
    private SombraCore core;

    private static final String SCHEDULE_URL = Constants.EMP_URL + "/v0.3/diary/getSchedule?" + Constants.EMP_API_KEY_PARAM;
    private ScheduleParser scheduleParser;

    private static final String MARKS_URL = Constants.EMP_URL + "/v0.3/diary/getSubjectsAndAttestationMarks?" + Constants.EMP_API_KEY_PARAM;
    private MarksParser marksParser;

    private static final String HOMEWORK_URL = Constants.EMP_URL + "/v0.3/diary/getHomeWorks?" + Constants.EMP_API_KEY_PARAM;
    private HomeworkParser homeworkParser;

    private static final String LESSON_URL = Constants.EMP_URL + "/v0.3/diary/getLesson?" + Constants.EMP_API_KEY_PARAM;

    private static final String SKIPS_URL = Constants.EMP_URL + "/v0.3/diary/getSkips?" + Constants.EMP_API_KEY_PARAM;

    public Diary(SombraCore core){
        this.core = core;
        scheduleParser = new ScheduleParser();
        marksParser = new MarksParser();
        homeworkParser = new HomeworkParser();
    }

    public interface ScheduleListener extends BaseCallback<ArrayList<ScheduleDay>>{}
    public Cancelable getSchedule(String studentId, String fromDay, String toDay, final ScheduleListener listener){
        JsonObject data = core.getBaseData();
        data.addProperty("child_alias", studentId);
        data.addProperty("start_date", fromDay);
        data.addProperty("end_date", toDay);

        final Cancelable cancelable = new Cancelable();

        GsonRequest request = new GsonRequest(SCHEDULE_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                cancelable.addTask(scheduleParser.parse(response, new AsyncParser.AsyncParserListener<ArrayList<ScheduleDay>>() {
                    @Override
                    public void onDone(ArrayList<ScheduleDay> result) {
                        listener.onSuccess(result);
                    }

                    @Override
                    public void onError(ApiError error) {
                        listener.onApiError(error);
                    }
                }));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkError();
            }
        });

        cancelable.addRequest(request);
        core.getQueue().add(request);
        return new Cancelable(request);
    }

    public interface MarksListener extends BaseCallback<ArrayList<Period>>{}
    public Cancelable getMarks(String studentId, int periodNum, final MarksListener listener){
        JsonObject data = core.getBaseData();
        data.addProperty("child_alias", studentId);
        data.addProperty("period_num", periodNum);

        final Cancelable cancelable = new Cancelable();

        GsonRequest request = new GsonRequest(MARKS_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                cancelable.addTask(marksParser.parse(response, new AsyncParser.AsyncParserListener<ArrayList<Period>>() {
                    @Override
                    public void onDone(ArrayList<Period> result) {
                        listener.onSuccess(result);
                    }

                    @Override
                    public void onError(ApiError error) {
                        listener.onApiError(error);
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkError();
            }
        });

        cancelable.addRequest(request);
        core.getQueue().add(request);
        return cancelable;
    }

    public interface HomeworkListener extends BaseCallback<ArrayList<HomeworkDay>>{}
    public Cancelable getHomework(String studentId, String fromDay, String toDay, final HomeworkListener listener){
        JsonObject data = core.getBaseData();
        data.addProperty("child_alias", studentId);
        data.addProperty("start_date", fromDay);
        data.addProperty("end_date", toDay);

        final Cancelable cancelable = new Cancelable();

        GsonRequest request = new GsonRequest(HOMEWORK_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                cancelable.addTask(homeworkParser.parse(response, new AsyncParser.AsyncParserListener<ArrayList<HomeworkDay>>() {
                    @Override
                    public void onDone(ArrayList<HomeworkDay> result) {
                        listener.onSuccess(result);
                    }

                    @Override
                    public void onError(ApiError error) {
                        listener.onApiError(error);
                    }
                }));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onNetworkError();
            }
        });

        cancelable.addRequest(request);
        core.getQueue().add(request);
        return cancelable;
    }

    public interface DeepLessonListener extends BaseCallback<DeepLesson>{}
    public Cancelable getDeepLesson(String studentId, final int lessonId, final DeepLessonListener listener){
        JsonObject data = core.getBaseData();
        data.addProperty("child_alias", studentId);
        data.addProperty("lesson_id", lessonId);

        GsonRequest request = new GsonRequest(LESSON_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                JsonObject jLesson = response.get("result").getAsJsonObject();

                DeepLesson lesson = new DeepLesson(lessonId, jLesson.get("subject_counter").getAsInt(), jLesson.get("subject_name").getAsString(), jLesson.get("date").getAsString());
                lesson.setTimes(jLesson.get("start_time").getAsString(), jLesson.get("ends_time").getAsString());
                lesson.setTeacher(new Teacher(jLesson.get("teacher_id").getAsInt(), jLesson.get("teacher_fio").getAsString()));
                lesson.skip = jLesson.get("skip").getAsBoolean();
                lesson.comment = jLesson.get("comment").getAsString();
                lesson.topic = jLesson.get("lesson_topic").getAsString();

                JsonArray jMarks = jLesson.getAsJsonArray("marks");
                if(jMarks.size()>0) {
                    ArrayList<DeepMark> marks = new ArrayList<>(jMarks.size());
                    for (JsonElement jMarkEl : jMarks) {
                        JsonObject jMark = jMarkEl.getAsJsonObject();
                        marks.add(new DeepMark(jMark.get("weight").getAsInt(), jMark.get("value").getAsInt(), jMark.get("mark_5").getAsInt(), jMark.get("mark_100").getAsInt(), jMark.get("type").getAsString(), jMark.get("exam").getAsBoolean(), jMark.get("comment").getAsString()));
                    }
                    lesson.addDeepMarks(marks);
                }

                JsonArray jTasks = jLesson.getAsJsonArray("tasks");
                if(jTasks.size()>0){
                    ArrayList<Hometask> tasks = new ArrayList<>(jTasks.size());
                    for(JsonElement jTaskEl: jTasks){
                        JsonObject jTask = jTaskEl.getAsJsonObject();
                        tasks.add(new Hometask(jTask.get("subject_name").getAsString(), jTask.get("homework").getAsString(), jTask.get("hw_file").getAsBoolean()));
                    }
                    lesson.addTasks(tasks);
                }

                listener.onSuccess(lesson);
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

    public interface SkipsListener extends BaseCallback<ArrayList<DayWithSkips>>{}
    public Cancelable getSkips(String studentId, String fromDate, String toDate, final SkipsListener listener){
        JsonObject data = core.getBaseData();
        data.addProperty("child_alias", studentId);
        data.addProperty("start_date", fromDate);
        data.addProperty("end_date", toDate);

        GsonRequest request = new GsonRequest(SKIPS_URL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if(!JsonHelper.checkResponse(response, listener))
                    return;

                JsonElement result = response.get("result");
                if(!result.isJsonArray()||result.getAsJsonArray().size()==0){
                    listener.onSuccess(new ArrayList<DayWithSkips>(0));
                    return;
                }

                JsonArray jDays = result.getAsJsonArray();
                ArrayList<DayWithSkips> days = new ArrayList<>(jDays.size());
                for(JsonElement jDayEl: jDays){
                    JsonObject jDay = jDayEl.getAsJsonObject();
                    days.add(new DayWithSkips(jDay.get("date").getAsString(), jDay.get("desc").getAsString(), jDay.get("all_day").getAsBoolean(), jDay.get("reason").getAsBoolean()));
                }

                listener.onSuccess(days);
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

    //TODO Next up: Deep periods info (getAttestationMarksBySubject and getCurrentMarksBySubject). Objects these methods return are sketchy, so be careful
}
