package com.aefyr.sombra.diary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Aefyr on 09.10.2017.
 */

public class AttestationPeriod {
    private String id;
    private String name;

    private long periodStart = 0;
    private long periodEnd = 0;
    private String rawStarts;
    private String rawEnds;

    private String periodIndex;

    private String resultMarkName;
    private float resultMark;
    private String averageMarkName;
    private float averageMark;

    AttestationPeriod(String id, String name, String rawStarts, String rawEnds, String index, String resultMarkName, float resultMark, String averageMarkName, float averageMark){
        this.id = id;
        this.name = name;
        parseDates(rawStarts, rawEnds);
        this.periodIndex = index;
        this.resultMarkName = resultMarkName;
        this.resultMark = resultMark;
        this.averageMarkName = averageMarkName;
        this.averageMark = averageMark;

    }

    private void parseDates(String rawStarts, String rawEnds){
        try {
            SimpleDateFormat periodsDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            periodStart = periodsDateFormat.parse(rawStarts).getTime();
            periodEnd = periodsDateFormat.parse(rawEnds).getTime();
        } catch (ParseException e) {
            this.rawStarts = rawStarts;
            this.rawEnds = rawEnds;
        }
    }

    public String getFormattedStart(SimpleDateFormat spd){
        if(periodStart==0)
            return rawStarts;
        return spd.format(periodStart);
    }

    public String getFormattedEnd(SimpleDateFormat spd){
        if(periodEnd==0)
            return rawEnds;
        return spd.format(periodEnd);
    }

    public String id(){
        return id;
    }

    public String name(){
        return name;
    }

    public String index(){
        return periodIndex;
    }

    public String resultMarkName(){
        return  resultMarkName;
    }

    public float resultMark(){
        return resultMark;
    }

    public String averageMarkName(){
        return averageMarkName;
    }

    public float averageMark(){
        return averageMark;
    }
}
