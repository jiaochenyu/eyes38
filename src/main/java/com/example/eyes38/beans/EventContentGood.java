package com.example.eyes38.beans;

/**
 * Created by weixiao on 2016/5/25.
 */
public class EventContentGood {
    private String content;
    private int id;
    private String title;
    private String pic;

    public EventContentGood() {
    }

    public EventContentGood( String title, String pic) {
        this.title = title;
        this.pic = pic;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
