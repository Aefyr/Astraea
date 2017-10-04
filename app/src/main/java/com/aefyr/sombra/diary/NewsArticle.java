package com.aefyr.sombra.diary;

/**
 * Created by Aefyr on 04.10.2017.
 */

public class NewsArticle extends Day{
    private int id;
    private String title;
    private String html;

    protected NewsArticle(int id, String rawDate, String title, String html) {
        super(rawDate);
        this.id = id;
        this.title = title;
        this.html = html;
    }

    public int id(){
        return id;
    }

    public String title(){
        return title;
    }

    public String html(){
        return html;
    }
}
