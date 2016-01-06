package com.teacupofcode.dev.interactapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Events extends Activity{

    String name; //string name
    String email;
    static ArrayList<String> events = new ArrayList<String>();
    static ArrayList<String> dates = new ArrayList<String>();

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
        dates=MySpreadsheetIntegration.dateList;
        events=MySpreadsheetIntegration.filterEvents(MySpreadsheetIntegration.eventList);
        Log.w("AYY", dates.get(0));
        setAllViewInfo();
    }
    //Later on, fix the method to have info and location
    public void setAllViewInfo()
    {
        for(int i=0; i<Events.events.size(); i++)
        {
            generateView(Events.events.get(i), "-----", "-----", Events.dates.get(i));
            //generateView(titleList.get(i), "-----", "-----", "====-=-=");
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

    public int generateView(String Title, String Info, String Place, String Time)
    {
        //Creating Relative Layout Programmatically
        RelativeLayout relativeLayout = new RelativeLayout(this);
        //CHANGE LATER
        relativeLayout.setId(View.generateViewId());
        relativeLayout.setBackgroundResource(R.drawable.borders);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rlp.topMargin=15;
        relativeLayout.setLayoutParams(rlp);
        ////////////// TEXT VIEWS //////////////

        TextView titleView = new TextView(this);
        titleView.setTextAppearance(this, android.R.style.TextAppearance_Large);
        //CHANGE LATER
        titleView.setId(View.generateViewId());
        titleView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        titleView.setText(Title);
        TextView infoView = new TextView(this);
        infoView.setTextAppearance(this, android.R.style.TextAppearance_Small);
        infoView.setId(View.generateViewId());
        RelativeLayout.LayoutParams infoLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        infoLayout.addRule(RelativeLayout.BELOW, titleView.getId());
        infoView.setLayoutParams(infoLayout);
        infoView.setText(Place + "\n" + Time + "\n" + Info);

        //////////////// BUTTON /////////////////

        Button signUpButton =  new Button(this);
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
        signUpButton.setId(View.generateViewId());

        //////////////Combine Everything///////////

        relativeLayout.addView(titleView);
        relativeLayout.addView(infoView);
        relativeLayout.addView(signUpButton);
        ((LinearLayout) (findViewById(R.id.scrollLayout))).addView(relativeLayout);
        return signUpButton.getId();

    }
}
