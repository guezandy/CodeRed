package com.lunadeveloper.codered;


import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.adapter.LaunchFragmentAdapter;
import com.lunadeveloper.codered.fragment.DialogAddScheduleFragment;
import com.lunadeveloper.codered.fragment.LaunchFragment;
import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.lunadeveloper.codered.util.SystemUiHider;
import com.viewpagerindicator.LinePageIndicator;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends ActionBarActivity {
    public String TAG = LaunchActivity.class.getSimpleName();

    LaunchFragmentAdapter mAdapter;
    ViewPager mPager;
    LinePageIndicator mIndicator;
    Button login;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.container);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);

/*
        ArrayList<String> days = new ArrayList<String>();
        days.add("W");
        ArrayList<String> day = new ArrayList<String>();
        day.add("T");

        ScheduleModel m = new ScheduleModel("ONE", days);
        m.saveInBackground();

        ParseQuery<ScheduleModel> query = ParseQuery.getQuery("ScheduleModel");
        query.whereContainedIn("week_days", day);
        query.findInBackground(new FindCallback<ScheduleModel>() {
            @Override
            public void done(List<ScheduleModel> scheduleModels, ParseException e) {
                for(ScheduleModel s : scheduleModels) {
                    System.out.println(s.getName()+ " " + s.getWeekDays());
                }
            }
        });
*/


        Boolean debug = true;
        if(debug) {
            signup.callOnClick();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmFireMissiles();
                //Intent i = new Intent(LaunchActivity.this, ParseLoginDispatchActivity.class);
                //startActivity(i);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LaunchActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        replaceFragment(new LaunchFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_main));
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "On Resume");
        replaceFragment(new LaunchFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_main));
    }
    /**
     * Handles adding all fragments to the view.
     * @param newFragment The fragment to add.
     * @param addToBackstack Whether this Fragment should appear in the backstack or not.
     * @param transition The transition animation to apply
     * @param backstackName The name
     */
    public void replaceFragment(android.support.v4.app.Fragment newFragment, boolean addToBackstack, int transition, String backstackName) {
        // use fragmentTransaction to replace the fragment
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, newFragment, backstackName);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(backstackName);
        }
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.commit();
    }


    public void confirmFireMissiles() {
        DialogAddScheduleFragment newFragment = new DialogAddScheduleFragment();
        newFragment.show(getSupportFragmentManager(), "missiles");
    }
}
