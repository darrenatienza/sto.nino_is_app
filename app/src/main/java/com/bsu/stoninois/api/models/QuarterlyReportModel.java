package com.bsu.stoninois.api.models;

public class QuarterlyReportModel {
    int id;
    String userName;
    int year;
    int count;
    int quarter;
    int userID;
    String userFullName;
    int accomplishmentID;
    String accomplishmentTitle;
    String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public int getAccomplishmentID() {
        return accomplishmentID;
    }

    public void setAccomplishmentID(int accomplishmentID) {
        this.accomplishmentID = accomplishmentID;
    }

    public String getAccomplishmentTitle() {
        return accomplishmentTitle;
    }

    public void setAccomplishmentTitle(String accomplishmentTitle) {
        this.accomplishmentTitle = accomplishmentTitle;
    }
}
