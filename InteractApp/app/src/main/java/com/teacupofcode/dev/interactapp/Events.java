package com.teacupofcode.dev.interactapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

import org.w3c.dom.Text;

/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Events extends Activity{


    TextView eventTitle1, eventTitle2,eventTitle3, eventInfo1, eventInfo2,eventInfo3;
    String name; //string name
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        Bundle intentData = getIntent().getExtras();
        name=intentData.getString("Name");
        email=intentData.getString("Email");
        eventInfo1 = (TextView)findViewById(R.id.eventInfo1);
        eventInfo2 = (TextView)findViewById(R.id.eventInfo2);
        eventInfo3 = (TextView)findViewById(R.id.eventInfo3);
        eventTitle1 = (TextView)findViewById(R.id.eventTitle1);
        eventTitle2 = (TextView)findViewById(R.id.eventTitle2);
        eventTitle3 = (TextView)findViewById(R.id.eventTitle3);


    }

    public void homeClickedEvents(View view) {
        Intent i = new Intent(this, Home.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void hoursClickedEvents(View view) {
        Intent i = new Intent(this, Hours.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void eventsClickedEvents(View view) {

    }

    public void signup3(View view) {
    }

    public void signup2(View view) {
    }

    public void signup1(View view) {
    }
}
