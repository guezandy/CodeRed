package com.lunadeveloper.codered;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import com.lunadeveloper.codered.fragment.CalFragment;
import com.lunadeveloper.codered.fragment.CalendarFragment;
import com.lunadeveloper.codered.fragment.CanIGoOutFragment;
import com.lunadeveloper.codered.fragment.DrawerFragment;
import com.lunadeveloper.codered.fragment.ListCalendarFragment;
import com.lunadeveloper.codered.fragment.SearchFriendsFragment;
import com.lunadeveloper.codered.fragment.NotificationsFragment;
import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.lunadeveloper.codered.ui.SlidingUpPanelLayout;
import com.lunadeveloper.codered.ui.SlidingUpPanelLayout.PanelSlideListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**Debug TAG **/
    public static String TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** Sliding drawer **/
    private SlidingUpPanelLayout mLayout;

    /**Loading spinner **/
    private ProgressBar spinner;


    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();
    public static ArrayList<String> locations = new ArrayList<String>();
    public String Holidays[] = {
            "New Year’s Day	Thursday",
            "Martin Luther King, Jr.",
            "Good Friday",
            "Easter Sunday",
            "Memorial Day",
            "Independence Day",
            "Presidents' Day",
            "Labor Day",
            "Columbus Day",
            "General Election Day",
            "Veterans Day",
            "Thanksgiving Day",
            "Lincoln’s Birthday",
            "Washington’s Birthday",
            "Christmas Day"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseService parseService = new ParseService(this.getBaseContext());
        spinner = (ProgressBar) findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);
        syncCalendar(parseService); //syncs the user calendar on login
        spinner.setVisibility(View.GONE);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                //Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelExpanded(View panel) {
               // Log.i(TAG, "onPanelExpanded");

            }

            @Override
            public void onPanelCollapsed(View panel) {
               // Log.i(TAG, "onPanelCollapsed");

            }

            @Override
            public void onPanelAnchored(View panel) {
                Log.i(TAG, "onPanelAnchored");
            }

            @Override
            public void onPanelHidden(View panel) {
                Log.i(TAG, "onPanelHidden");
            }
        });

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch(position) {
            case 0:
                //ON START
                replaceFragment(new CanIGoOutFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                replaceDrawerInfoFragment(new DrawerFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                replaceDrawerFragment(new ListCalendarFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                break;
            case 1:
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                CodeRedApplication.setGoOutDate(dateFormat.format(c.getTime()));
                replaceFragment(new SearchFriendsFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                replaceDrawerInfoFragment(new DrawerFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                replaceDrawerFragment(new NotificationsFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                break;
            case 2:
                Calendar c2 = Calendar.getInstance();
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("MM-dd-yyyy");
                CodeRedApplication.setGoOutDate(dateFormat2.format(c2.getTime()));
                break;
            case 3:
                Calendar c3 = Calendar.getInstance();
                SimpleDateFormat dateFormat3 = new SimpleDateFormat("MM-dd-yyyy");
                CodeRedApplication.setGoOutDate(dateFormat3.format(c3.getTime()));
                logout();
                break;
            default:
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_main);
                break;
            case 2:
                mTitle = getString(R.string.title_search);
                break;
            case 3:
                mTitle = getString(R.string.title_invite);
                break;
            case 4:
                mTitle = "Logout";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);

            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings: {
                return true;
            }
            case R.id.action_sync: {
                ParseService parseService = new ParseService(this.getBaseContext());
                syncCalendar(parseService);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null && mLayout.isPanelExpanded() || mLayout.isPanelAnchored()) {
            mLayout.collapsePanel();
        } else {
            super.onBackPressed();
        }
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
        Log.i(TAG, "Initializing Fragment Transaction");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Log.i(TAG, "Replacing the fragment and calling backstack");
        fragmentTransaction.replace(R.id.container, newFragment, backstackName);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(backstackName);
        }
        Log.i(TAG, "setting the transition");
        fragmentTransaction.setTransition(transition);
        Log.i(TAG,"Commiting Transaction");
        fragmentTransaction.commit();
    }

    public void replaceDrawerInfoFragment(android.support.v4.app.Fragment newFragment, boolean addToBackstack, int transition, String backstackName) {
        // use fragmentTransaction to replace the fragment
        Log.i(TAG, "Initializing Fragment2 Transaction");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container2, newFragment, backstackName);
        if (addToBackstack) {
            //fragmentTransaction.addToBackStack(backstackName);
        }
        fragmentTransaction.setTransition(transition);
        Log.i(TAG,"Commiting 2 Transaction");
        fragmentTransaction.commit();
    }

    public void replaceDrawerFragment(android.support.v4.app.Fragment newFragment, boolean addToBackstack, int transition, String backstackName) {
        // use fragmentTransaction to replace the fragment
        Log.i(TAG, "Initializing Fragment3 Transaction");
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container3, newFragment, backstackName);
        if (addToBackstack) {
            //fragmentTransaction.addToBackStack(backstackName);
        }
        fragmentTransaction.setTransition(transition);
        Log.i(TAG,"Commiting 3 Transaction");
        fragmentTransaction.commit();
    }

    public void openDrawerThroughFragment() {
        mLayout.setAnchorPoint(0.7f);
        mLayout.expandPanel(0.7f);
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTime(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String addOneToDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        calendar.add(Calendar.DATE, 1);
        return formatter.format(calendar.getTime());
    }

    public static int getHour(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh");
        SimpleDateFormat amOrPm = new SimpleDateFormat(
                "a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String hour = formatter.format(calendar.getTime());
        int mhour = Integer.parseInt(hour);
        if(amOrPm.format(calendar.getTime()).equals("PM")) {
            mhour = ((Integer.parseInt(hour) + 12)%24);
        }
        return mhour;
    }

    public void syncCalendar(ParseService parseService) {
        Context con = this.getBaseContext();
        Set<String> calendars = new HashSet<String>();
        Calendar startTime = Calendar.getInstance();
        Calendar endTime= Calendar.getInstance();
        endTime.add(Calendar.MONTH, 1);
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTEND + " <= " + endTime.getTimeInMillis() + " ))";
        final Cursor cursor = getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, selection,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        locations.clear();

        //TODO: delete all entries from this person
        //Query limited to 100 entries
        ParseQuery<ParseEventModel> deleteQuery = ParseQuery.getQuery("ParseEventModel");
        deleteQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        deleteQuery.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                System.out.println("Found this many entries to delete: "+parseEventModels.size());
                for(ParseEventModel event : parseEventModels) {
                    event.deleteInBackground();
                }
            }
        });

        //TODO: Insert only these entries
        System.out.println("Number of events: "+cursor.getCount());
        System.out.println("Length: "+ CNames.length);

        for (int i = 0; i < CNames.length; i++) { //for each event

            System.out.println(i+ " "+cursor.getString(1));
            System.out.println("START HOUR: "+ getHour(Long.parseLong(cursor.getString(3))));
            //minimum we need is a title and start time
            if(cursor.getString(1) != null && cursor.getString(3) != null) {
                ParseEventModel n = new ParseEventModel();
                n.setTitle(cursor.getString(1));
                String eStartDate = getDate(Long.parseLong(cursor.getString(3)));
                String eStartTime = getTime(Long.parseLong(cursor.getString(3)));
                if(eStartTime.equals("04:00:00 PM")) {
                    eStartDate = addOneToDate(Long.parseLong(cursor.getString(3)));
                }
                n.setStartDate(eStartDate);
                n.setStartTime(eStartTime);
                n.setStartHour(getHour(Long.parseLong(cursor.getString(3))));
                //if event is too early tomorrow
                if(getHour(Long.parseLong(cursor.getString(3))) <= Integer.parseInt(ParseUser.getCurrentUser().getString("early"))) {
                    n.setTooEarly(true);
                } else {
                    n.setTooEarly(false);
                }
                n.setDescription(cursor.getString(2));
                if (cursor.getString(5) != null) {
                    n.setLocation(cursor.getString(5));
                }
                //is this one of the days off?
                if(Arrays.asList(Holidays).contains(cursor.getString(1))) {
                    n.setDayOff(true);
                } else {
                    n.setDayOff(false);
                }
                n.setUser();
                //n.put("hash", (cursor.getString(1) + cursor.getString(2) + Long.parseLong(cursor.getString(3))).hashCode());
                n.saveInBackground();
            }
            if(!cursor.isLast())
                cursor.moveToNext();

            /*nameOfEvent.add(cursor.getString(1));
            startDates.add(getDateAndTime(Long.parseLong(cursor.getString(3))));
            if (cursor.getString(4) != null) {
                endDates.add(getDateAndTime(Long.parseLong(cursor.getString(4))));
            }
            descriptions.add(cursor.getString(2));
            if(cursor.getString(2).equals("")) {
                System.out.println("no space");
            }
            if(cursor.getString(1).equals("Easter Sunday")) {
                System.out.println("ITS EASTER");
            }

            if (cursor.getString(5) != null) {
                locations.add(cursor.getString(5));
            }
            CNames[i] = cursor.getString(1);
            //Log.i(TAG, "CHECKING DUPLCIATE");
            /*
            parseService.checkDuplicates(cursor, new IParseCallback<List<ParseEventModel>>() {
                @Override
                public void onSuccess(List<ParseEventModel> items) {
                    Log.i(TAG, "NOT DUPLICATE");
                    //Not duplicate
                    ParseEventModel event = new ParseEventModel();
                    event.setUser();
                    Log.i("CALLBACK: ", nameOfEvent.get(nameOfEvent.size()-1));
                    event.setTitle(nameOfEvent.get(nameOfEvent.size()-1));
                    event.setDescription(cursor.getString(2));
                    event.setStartTime(getDateAndTime(Long.parseLong(cursor.getString(3))));
                    event.setStartDate(getDate(Long.parseLong(cursor.getString(3))));
                    if(cursor.getString(4) != null) {
                        event.setEndTime(getDateAndTime(Long.parseLong(cursor.getString(4))));
                    }
                    event.setLocation(cursor.getString(5));
                    String yes = "no";

                    if(Arrays.asList(Holidays).contains(cursor.getString(1))) {
                        event.setDayOff(true);
                        yes = "YES";
                    } else if(cursor.getString(1).contains("Birthday") || cursor.getString(1).contains("birthday")) {
                        event.setDayOff(false);
                        yes = "NO";
                    } else if(cursor.getString(1).contains("day") || cursor.getString(1).contains("Day")) {
                        event.setDayOff(true);
                        yes = "YES";
                    } else {
                        event.setDayOff(false);
                        yes = "NO";
                    }
                    event.setCalendarDisplay(yes+" : "+getDate(Long.parseLong(cursor.getString(3)))+" "+cursor.getString(1));


                    event.saveInBackground();
                }


                @Override
                public void onFail(String message) {
                    Log.i(TAG, "GOT A DUPLICATE");
                }
            });
            */
        }
        /*System.out.println("Number of events: "+cursor.getCount());
        System.out.println("NAME: "+nameOfEvent.toString());
        System.out.println("START: "+startDates.toString());
        System.out.println("DESC: "+descriptions.toString());
        System.out.println("LOCATION: "+locations.toString());*/

    }

    private void logout() {
        // Log the user out
        ParseUser.logOut();

        // Go to the login view
        startLoginActivity();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, ParseLoginDispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
