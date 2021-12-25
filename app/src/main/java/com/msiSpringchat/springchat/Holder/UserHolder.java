package com.msiSpringchat.springchat.Holder;

public class UserHolder {

    String name,piMage,barthDate,gender,uid;
    String status;


    public UserHolder(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserHolder(String status) {
        this.status = status;
    }

    public UserHolder(String name, String piMage, String barthDate, String gender, String uid) {
        this.name = name;
        this.piMage = piMage;
        this.barthDate = barthDate;
        this.gender = gender;
        this.uid = uid;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPiMage() {
        return piMage;
    }

    public void setPiMage(String piMage) {
        this.piMage = piMage;
    }

    public String getBarthDate() {
        return barthDate;
    }

    public void setBarthDate(String barthDate) {
        this.barthDate = barthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



}
