package com.teacupofcode.ved.interactapp;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * Created by Admin on 9/15/2016.
 */
public class Event {

    String name;
    String link;
    String location;
    Calendar date;
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
        String timeString =date.get(Calendar.HOUR_OF_DAY)%12+":"+date.get(Calendar.MINUTE);
        if(date.get(Calendar.MINUTE)<10)
            timeString =date.get(Calendar.HOUR_OF_DAY)%12+":0"+date.get(Calendar.MINUTE);
        if(date.get(Calendar.HOUR_OF_DAY)>12)
            timeString = timeString+"PM";
        else
            timeString = timeString+"AM";
        return (new DateFormatSymbols().getMonths()[date.get(Calendar.MONTH)])+" " +date.get(Calendar.DAY_OF_MONTH)+" " +timeString;
    }

    public void setDate(Calendar date) {
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
