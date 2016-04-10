package com.example.wad.hangin;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Admin on 4/9/2016.
 */
public class Places
{
    private LatLng latlng;
    private String name;
    private ArrayList<Friend> friendsArrayList = new ArrayList<Friend>();

    public void Places(LatLng latlng, String name)
    {
        this.latlng = latlng;
        this.name = name;
    }
    public ArrayList<Friend> getFriendList()
    {
        return this.friendsArrayList;
    }
    public LatLng getLatLng()
    {
        return this.latlng;
    }
    public String getName()
    {
        return this.name;
    }
    public void addFriend(Friend friend)
    {
        friendsArrayList.add(friend);
    }

}
