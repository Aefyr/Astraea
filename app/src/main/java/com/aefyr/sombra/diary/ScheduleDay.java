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
    private String rawDate;
    private long date = 0;
    private ArrayList<ScheduleLesson> lessons;

    ScheduleDay(String rawDate, ArrayList<ScheduleLesson> lessons){
        parseDate(rawDate);
        this.lessons = lessons;
    }

    private void parseDate(String rawDate){
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDate).getTime();
        } catch (ParseException e) {
            this.rawDate = rawDate;
        }
    }

    public long rawDate() {
        return date;
    }

    public String getFormattedDate(SimpleDateFormat simpleDateFormat){
        if(date == 0)
            return rawDate;
        else
            return simpleDateFormat.format(date);
    }

    public ArrayList<ScheduleLesson> lessons() {
        return lessons;
    }
}
