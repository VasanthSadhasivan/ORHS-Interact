package com.teacupofcode.ved.interactapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Whole ducking implementation created by Naveen Gopalan.
 */
public class MainActivity extends AppCompatActivity implements TabLayout.OnClickListener{

    static SectionsPagerAdapter mSectionsPagerAdapter;
    static String nameH, emailH;
    static final String KEY2 = "hey2";
    static final String KEY3 = "oop";
    static final String TAG = "MainActivity";
    Fragment h;
    Fragment e;
    Fragment i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >20)
            setTheme(R.style.AppThemeV21);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Bundle intentData = getIntent().getExtras();
        nameH = intentData.getString("Name");
        emailH = intentData.getString("Email");
        //Set up the ViewPagerAdapter.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        h=HomeFrag.newInstance(1);
        e=Events.newInstance(2);
        i=Info.newInstance(3);
        mSectionsPagerAdapter.addFragment(h, "Home");
        mSectionsPagerAdapter.addFragment(e, "Events");
        mSectionsPagerAdapter.addFragment(i, "Info");
        //Set up ViewPager.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        //Set up TabLayout.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.event);
        tabLayout.getTabAt(2).setIcon(R.drawable.info);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.background));

    }

   @Override
    protected void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        Log.v("2", "FACK");
        savedState.putString(KEY2, nameH);
        savedState.putString(KEY3, emailH);
    }

    public void returnClickedHome(View view) {
        if(h != null)
            getSupportFragmentManager().beginTransaction().remove(h).commit();
        if(e != null)
            getSupportFragmentManager().beginTransaction().remove(e).commit();
        if(i != null)
            getSupportFragmentManager().beginTransaction().remove(i).commit();
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("Name", nameH);
        i.putExtra("Email", emailH);
        i.putExtra("DontClose", "PLEASSEEE");
        startActivity(i);
    }

    public void hoursClickedHome(View view) {
        //15-16 ORHS URL Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/1MI6BIaeNRsti2VtaFJGKR3HdT1P0KLVJx7Au-WhvDS8/edit"); // missing 'http://' will cause crashed
        Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/13CkOpYGJA3V6fOwlli6XAu133F7TptnIW-eZjFhDZjc/"); // missing 'http://' will cause crashed
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {

    }

    public static class HomeFrag extends Fragment {
        TextView tname;
        TextView currentEvent;
        int mFlipping = 0;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public HomeFrag() { }

        @Override
        public void onActivityCreated(Bundle poop){
            super.onActivityCreated(poop);
            new LoadProfileImage().execute();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HomeFrag newInstance(int sectionNumber) {
            HomeFrag fragment = new HomeFrag();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home, container, false);

            MySpreadsheetIntegration myspreadsheetobject = new MySpreadsheetIntegration();
            myspreadsheetobject.execute("whatever");
            while(MySpreadsheetIntegration.eventList==null)
            {
            }
            currentEvent = (TextView) rootView.findViewById(R.id.currentEvent);
            Log.v(TAG, ""+MySpreadsheetIntegration.eventList.get(0).getName() );
            currentEvent.setText("Upcoming Event:\n" + MySpreadsheetIntegration.eventList.get(0).getName());
            tname = (TextView) rootView.findViewById(R.id.homeTitle);
            tname.setText(nameH);
            ViewFlipper flipper = (ViewFlipper) rootView.findViewById(R.id.flipper1);
            flipper.startFlipping();
            mFlipping=1;
            return rootView;
        }
        private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

            public LoadProfileImage() {
            }

            protected Bitmap doInBackground(String... urls) {
                while(LoginActivity.profilePic == null){

                }
                return LoginActivity.profilePic;
            }

            protected void onPostExecute(Bitmap result) {
                ((ImageView) getActivity().findViewById(R.id.profilePic)).setImageBitmap(result);
            }
        }
    }

    public static class Events extends Fragment {

        static Intent browserIntent = new Intent();
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Events() { }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);
            setAllViewInfo();
        }

        public static Events newInstance(int sectionNumber) {
            Events fragment = new Events();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.events, container, false);
            return rootView;
        }

        //Later on, fix the method to have info and location
       public void setAllViewInfo() {
            for(Event event : MySpreadsheetIntegration.eventList) {
                generateView(event.getName(), event.getDescription() ,event.getLocation(), event.getDate(), event.getLink());
            }
        }

        public int generateView(String Title, String Info, final String Place, String Time, final String link) {
            //Creating Relative Layout Programmatically
            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            //CHANGE LATER
            relativeLayout.setId(View.generateViewId());
            relativeLayout.setBackgroundResource(R.drawable.borders);
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            rlp.topMargin=15;
            relativeLayout.setLayoutParams(rlp);
            ////////////// TEXT VIEWS //////////////

            TextView titleView = new TextView(getActivity());
            titleView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text));
            titleView.setBackgroundResource(R.color.background2);
            if (Build.VERSION.SDK_INT < 23) {
                titleView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            } else {
                titleView.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            //CHANGE LATER
            titleView.setId(View.generateViewId());
            titleView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            titleView.setText(Title);
            titleView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text));
            TextView infoView = new TextView(getActivity());
            if (Build.VERSION.SDK_INT < 23) {
                infoView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            } else {
                infoView.setTextAppearance(android.R.style.TextAppearance_Small);
            }
            infoView.setId(View.generateViewId());
            RelativeLayout.LayoutParams infoLayout = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            infoLayout.addRule(RelativeLayout.BELOW, titleView.getId());
            infoView.setLayoutParams(infoLayout);
            String placeToDisplay = Place;
            if(Place.length()>15)
                placeToDisplay = placeToDisplay.substring(0,16)+"...";
            if(Info.length()>30)
                Info = Info.substring(0,31)+"...";

            infoView.setText(placeToDisplay + "\n" + Time + "\n" + Info);
            infoView.setTextColor(ContextCompat.getColor(getActivity(), R.color.text));

            //////////////// BUTTON ///////////////

            Button signUpButton =  new Button(getActivity());
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
            signUpButton.setTextColor(ContextCompat.getColor(getActivity(), R.color.text));
            signUpButton.setId(View.generateViewId());
            signUpButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Events.browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(Events.browserIntent);
                }
            });


            //////////////// BUTTON ///////////////

            ImageButton locationButton =  new ImageButton(getActivity());
            locationButton.setImageResource(R.drawable.marker);
            RelativeLayout.LayoutParams locationButtonLayout = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            locationButtonLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            locationButtonLayout.addRule(RelativeLayout.BELOW, infoView.getId());
            locationButtonLayout.setMargins(0,10,0,0);
            locationButton.setLayoutParams(locationButtonLayout);
            locationButton.setId(View.generateViewId());
            locationButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(String.format("geo:0,0?q=%s",
                                    URLEncoder.encode(Place)))));
                }
            });


            relativeLayout.setPadding(20, 10, 30, 10);
            //////////////Combine Everything///////////

            relativeLayout.addView(titleView);
            relativeLayout.addView(infoView);
            relativeLayout.addView(signUpButton);
            relativeLayout.addView(locationButton);
            LinearLayout layout = ((LinearLayout) (getActivity().findViewById(R.id.scrollLayout)));
            if (layout == null)
                return 1;
            layout.addView(relativeLayout);
            return signUpButton.getId();

        }
    }

    public static class Info extends Fragment implements View.OnClickListener{
        MediaPlayer mPlayer;
        int counter = 0;
        ImageView img;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Info() { }

        public static Info newInstance(int sectionNumber) {
            Info fragment = new Info();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.info, container, false);
            img = (ImageView) rootView.findViewById(R.id.aboutWAAD);
            img.setOnClickListener(this);
            Button a = (Button) rootView.findViewById(R.id.joinbutton);
            a.setOnClickListener(this);
            Button b = (Button) rootView.findViewById(R.id.websitebutton);
            b.setOnClickListener(this);
            Button c = (Button) rootView.findViewById(R.id.rescourcesbutton);
            c.setOnClickListener(this);
            Button d = (Button) rootView.findViewById(R.id.githubbutton);
            d.setOnClickListener(this);

            mPlayer = MediaPlayer.create(getActivity(), R.raw.tonymontana);
            return rootView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.joinbutton:
                    Uri uri1 = Uri.parse("https://goo.gl/forms/k9uHclWR4jazJjYj2");
                    Intent i1 = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(i1);
                    break;
                case R.id.websitebutton:
                    Uri uri2 = Uri.parse("https://orhsinteract.com");
                    Intent i2 = new Intent(Intent.ACTION_VIEW, uri2);
                    startActivity(i2);
                    break;
                case R.id.rescourcesbutton:
                    Uri uri3 = Uri.parse("https://drive.google.com/folderview?id=0BwJLvdTM6Ac4UWlqSHlKTGhxRFk&usp=sharing");
                    Intent i3 = new Intent(Intent.ACTION_VIEW, uri3);
                    startActivity(i3);
                    break;
                case R.id.githubbutton:
                    Uri uri = Uri.parse("https://github.com/ORHS-Web-App-Dev");
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(i);
                    break;
                case R.id.aboutWAAD:
                    counter++;
                    if (counter>9) {
                        mPlayer.start();
                        img.setImageResource(R.mipmap.aboutwaad);
                    }
                    break;
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        private ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private ArrayList<String> mTitle = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mTitle.add(title);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle.get(position);
        }
    }

}
