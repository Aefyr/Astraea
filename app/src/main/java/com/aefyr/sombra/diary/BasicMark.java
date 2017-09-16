package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 13.09.2017.
 */

public class BasicMark {
    private int weight;
    private int valueRaw;
    private int value5;
    private int value100;

    BasicMark(int weight, int valueRaw, int value5, int value100){
        this.weight = weight;
        this.valueRaw = valueRaw;
        this.value5 = value5;
        this.value100 = value100;
    }

    public int weight(){
        return weight;
    }

    public int valueRaw(){
        return valueRaw;
    }

    public int value5(){
        return value5;
    }

    public int value100(){
        return value100;
    }
}
