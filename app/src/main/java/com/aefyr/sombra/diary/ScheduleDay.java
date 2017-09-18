package com.aefyr.sombra.diary;

import java.util.ArrayList;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class ScheduleDay extends Day{
    private ArrayList<ScheduleLesson> lessons;

    ScheduleDay(String rawDate, ArrayList<ScheduleLesson> lessons){
        super(rawDate);
        this.lessons = lessons;
    }

    public ArrayList<ScheduleLesson> lessons() {
        return lessons;
    }
}
