package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 18.09.2017.
 */

public class Hometask {
    private String subjectName;
    private String task;
    private boolean filesAttached;

    Hometask(String subjectName, String task, boolean filesAttached){
        this.subjectName = subjectName;
        this.task = task;
        this.filesAttached = filesAttached;
    }

    public String subjectName(){
        return subjectName;
    }

    public String task(){
        return task;
    }

    public boolean hasFiles(){
        return filesAttached;
    }
}
