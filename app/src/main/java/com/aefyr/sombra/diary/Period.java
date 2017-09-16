package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 16.09.2017.
 */

public class Period {
    String id;
    String name;
    String finalMark;
    String subjectName;
    int subjectId;
    float averageMark;
    String startDate;
    String endDate;

    public Period(String id, String name, String subjectName, int subjectId, String startDate, String endDate){
        this.id = id;
        this.name = name;
        this.subjectName = subjectName;
        this.subjectId = subjectId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String id(){
        return id;
    }

    public String name(){
        return name;
    }

    public int subjectId(){
        return subjectId;
    }

    public String subjectName(){
        return subjectName;
    }

    public float averageMark(){
        return  averageMark;
    }

    public String finalMark(){
        return  finalMark;
    }

    public String startDate(){
        return startDate;
    }

    public String endDate(){
        return endDate;
    }
}
