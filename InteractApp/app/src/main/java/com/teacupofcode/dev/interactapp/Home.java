package com.teacupofcode.dev.interactapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by Vasanth Sadhasivan on 10/26/2015.
 */
public class Home extends Activity{

    TextView contactInfo;
    TextView tname; //name on text view
    ImageView menu;
    String name; //string name
    String email;
    String phoneNumber;
    int mFlipping = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle intentData = getIntent().getExtras();

        //if(intentData !=null) {
          //  if (intentData.containsKey("Name"))
                name = intentData.getString("Name");
          //  if (intentData.containsKey("Email"))
                email = intentData.getString("Email");
       // }
       // contactInfo = (TextView) findViewById(R.id.contactInfo);
        tname = (TextView) findViewById(R.id.homeTitle);
        tname.setText(name);
        //contactInfo.setText("xxxxxx.com\n(910)-xxx-xxxx\n" + email);

        ViewFlipper flipper = (ViewFlipper) findViewById(R.id.flipper1);
        flipper.startFlipping();
        mFlipping=1;


    }

    public void returnClickedHome(View view) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        i.putExtra("DontClose", "PLEASSEEE");
        startActivity(i);
    }

    public void hoursClickedHome(View view) {
        Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/1MI6BIaeNRsti2VtaFJGKR3HdT1P0KLVJx7Au-WhvDS8/edit"); // missing 'http://' will cause crashed
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    public void homeClickedHome(View view) {

    }

    public void infoClickedHome(View view) {
        Intent i = new Intent(this, Info.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }

    public void eventsClickedHome(View view) {
        Intent i = new Intent(this, Events.class);
        i.putExtra("Name", name);
        i.putExtra("Email", email);
        startActivity(i);
    }
}
