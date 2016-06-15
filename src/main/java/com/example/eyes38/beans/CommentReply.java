package com.example.eyes38.beans;

import java.io.Serializable;

/**
 * Created by jqchen on 2016/5/27.
 */
public class CommentReply implements Serializable {
    /**
     * id 编号，表明几楼
     * comment_id 后台编号
     * author_name 评论作者
     * path 头像地址
     * comment 回复内容
     * create_date 评论时间
     * author_id 评论人id
     */
    private int id;
    private int comment_id;
    private String author_name;
    private String path;
    private String comment;
    private String create_date;
    private int author_id;
    public CommentReply() {
    }

    public CommentReply(int id, int comment_id, String author_name, String path, String comment, String create_date,int author_id) {
        this.id = id;
        this.comment_id = comment_id;
        this.author_name = author_name;
        this.path = path;
        this.comment = comment;
        this.create_date = create_date;
        this.author_id = author_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
