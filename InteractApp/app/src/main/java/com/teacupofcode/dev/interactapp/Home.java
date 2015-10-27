package com.teacupofcode.dev.interactapp;

import java.lang.*;
import com.navdrawer.SimpleSideDrawer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 10/26/2015.
 */
public class Home extends Activity{

    TextView contactInfo;
    TextView tname; //name on text view
    ImageView menu;
    String name; //string name
    String email;
    String phoneNumber;
    SimpleSideDrawer slide_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        slide_me=new SimpleSideDrawer(this);
        slide_me.setLeftBehindContentView(R.layout.left_menu);
        Bundle intentData = getIntent().getExtras();
        name=intentData.getString("Name");
        email=intentData.getString("Email");
        contactInfo = (TextView) findViewById(R.id.contactInfo);
        menu = (ImageView) findViewById(R.id.menu_home);
        tname = (TextView) findViewById(R.id.homeName);
        tname.setText(name);
        contactInfo.setText("xxxxxx.com\n(910)-xxx-xxxx\n"+email);
    }
    public void onLeftClick(View v)
    {
        slide_me.toggleLeftDrawer();
    }
}
