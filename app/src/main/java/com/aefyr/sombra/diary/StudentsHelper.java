package com.aefyr.sombra.diary;

import com.aefyr.sombra.common.BaseCallback;
import com.aefyr.sombra.common.Cancelable;
import com.aefyr.sombra.common.Constants;
import com.aefyr.sombra.common.GsonRequest;
import com.aefyr.sombra.common.JsonHelper;
import com.aefyr.sombra.common.SombraCore;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Aefyr on 11.09.2017.
 */

public class StudentsHelper {
    private SombraCore core;
    private static final String BOUND_STUDENTS_URL = Constants.EMP_URL + "/v0.3/diary/getChildrenByUser?" + Constants.EMP_API_KEY_PARAM;

    public StudentsHelper(SombraCore core) {
        this.core = core;
    }

    public interface StudentsGetListener extends BaseCallback<ArrayList<BoundStudent>> {
    }

    ;

    public Cancelable getBoundStudents(final StudentsGetListener listener) {

        GsonRequest request = new GsonRequest(BOUND_STUDENTS_URL, core.getBaseData(), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!JsonHelper.checkResponse(response, listener))
                    return;

                if (!response.get("result").isJsonArray() || response.get("result").getAsJsonArray().size() == 0) {
                    listener.onSuccess(new ArrayList<BoundStudent>(0));
                    return;
                }

                ArrayList<BoundStudent> students = new ArrayList<>(3);
                for (JsonElement studentEl : response.get("result").getAsJsonArray()) {
                    JsonObject student = studentEl.getAsJsonObject();
                    students.add(new BoundStudent(student.get("child_alias").getAsString(), student.get("child_name").getAsString(), student.get("school_name").getAsString(), student.get("unit_name").getAsString()));
                }

                listener.onSuccess(students);
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
}
