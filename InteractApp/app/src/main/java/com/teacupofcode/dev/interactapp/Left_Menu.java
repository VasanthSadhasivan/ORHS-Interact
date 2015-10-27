package com.teacupofcode.dev.interactapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Admin on 10/26/2015.
 */
public class Left_Menu extends Activity {

    Button hours, events, home;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.left_menu);
        Log.w("AYY", "AYY");
        hours = (Button) findViewById(R.id.hoursButton);
        home = (Button) findViewById(R.id.homeButton);
        events = (Button) findViewById(R.id.eventsButton);

    }

    public void hoursClicked(View view) {
        Intent i = new Intent(this, Hours.class);
        startActivity(i);
    }


    public void eventsClicked(View view) {
        Intent i = new Intent(this, Events.class);
        startActivity(i);
    }

    public void homeClicked(View view) {
        Intent i = new Intent(this, Home.class);
        startActivity(i);
    }
}
