package com.teacupofcode.dev.interactapp;

import java.lang.*;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Info extends Activity {

    String name; //string name
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        Bundle intentData = getIntent().getExtras();
        name = intentData.getString("Name");
        email = intentData.getString("Email");
    }

    public void infoClickedInfo(View view) {

    }

    public void homeClickedInfo(View view) {
        Intent i = new Intent(this, Home.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void eventsClickedInfo(View view) {
        Intent i = new Intent(this, Events.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void websiteClickedInfo (View view) {
        Uri uri = Uri.parse("https://orhsinteract.wix.com/1516");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    public void joinClickedInfo (View view) {
        Uri uri = Uri.parse("https://docs.google.com/forms/d/1-ctFeAA4864rmHKsSczlP04mas-20f5n5aDrcX5btbc/viewform");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    public void githubClickedInfo (View view) {
        Uri uri = Uri.parse("https://github.com/ORHS-Web-App-Dev");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    public void resourcesClickedInfo (View view) {
        Uri uri = Uri.parse("https://drive.google.com/folderview?id=0BwJLvdTM6Ac4UWlqSHlKTGhxRFk&usp=sharing");
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }
}
