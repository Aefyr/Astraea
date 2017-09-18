package com.aefyr.sombra.diary;

import java.util.ArrayList;

/**
 * Created by Aefyr on 18.09.2017.
 */

public class HomeworkDay extends Day{
    private ArrayList<Hometask> tasks;

    HomeworkDay(String rawDate, ArrayList<Hometask> tasks){
        super(rawDate);
        this.tasks = tasks;
    }

    public ArrayList<Hometask> tasks(){
        return tasks;
    }
}
