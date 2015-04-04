package com.lunadeveloper.codered;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Created by Owner on 9/16/2014.
 */


public class CodeRedApplication extends Application {
    private final static String TAG = CodeRedApplication.class.getSimpleName();

    // Debugging switch
    public static final boolean APPDEBUG = false;

    // Debugging tag for the application
    public static final String APPTAG = "CodeRed";

    public ParseUser user;
    private static final String KEY_PARSE_USER = "user";

    // Key for saving the search distance preference
    private static final String KEY_SEARCH_DISTANCE = "searchDistance";

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


        preferences = getSharedPreferences("com.lunadeveloper.codered", Context.MODE_PRIVATE);
        /*
            Initialize the ability to store data locally
         */
        Parse.enableLocalDatastore(this);


        /*
         * Parse credentials and initialize
         */
        Parse.initialize(this, "BrlF80YJ7tEhkrZ4pX2XjkRxGsuHEKU0yBppFrrO", "TRMjDXZaLJw5DaWM3Ma8rmZgRCONTxNzlke71Ylh");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
                //        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

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

        ParseACL.setDefaultACL(defaultACL, true);

    }

    public static void setUserId(String userId) {
        preferences.edit().putString(KEY_PARSE_USER, userId).commit();
    }

    public static ParseUser getUser() {
        final String userId = preferences.getString(KEY_PARSE_USER, "");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(userId, new GetCallback<ParseUser>(){
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user != null) {
                    Log.i(TAG, "We got the user in the application class");
                    Log.i(TAG, user.getString("first_name"));
                    //theUser = user;
                }
                else {
                    Log.i(TAG, "We didnt get the user in the class");
                }
            }
        });
        return ParseUser.getCurrentUser();

    }

    public static float getSearchDistance() {
        return preferences.getFloat(KEY_SEARCH_DISTANCE, 250);
    }

    public static void setSearchDistance(float value) {
        preferences.edit().putFloat(KEY_SEARCH_DISTANCE, value).commit();
    }

}