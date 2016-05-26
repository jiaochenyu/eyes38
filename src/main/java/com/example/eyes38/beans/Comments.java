package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comments implements Serializable{
    private String path;
    private int id;
    private int ratingbar;
    private String content;
    private String time;
    private String name;

    public Comments(int id, String path, String name, int ratingbar, String content, String time) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.ratingbar = ratingbar;
        this.content = content;
        this.time = time;
    }

    public int getRatingbar() {
        return ratingbar;
    }

    public void setRatingbar(int ratingbar) {
        this.ratingbar = ratingbar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
