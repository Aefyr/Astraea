package com.aefyr.sombra.diary;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by Aefyr on 18.09.2017.
 */

public class DeepMark extends BasicMark{
    private String type;
    private boolean exam;
    private String comment;
    private String topic;

    private long date = 0;
    private String rawDate;


    DeepMark(int weight, int valueRaw, int value5, int value100, String type, boolean exam, String comment, @Nullable String topic, @Nullable String rawDate) {
        super(weight, valueRaw, value5, value100);
        this.type = type;
        this.exam = exam;

        if(comment.length()>0)
            this.comment = comment;

        if(topic!=null)
            this.topic = topic;

        if(rawDate!=null)
            parseDate(rawDate);

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

    public String topic(){
        return topic;
    }
}
