package com.teacupofcode.dev.interactapp;

import java.lang.*;

import android.app.Activity;
import android.content.Intent;
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
    public void hoursClickedInfo (View view) {

    }
}
