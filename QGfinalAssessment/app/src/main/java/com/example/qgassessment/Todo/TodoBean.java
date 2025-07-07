package com.example.qgassessment.Todo;

public class TodoBean {
    private int id;
    private String title;
    private String startTime;
    private String endTime;
    private String content;
    private int userId;

    public TodoBean() {
    }

    public TodoBean(String content, String endTime, int id, String startTime, String title, int userId) {
        this.content = content;
        this.endTime = endTime;
        this.id = id;
        this.startTime = startTime;
        this.title = title;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
