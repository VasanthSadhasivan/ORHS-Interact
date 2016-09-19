package com.teacupofcode.ved.interactapp;

import android.provider.ContactsContract;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 9/15/2016.
 */
public class Event {

    String name;
    String link;
    String location;
    Date date;
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mma MMMM d, y");
        return (formatter.format(date));
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
