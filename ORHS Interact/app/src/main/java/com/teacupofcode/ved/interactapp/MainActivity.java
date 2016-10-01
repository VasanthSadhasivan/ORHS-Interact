package com.teacupofcode.ved.interactapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.os.AsyncTask.THREAD_POOL_EXECUTOR;

/**
 * Whole ducking implementation created by Naveen Gopalan.
 */
public class MainActivity extends AppCompatActivity{

    static SectionsPagerAdapter mSectionsPagerAdapter;
    static String nameH, emailH;
    static final String KEY2 = "hey2";
    static final String KEY3 = "oop";
    static final String TAG = "MainActivity";
    static FragmentMaker frag;
    boolean isDone = false;
    boolean isNotDone = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >20)
            setTheme(R.style.AppThemeV21);
        super.onCreate(savedInstanceState);
        frag = new FragmentMaker();
        new MySpreadsheetIntegration().executeOnExecutor(THREAD_POOL_EXECUTOR, frag);
        new Timer().executeOnExecutor(THREAD_POOL_EXECUTOR);
        //if (isNotDone && this.isDone)
        setContentView(R.layout.activity_main);
        Bundle intentData = getIntent().getExtras();
        nameH = intentData.getString("Name");
        emailH = intentData.getString("Email");
    }

    public static void runFragment(FragmentMaker f) {
        f.execute();
    }

    public class FragmentMaker extends AsyncTask<Void, Void, Fragment[]> {

        @Override
        protected Fragment[] doInBackground(Void... params) {
            Fragment[] a = new Fragment[3];
            a[0] = HomeFrag.newInstance(0);
            a[1] = Events.newInstance(1);
            a[2] = Info.newInstance(2);
            return a;
        }

        @Override
        protected void onPostExecute(Fragment[] result) {
            //Set up the ViewPagerAdapter.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mSectionsPagerAdapter.addFragment(result[0], "Home");
            mSectionsPagerAdapter.addFragment(result[1], "Events");
            mSectionsPagerAdapter.addFragment(result[2], "Info");
            //Set up ViewPager.
            ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOffscreenPageLimit(3);
            //Set up TabLayout.
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(mViewPager);

            //noinspection ConstantConditions
            tabLayout.getTabAt(0).setIcon(R.drawable.home);
            //noinspection ConstantConditions
            tabLayout.getTabAt(1).setIcon(R.drawable.event);
            //noinspection ConstantConditions
            tabLayout.getTabAt(2).setIcon(R.drawable.info);

            tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getApplicationContext(), R.color.background));
            isNotDone = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        Log.v("2", "MainActivity onSaveInstanceState");
        savedState.putString(KEY2, nameH);
        savedState.putString(KEY3, emailH);
    }

    @Override
    public void onBackPressed() {
        returnClickedHome(findViewById(R.id.returnbutton));
    }

    public void returnClickedHome(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        i.putExtra("Name", nameH);
        i.putExtra("Email", emailH);
        i.putExtra("Don't Close", "PLEASSEEE");
        startActivity(i);
    }

    public void hoursClickedHome(View view) {
        Uri uri = Uri.parse("https://docs.google.com/spreadsheets/d/13CkOpYGJA3V6fOwlli6XAu133F7TptnIW-eZjFhDZjc/"); // missing 'http://' will cause crashed
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(i);
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
        public void onActivityCreated(Bundle bund){
            super.onActivityCreated(bund);
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

        @Override @SuppressLint("SetTextI18n")
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.home, container, false);
            currentEvent = (TextView) rootView.findViewById(R.id.currentEvent);
            Log.v(TAG, ""+MySpreadsheetIntegration.upcoming );
            currentEvent.setText("Upcoming Event:\n" + MySpreadsheetIntegration.upcoming);
            tname = (TextView) rootView.findViewById(R.id.homeTitle);
            tname.setText(nameH);
            ViewFlipper flipper = (ViewFlipper) rootView.findViewById(R.id.flipper1);
            flipper.startFlipping();
            mFlipping=1;
            return rootView;
        }
        private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {

            LoadProfileImage() {
            }

            protected Bitmap doInBackground(String... urls) {
                //noinspection StatementWithEmptyBody
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
        HashMap<String, List<String>> details;
        List<String> title;
        static Intent browserIntent = new Intent();
        private static final String ARG_SECTION_NUMBER = "section_number";
        ExpandableListAdapter listAdapter;
        ExpandableListView expandableListView;
        public Events() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.events, container, false);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState){
            super.onActivityCreated(savedInstanceState);
            details = getInfo();
            title = new ArrayList<>(details.keySet());
            listAdapter = new ExpandableListAdapt(getContext(), details, title);
            expandableListView = ((ExpandableListView) getActivity().findViewById(R.id.exl_list));
            expandableListView.setChildDivider(getResources().getDrawable(R.color.background2));
            //setAllViewInfo();
            expandableListView.setAdapter(listAdapter);
            // Listview Group expanded listener
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



        public HashMap<String, List<String>> getInfo(){
            HashMap<String, List<String>> eventDescription = new HashMap<>();
            for (Event e: MySpreadsheetIntegration.eventList) {
                List<String> cricket = new ArrayList<>();
                cricket.add("Time: " + e.getDate());
                cricket.add("Address: " + e.getLocation());
                cricket.add("Description: " + e.getDescription());
                eventDescription.put(e.getName(), cricket);
                Log.v("hi", "hi");
            }
            return eventDescription;
        }

        //Later on, fix the method to have info and location
       public void setAllViewInfo() {
            for(Event event : MySpreadsheetIntegration.eventList) {
                generateView(event.getName(), event.getDescription() ,event.getLocation(), event.getDate(), event.getLink());
            }
        }

        @SuppressWarnings("deprecation") public int generateView(String Title, String Info, final String Place, String Time, final String link) {
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
            signUpButton.setText(R.string.signing_up);
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
            //////////////Combine Everything//////////////
            /*relativeLayout.addView(titleView);
            relativeLayout.addView(infoView);
            relativeLayout.addView(signUpButton);
            relativeLayout.addView(locationButton);
            LinearLayout layout = ((LinearLayout) (getActivity().findViewById(R.id.exl_list)));
            if (layout == null)
                return 1;
            layout.addView(relativeLayout);*/
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

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        void addFragment(Fragment fragment, String title) {
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


    public class Timer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(3000);
            return null;
        }
        @Override
        protected void onPostExecute(Void a) {
            if (isNotDone) {
                (findViewById(R.id.main_content)).setBackgroundColor(getResources().getColor(R.color.background2));
                (findViewById(R.id.loading_spinner)).setVisibility(View.VISIBLE);
            }
        }
    }

}
