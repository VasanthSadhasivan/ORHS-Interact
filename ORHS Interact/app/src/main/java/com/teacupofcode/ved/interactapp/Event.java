package com.teacupofcode.ved.interactapp;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *  Created by Admin on 9/15/2016.
 */
class Event {

    private String name;
    private String link;
    private String location;
    private Date date;
    private String description;

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

     String getDate() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("h:mma MMMM d, y");
        return (formatter.format(date));
    }

    void setDate(Date date) {
        this.date = date;
    }

    String getLink() {
        return link;
    }

    void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}