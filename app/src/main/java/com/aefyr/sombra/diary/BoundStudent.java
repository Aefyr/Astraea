package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 11.09.2017.
 */

public class BoundStudent {
    private String id;
    private String name;
    private String schoolName;
    private String className;

    BoundStudent(String id, String name, String schoolName, String className){
        this.id = id;
        this.name = name;
        this.schoolName = schoolName;
        this.className = className;
    }

    public String id(){
        return id;
    }

    public String name(){
        return name;
    }

    public String schoolName(){
        return schoolName;
    }

    public String className(){
        return className;
    }
}
