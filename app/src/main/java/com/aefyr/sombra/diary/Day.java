package com.aefyr.sombra.diary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Aefyr on 18.09.2017.
 */
class Day {
    protected String rawDate;
    protected long date = 0;

    protected Day(String rawDate){
        parseDate(rawDate);
    }

    protected void parseDate(String rawDate){
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
}
