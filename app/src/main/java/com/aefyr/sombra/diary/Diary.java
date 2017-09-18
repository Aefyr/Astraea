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

            }
        });

        cancelable.addRequest(request);
        core.getQueue().add(request);
        return cancelable;
    }
}
