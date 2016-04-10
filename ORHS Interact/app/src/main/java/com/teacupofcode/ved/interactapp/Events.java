package com.teacupofcode.ved.interactapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Events extends Activity{

    String name; //string name
    String email;
    static ArrayList<String> events = new ArrayList<String>();
    static ArrayList<String> dates = new ArrayList<String>();
    static ArrayList<String> links = new ArrayList<String>();
    static Intent browserIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        Bundle intentData = getIntent().getExtras();
        name=intentData.getString("Name");
        email=intentData.getString("Email");

        Log.w("AYY", "BEBEBEBBE");

        MySpreadsheetIntegration myspreadsheetobject = new MySpreadsheetIntegration();
        myspreadsheetobject.execute("whatever");
        while(MySpreadsheetIntegration.dateList==null || MySpreadsheetIntegration.eventList==null)
        {
        }
        Events.dates=MySpreadsheetIntegration.dateList;
        Events.events=MySpreadsheetIntegration.filterEvents(MySpreadsheetIntegration.eventList);
        Events.links = MySpreadsheetIntegration.linkList;
        Log.w("AYY", Events.dates.get(0));
        setAllViewInfo();
    }
    //Later on, fix the method to have info and location
    public void setAllViewInfo()
    {
        Log.w("GS99",""+ Events.events.size());
        for(int i=0; i<Events.events.size(); i++)
        {
            Log.w("GS9", ""+Events.events.get(i));
            //try {
            generateView(Events.events.get(i), "-----", "-----", Events.dates.get(i), Events.links.get(i));
            //}
            /*catch (IndexOutOfBoundsException e){
                generateView(Events.events.get(i), "-----", "-----", Events.dates.get(i), "https://docs.google.com/spreadsheets/d/1aoUbUIIQtmj2aubTwQF4XskgaESBlrMdzmli7IPzEcQ/edit#gid=0");
            }
            catch (NullPointerException e){
                generateView(Events.events.get(i), "-----", "-----", Events.dates.get(i), "https://docs.google.com/spreadsheets/d/1aoUbUIIQtmj2aubTwQF4XskgaESBlrMdzmli7IPzEcQ/edit#gid=0");
            }*/
        }
    }
    public void homeClickedEvents(View view) {
        Intent i = new Intent(this, Home.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void infoClickedEvents(View view) {
        Intent i = new Intent(this, Info.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void eventsClickedEvents(View view) {

    }

    public int generateView(String Title, String Info, String Place, String Time, final String link)
    {
        //Creating Relative Layout Programmatically
        RelativeLayout relativeLayout = new RelativeLayout(this);
        //CHANGE LATER
        relativeLayout.setId(View.generateViewId());
        relativeLayout.setBackgroundResource(R.drawable.borders);
        relativeLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.background2));
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.topMargin=15;
        relativeLayout.setLayoutParams(rlp);
        ////////////// TEXT VIEWS //////////////

        TextView titleView = new TextView(this);
        titleView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        titleView.setBackgroundResource(R.color.background2);
        if (Build.VERSION.SDK_INT < 23) {
            titleView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Large);
        } else {
            titleView.setTextAppearance(android.R.style.TextAppearance_Large);
        }
        //CHANGE LATER
        titleView.setId(View.generateViewId());
        titleView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleView.setText(Title);
        titleView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        TextView infoView = new TextView(this);
        if (Build.VERSION.SDK_INT < 23) {
            infoView.setTextAppearance(getApplicationContext(), android.R.style.TextAppearance_Small);
        } else {
            infoView.setTextAppearance(android.R.style.TextAppearance_Small);
        }
        infoView.setId(View.generateViewId());
        RelativeLayout.LayoutParams infoLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        infoLayout.addRule(RelativeLayout.BELOW, titleView.getId());
        infoView.setLayoutParams(infoLayout);
        infoView.setText(Place + "\n" + Time + "\n" + Info);
        infoView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));

        //////////////// BUTTON ///////////////
        //

        Button signUpButton =  new Button(this);
        signUpButton.setBackgroundResource(R.drawable.alternativebuttons);
        //signUpButton.getBackground().setColorFilter(
                //getResources().getColor(R.color.blue_grey_500), PorterDuff.Mode.MULTIPLY);
        RelativeLayout.LayoutParams buttonLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        buttonLayout.addRule(RelativeLayout.CENTER_VERTICAL);
        buttonLayout.addRule(RelativeLayout.ALIGN_PARENT_END);
        signUpButton.setGravity(Gravity.CENTER);
        signUpButton.setLayoutParams(buttonLayout);
        signUpButton.setText("Sign Up");
        signUpButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.text));
        signUpButton.setId(View.generateViewId());
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Events.browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(Events.browserIntent);
            }
        });
        relativeLayout.setPadding(20, 10, 30, 10);
        //////////////Combine Everything///////////

        relativeLayout.addView(titleView);
        relativeLayout.addView(infoView);
        relativeLayout.addView(signUpButton);
        ((LinearLayout) (findViewById(R.id.scrollLayout))).addView(relativeLayout);
        return signUpButton.getId();

    }
}
