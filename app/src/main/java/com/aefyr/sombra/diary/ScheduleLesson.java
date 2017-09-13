package com.aefyr.sombra.diary;

import java.util.ArrayList;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class ScheduleLesson {
    private int id;
    private int num;
    private String name;
    private String starts;
    private String ends;

    boolean homework; //How does this parameter works?
    boolean skip; //OwO what's this?
    private ArrayList<BasicMark> marks;

    private Teacher teacher;

    ScheduleLesson(int id, int num, String name) {
        this.id = id;
        this.num = num;
        this.name = name;
    }

    void setTimes(String startTime, String endTime) {
        starts = startTime;
        ends = endTime;
    }

    void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    void addMarks(ArrayList<BasicMark> marks){
        this.marks = marks;
    }

    public int id() {
        return id;
    }

    public int num() {
        return num;
    }

    public String name() {
        return name;
    }

    public boolean homework() {
        return homework;
    }

    public boolean skip() {
        return skip;
    }

    public boolean hasMarks() {
        return marks != null;
    }

    public ArrayList<BasicMark> getMarks() {
        return marks;
    }

    public Teacher teacher() {
        return teacher;
    }

    public String startTime() {
        return starts;
    }

    public String endTime() {
        return ends;
    }
}
