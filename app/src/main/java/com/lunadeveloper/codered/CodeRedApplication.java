package com.lunadeveloper.codered;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;


import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.lunadeveloper.codered.model.ScheduleModel;
import com.lunadeveloper.codered.model.UserEventModel;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.lunadeveloper.codered.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Owner on 9/16/2014.
 */


public class CodeRedApplication extends Application {
    private final static String TAG = CodeRedApplication.class.getSimpleName();

    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "CodeRed";

    // Key for saving the search distance preference
    private static final String KEY_GO_OUT_DATE = "searchDate";

    private static SharedPreferences preferences;




    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");

        /*
         * Registering Parse Subclasses
         *
         */
        ParseObject.registerSubclass(ParseUserModel.class);
        ParseObject.registerSubclass(ParseEventModel.class);
        ParseObject.registerSubclass(ScheduleModel.class);
        //ParseObject.registerSubclass(UserEventModel.class);



        preferences = getSharedPreferences("com.lunadeveloper.codered", Context.MODE_PRIVATE);
        /*
            Initialize the ability to store data locally
         */
        Parse.enableLocalDatastore(this);


        /*
         * Parse credentials and initialize
         */
        Parse.initialize(this, "BrlF80YJ7tEhkrZ4pX2XjkRxGsuHEKU0yBppFrrO", "TRMjDXZaLJw5DaWM3Ma8rmZgRCONTxNzlke71Ylh");
        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        /*
         * This app lets an anonymous user create and save photos of meals
         * they've eaten. An anonymous user is a user that can be created
         * without a username and password but still has all of the same
         * capabilities as any other ParseUser.
         *
         * After logging out, an anonymous user is abandoned, and its data is no
         * longer accessible. In your own app, you can convert anonymous users
         * to regular users so that data persists.
         *
         * Learn more about the ParseUser class:
         * https://www.parse.com/docs/android_guide#users
         */
        //ParseUser.enableAutomaticUser();

        /*
         * For more information on app security and Parse ACL:
         * https://www.parse.com/docs/android_guide#security-recommendations
         */
        ParseACL defaultACL = new ParseACL();

        /*
         * If you would like all objects to be private by default, remove this
         * line
         */
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


    }


    public static void setGoOutDate(String s) {
        preferences.edit().putString(KEY_GO_OUT_DATE, s).commit();
    }

    public static String getGoOutDate() {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar c = Calendar.getInstance();
        return preferences.getString(KEY_GO_OUT_DATE, dateFormat.format(c.getTime()));
    }


}