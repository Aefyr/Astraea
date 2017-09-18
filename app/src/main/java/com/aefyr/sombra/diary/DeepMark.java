package com.aefyr.sombra.diary;

import java.util.ArrayList;

/**
 * Created by Aefyr on 18.09.2017.
 */

public class DeepMark extends BasicMark{
    private String type;
    private boolean exam;
    private String comment;


    DeepMark(int weight, int valueRaw, int value5, int value100, String type, boolean exam) {
        super(weight, valueRaw, value5, value100);
        this.type = type;
        this.exam = exam;
    }

    void addComment(String comment){
        this.comment = comment;
    }

    public String type(){
        return type;
    }

    public boolean exam(){
        return exam;
    }

    public boolean hasComment(){
        return comment != null;
    }

    public String comment(){
        return comment;
    }
}
