package com.teacupofcode.ved.interactapp;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.InputStream;

/**
 *  Created by Vasanth Sadhasivan on 10/30/2015.
 */
public class LoginActivity extends Activity implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult> {
    public static Bitmap profilePic = null;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 0;
    private static final int RC_PERM_GET_ACCOUNTS = 2;
    private static final String KEY_IS_RESOLVING = "is_resolving";
    private static final String KEY_SHOULD_RESOLVE = "should_resolve";
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatus;
    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;
    private boolean noSwitching=false;
    //private String currentAccountEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >20)
            setTheme(R.style.AppThemeV21);
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState != null){
            mIsResolving = savedInstanceState.getBoolean(KEY_IS_RESOLVING);
            mShouldResolve = savedInstanceState.getBoolean(KEY_SHOULD_RESOLVE);
        }
        hideShit();
        try {
            Bundle intentData = getIntent().getExtras();
            if (intentData.containsKey("Don't Close")){
                showShit();
                //currentAccountEmail = intentData.getString("Email");
                noSwitching=true;
            }
        }catch (Exception e){
            noSwitching=false;
        }


        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        ((SignInButton) findViewById(R.id.sign_in_button)).setSize(SignInButton.SIZE_WIDE);
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) ==  PackageManager.PERMISSION_DENIED
                && savedInstanceState != null) {
            Toast.makeText(getApplicationContext(), "You need to allow Contact permission...", Toast.LENGTH_LONG).show();
            checkAccountsPermission();
        }
        findViewById(R.id.sign_in_button).setEnabled(false);
        mStatus = (TextView) findViewById(R.id.status);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                .addScope(new Scope(Scopes.EMAIL))
                .build();
        checkAccountsPermission();
        Log.v(TAG, "LoginActivity Created + Accounts Permission Checked");
        mStatus.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG ,"LoginActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        Log.v(TAG, "LoginActivity onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_RESOLVING, mIsResolving);
        outState.putBoolean(KEY_SHOULD_RESOLVE, mShouldResolve);
        super.onSaveInstanceState(outState);
        Log.v(TAG, "LoginActivity onSaveInstanceState");
    }

    @SuppressWarnings("deprecation")
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            Log.w(TAG,"updateUI passed true");
            Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            Log.w(TAG, String.valueOf(currentPerson == null));
            if ((currentPerson != null)) {
                String personPhotoUrl = currentPerson.getImage().getUrl();
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 350;
                new LoadProfileImage().execute(personPhotoUrl);
                if (checkAccountsPermission() && !(noSwitching)) {
                    Log.w(TAG,"MainActivity started");
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("Name", currentPerson.getDisplayName());
                    i.putExtra("Email", Plus.AccountApi.getAccountName(mGoogleApiClient));
                    startActivity(i);
                }
                Log.w(TAG, "MainActivity started");
                // Show signed-in user's name

                mStatus.setText(getString(R.string.signed_in_fmt, currentPerson.getDisplayName()));

                ((TextView) findViewById(R.id.email)).setText(Plus.AccountApi.getAccountName(mGoogleApiClient));
                // Show users' email address (which requires GET_ACCOUNTS permission)
                /*if (checkAccountsPermission() && !(noSwitching)) {
                    Log.w("poop","poop2");
                    Intent i = new Intent(this, Home.class);
                    i.putExtra("Name", name);
                    i.putExtra("Email", currentAccount);
                    startActivity(i);
                }*/
            }

            else {
                // If getCurrentPerson returns null there is generally some error with the
                // configuration of the application (invalid Client ID, Plus API not enabled, etc).
                Log.w(TAG, getString(R.string.error_null_person));
                mStatus.setText(getString(R.string.signed_in_err));
            }

            // Set button visibility
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            // Show signed-out message and clear email field
            /*if(flag){
                mStatus.setText("Signed out" );
                flag = false;
            }
            else
                mStatus.setText("Not an ORHS account \nSigned out" );*/
            ((TextView) findViewById(R.id.email)).setText("");

            // Set button visibility
            findViewById(R.id.sign_in_button).setEnabled(true);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private boolean checkAccountsPermission() {
        final String perm = Manifest.permission.GET_ACCOUNTS;
        int permissionCheck = ContextCompat.checkSelfPermission(this, perm);
        Log.w(TAG, String.valueOf(permissionCheck));
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            // We have the permission
            return true;
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            // Need to show permission rationale, display a snackbar and then request
            // the permission again when the snackbar is dismissed.
            Snackbar.make(findViewById(R.id.main_layout2),
                    R.string.contacts_permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Request the permission again.
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{perm},
                                    RC_PERM_GET_ACCOUNTS);
                        }

                    }).show();
            return false;
        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{perm},
                    RC_PERM_GET_ACCOUNTS);
            return false;
        }
    }

    private void showSignedInUI() {
        updateUI(true);
    }

    private void showSignedOutUI() {
        showShit();
        updateUI(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    @Override @SuppressWarnings("deprecation")
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
        boolean b = false;
        if (requestCode == RC_PERM_GET_ACCOUNTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "onRequestPermissionsResult passed Permission Granted");
                try {
                    b = Plus.AccountApi.getAccountName(mGoogleApiClient).contains("eduhsd.k12.ca.us");
                } catch (Exception e) {
                    checkAccountsPermission();
                }

                if (b) {
                    showSignedInUI();
                    Log.w(TAG, "AYy");
                } else {
                    onDisconnectClicked();
                    Log.w(TAG, "Fake email");
                    mStatus.setText(R.string.not_orhs);
                    Toast.makeText(getApplicationContext(), "Not an ORHS account.", Toast.LENGTH_LONG).show();
                }
                }
            } else {
                Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
                Toast.makeText(getApplicationContext(), "You need Contact Permissions to login...", Toast.LENGTH_LONG).show();
                mStatus.setText(R.string.sign_out);
            }
        }

    @Override @SuppressWarnings("deprecation")
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.

        try {
            Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
            Log.d(TAG, "onConnected:" + bundle);
            mShouldResolve = false;
            Log.w(TAG, "onConnected");
            boolean containsEmail = (Plus.AccountApi.getAccountName(mGoogleApiClient).contains("eduhsd.k12.ca.us"));
            Log.w(TAG, String.valueOf(containsEmail));
            boolean personIsSignedIn = (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null);
            Log.w(TAG, String.valueOf(personIsSignedIn));
            // Show the signed-in UI
            if (containsEmail && personIsSignedIn) {
                showSignedInUI();
                Log.w(TAG, "Email is good + personIsSignedIn");
            } else {
                if (!containsEmail) {
                    Log.w(TAG, "No eduhsd");
                    mStatus.setText("Not an ORHS account \n Signed Out");
                    Toast.makeText(getApplicationContext(), "Not an ORHS account.", Toast.LENGTH_LONG ).show();
                } else {
                    mStatus.setText("Error \n Signed Out");
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                }
                onSignOutClicked();
                Log.w(TAG, "SignIn Failed");
            }
        }catch(SecurityException ignored)
        {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        Log.w(TAG, "onConnectionSuspended:" + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Log.w(TAG, String.valueOf(!mIsResolving && mShouldResolve));
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                showErrorDialog(connectionResult);
            }
        } else {
            // Show the signed-out UI
            Log.w(TAG, "onConnectionFailed signOutUI");
            showSignedOutUI();
        }
    }

    private void showErrorDialog(ConnectionResult connectionResult) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, RC_SIGN_IN,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                Log.w(TAG, "showErrorDialog");
                                mShouldResolve = false;
                                showSignedOutUI();
                            }
                        }).show();
            } else {
                Log.w(TAG, "Google Play Services Error:" + connectionResult);
                String errorString = apiAvailability.getErrorString(resultCode);
                Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();

                mShouldResolve = false;
                showSignedOutUI();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                onSignInClicked();
                break;
            case R.id.sign_out_button:
                onSignOutClicked();
                break;
            case R.id.disconnect_button:
                onDisconnectClicked();
                break;
        }
    }

    private void onSignInClicked() {
        // User clicked the sign-in button, so begin the sign-in process and automatically
        // attempt to resolve any errors that occur.
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            mShouldResolve = true;
            mGoogleApiClient.connect();

            // Show a message to the user that we are signing in.
            mStatus.setText(R.string.signing_in);
            noSwitching = false;
        }
        else{
            Toast.makeText(this, "You need Contact Permissions to login...", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    RC_PERM_GET_ACCOUNTS);
        }

    }

    @SuppressWarnings("deprecation")
    private void onSignOutClicked() {
        // Clear the default account so that GoogleApiClient will not automatically
        // connect in the future.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        Log.w(TAG, "onSignOutClicked");
        mStatus.setText("");
        showSignedOutUI();
    }

    @SuppressWarnings("deprecation")
    private void onDisconnectClicked() {
        // Revoke all granted permissions and clear the default account.  The user will have
        // to pass the consent screen to sign in again.
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
        Log.w(TAG, "onDisconnectClicked");
        mStatus.setText("");
        showSignedOutUI();
    }

    @Override
    public void onResult(@NonNull People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {
                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    Log.d(TAG, "Display name: " + personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.release();
            }
        } else {
            Log.e(TAG, "Error requesting people data: " + peopleData.getStatus());

        }
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

        LoadProfileImage() {
        }

        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urls[0]).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }
        Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
            int targetWidth = 350;
            int targetHeight = 350;
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight,Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);

            canvas.clipPath(path);
            canvas.drawBitmap(scaleBitmapImage,
                    new Rect(0, 0, scaleBitmapImage.getWidth(),
                            scaleBitmapImage.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);
            return targetBitmap;
        }
        protected void onPostExecute(Bitmap result) {
            LoginActivity.profilePic = getRoundedShape(result);
        }
    }

    public void hideShit(){
        findViewById(R.id.main_layout).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.launch_screen));
        findViewById(R.id.main_layout2).setVisibility(View.INVISIBLE);
    }

    public void showShit(){
        findViewById(R.id.main_layout2).setVisibility(View.VISIBLE);
    }
}