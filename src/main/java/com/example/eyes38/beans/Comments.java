package com.example.eyes38.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jqchen on 2016/5/20.
 */
public class Comments implements Serializable{
    /**
     * path 头像地址
     * id 评论id
     * ratingbar 评论星级
     * content 内容
     * time 评论时间
     * name 评论作者名称
     * List<CommentReply> 回复内容
     */
    private String path;
    private int id;
    private int ratingbar;
    private String content;
    private String time;
    private String name;
    private List<CommentReply> replyList;

    public List<CommentReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<CommentReply> replyList) {
        this.replyList = replyList;
    }

    public Comments() {

    }

    public Comments(int id, String path, String name, int ratingbar, String content, String time, List<CommentReply> replyList) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.ratingbar = ratingbar;
        this.content = content;
        this.time = time;
        this.replyList = replyList;
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
