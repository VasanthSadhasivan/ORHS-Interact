package com.teacupofcode.dev.interactapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.navdrawer.SimpleSideDrawer;

/**
 * Created by Admin on 10/26/2015.
 */
public class Events extends Activity{

    SimpleSideDrawer slide_me;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);
        slide_me=new SimpleSideDrawer(this);
        slide_me.setLeftBehindContentView(R.layout.left_menu);

    }
    public void onLeftClick(View v)
    {
        slide_me.toggleLeftDrawer();
    }
}
