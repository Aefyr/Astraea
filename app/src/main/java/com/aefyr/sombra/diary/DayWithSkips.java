package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 04.10.2017.
 */

public class DayWithSkips extends Day {
    private boolean wholeDaySkipped;
    private boolean reasonableSkipping;
    private String description;

    protected DayWithSkips(String rawDate, String description, boolean wholeDaySkipped, boolean reasonableSkipping) {
        super(rawDate);
        this.description = description;
        this.wholeDaySkipped = wholeDaySkipped;
        this.reasonableSkipping = reasonableSkipping;
    }

    public boolean skippedWholeDay(){
        return wholeDaySkipped;
    }

    public boolean reasonableSkipping(){
        return reasonableSkipping;
    }

    public String description(){
        return description;
    }
}
