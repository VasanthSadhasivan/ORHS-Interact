package com.teacupofcode.ved.interactapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 9/28/2016.
 */
public class DatProvider {
    public static HashMap<String, List<String>> getInfo(){
        HashMap<String, List<String>> eventDescription = new HashMap<String, List<String>>();
        List<String> d1 = new ArrayList<String>();
        d1.add("hi");
        List<String> d2 = new ArrayList<String>();
        d2.add("hi");
        List<String> d3 = new ArrayList<String>();
        d3.add("hi");
        eventDescription.put("1",d1);
        eventDescription.put("2",d2);
        eventDescription.put("3",d3);
        return eventDescription;
    }
}
