package com.teacupofcode.dev.interactapp;

import android.Manifest;
import java.lang.*;
import com.navdrawer.SimpleSideDrawer;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.teacupofcode.dev.interactapp.R;

import org.w3c.dom.Text;

/**
 * Created by Admin on 10/26/2015.
 */
public class Home extends Activity{

    TextView contactInfo;
    TextView tname; //name on text view
    String name; //string name
    String email;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Bundle intentData = getIntent().getExtras();
        name=intentData.getString("Name");
        email=intentData.getString("Email");
        contactInfo = (TextView) findViewById(R.id.contactInfo);
        tname = (TextView) findViewById(R.id.homeName);
        tname.setText(name);
        contactInfo.setText("xxxxxx.com\n(910)-xxx-xxxx\n"+email);
    }

}
