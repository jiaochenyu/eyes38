package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/6/14.
 * 此类是机器人聊天的内容
 */
public class Consult implements Serializable {
    private String path;
    private String info;

    public Consult(String path, String info) {
        this.path = path;
        this.info = info;
    }

    public Consult() {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
