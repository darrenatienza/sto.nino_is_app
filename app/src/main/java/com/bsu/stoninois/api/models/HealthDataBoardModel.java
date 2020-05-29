package com.bsu.stoninois.api.models;

public class HealthDataBoardModel {
    int id;
    String userName;
    int year;
    int count;
    int userID;
    int commonHealthProfileID;
    String commonHealthProfileTitle;



    public String getCommonHealthProfileTitle() {
        return commonHealthProfileTitle;
    }

    public void setCommonHealthProfileTitle(String commonHealthProfileTitle) {
        this.commonHealthProfileTitle = commonHealthProfileTitle;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCommonHealthProfileID() {
        return commonHealthProfileID;
    }

    public void setCommonHealthProfileID(int commonHealthProfileID) {
        this.commonHealthProfileID = commonHealthProfileID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
