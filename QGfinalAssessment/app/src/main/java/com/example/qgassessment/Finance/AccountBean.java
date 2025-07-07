package com.example.qgassessment.Finance;
//记录每一条数据的类
public class AccountBean {
    int id,userId;
    String type;
    float money;
    String time;
    int year,month,day;

    public AccountBean(int day, int id, int userId, float money, int month, String time, String type, int year) {
        this.day = day;
        this.id = id;
        this.userId = userId;
        this.money = money;
        this.month = month;
        this.time = time;
        this.type = type;
        this.year = year;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public AccountBean() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
