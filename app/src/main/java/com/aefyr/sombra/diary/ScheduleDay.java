package com.aefyr.sombra.diary;

import com.aefyr.sombra.common.ApiError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class ScheduleDay {
    private long date;
    private ArrayList<ScheduleLesson> lessons;

    ScheduleDay(String rawDate, ArrayList<ScheduleLesson> lessons) throws ApiError {
        parseDate(rawDate);
        this.lessons = lessons;
    }

    private void parseDate(String rawDate) throws ApiError {
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDate).getTime();
        } catch (ParseException e) {
            throw new ApiError("Can't parse ScheduleDay date: " + e.getMessage());
        }
    }

    public long date() {
        return date;
    }

    public ArrayList<ScheduleLesson> lessons() {
        return lessons;
    }
}
