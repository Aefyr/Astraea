package com.aefyr.sombra.diary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Aefyr on 18.09.2017.
 */

public class DeepLesson extends ScheduleLesson {
    private ArrayList<DeepMark> deepMarks;
    private ArrayList<Hometask> tasks;
    private long date = 0;
    private String rawDate;
    String topic;
    String comment;

    DeepLesson(int id, int num, String name, String rawDate) {
        super(id, num, name);
        parseDate(rawDate);
    }

    void addDeepMarks(ArrayList<DeepMark> marks){
        deepMarks = marks;
    }

    void addTasks(ArrayList<Hometask> tasks){
        this.tasks = tasks;
    }

    private void parseDate(String rawDate){
        try {
            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDate).getTime();
        } catch (ParseException e) {
            this.rawDate = rawDate;
        }
    }

    public boolean hasTopic(){
        return topic != null;
    }

    public String topic(){
        return topic;
    }

    public boolean hasComment(){
        return comment != null;
    }

    public String comment(){
        return comment;
    }

    public boolean hasTasks(){
        return tasks != null;
    }

    public ArrayList<Hometask> tasks(){
        return  tasks;
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

    @Override
    public boolean hasMarks() {
        return deepMarks != null;
    }

    public ArrayList<DeepMark> deepMarks(){
        return deepMarks;
    }

    @Override
    public ArrayList<BasicMark> marks() {
        throw new RuntimeException("Deep lesson's marks must be accessed via DeepLesson.deepMarks()");
    }
}
