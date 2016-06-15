package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by JCY on 2016/6/14.
 */
public class MessageBean implements Serializable{
    int messageID;
    int customerID ;
    String path;
    String time ;
    String content;
    String title;

    public MessageBean() {
    }

    public MessageBean(int messageID, int customerID, String path, String title, String content, String time) {
        this.messageID = messageID;
        this.customerID = customerID;
        this.path = path;
        this.content = content;
        this.title = title;
        this.time = time;
    }

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
