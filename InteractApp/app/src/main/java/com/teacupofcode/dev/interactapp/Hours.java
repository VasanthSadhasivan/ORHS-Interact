package com.teacupofcode.dev.interactapp;

import java.lang.*;
import com.navdrawer.SimpleSideDrawer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Hours extends Activity{

    String name; //string name
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.hours);
        Bundle intentData = getIntent().getExtras();
        name=intentData.getString("Name");
        email=intentData.getString("Email");
    }

    public void hoursClickedHours(View view) {

    }

    public void homeClickedHours(View view) {
        Intent i = new Intent(this, Home.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void eventsClickedHours(View view) {
        Intent i = new Intent(this, Events.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void volunteerClicked(View view) {

    }
}
