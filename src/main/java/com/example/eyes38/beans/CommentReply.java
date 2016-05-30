package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/27.
 */
public class CommentReply implements Serializable {
    private int id;
    private String name;
    private String path;
    private String content;
    private String time;

    public CommentReply() {
    }

    public CommentReply(int id, String name, String path, String content, String time) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.content = content;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
