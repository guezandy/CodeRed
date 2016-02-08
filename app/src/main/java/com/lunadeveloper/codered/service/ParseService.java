package com.lunadeveloper.codered.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Service class to handle interactions with Parse.
 * <p/>
 * Note: This class does not spawn new Threads.
 * <p/>
 */
public class ParseService {
    private final String TAG = ParseService.class.getSimpleName();


    static int counter = 1;
    private Context context;

    NumberFormat nf = NumberFormat.getCurrencyInstance();

    public boolean APPDEBUG = false;

    public ParseService(Context context) {
        this.context = context;
    }


    public void registerNewUser(final Context context, List<String> registerDetails) {
        final ParseUserModel user = new ParseUserModel();
        // username is set to email
        user.setUsername(registerDetails.get(0));
        user.setPassword(registerDetails.get(1));
        user.setFullName(registerDetails.get(2));
        user.setEarly(registerDetails.get(3));

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(
                            context,
                            "Registration Successful\n",
                            Toast.LENGTH_SHORT).show();
                    // Hooray! Let them use the app now.
                    Intent i = new Intent(context, ParseLoginDispatchActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.getApplicationContext().startActivity(i);
                } else {
                    Toast.makeText(context, "Registration Failed: "+e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    Log.e(TAG, "Login failed: "+e.getMessage());
                }
            }
        });
    }


    public void getEvents(final IParseCallback<List<ParseEventModel>> eventsCallback) {


        //get tomorrows date as string to compare to and get in query


        Date now = new Date();
        System.out.println("PS: Tomorrow: " + showTomorrowsDate(now.getTime()));
        ParseQuery<ParseEventModel> query = ParseQuery.getQuery("ParseEventModel");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("start_date", showTomorrowsDate(now.getTime()));
        query.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> results, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    //add results to the callback
                    eventsCallback.onSuccess(results);
                }
            }
        });
    }

    public void checkDuplicates(Cursor cursor, final IParseCallback<List<ParseEventModel>> eventsCallback) {
        Log.i(TAG, "CHECKING DUPLICATES");
        ParseQuery<ParseEventModel> query = ParseQuery.getQuery("ParseEventModel");

        query.whereEqualTo("title", cursor.getString(1));
        //query.whereEqualTo("start_time", getDateAndTime(Long.parseLong(cursor.getString(3))));
        if(cursor.getString(4) != null) {
            query.whereEqualTo("end", getDateAndTime(Long.parseLong(cursor.getString(4))));
        }
        query.getFirstInBackground(new GetCallback<ParseEventModel>() {
            @Override
            public void done(ParseEventModel event, ParseException e) {
                if (event != null) {
                    Log.i(TAG, "DUPLICATES");
                    eventsCallback.onFail("Duplicate");
                } else {
                    Log.i(TAG, "NOT DUPLCIATE");
                    eventsCallback.onSuccess(new ArrayList<ParseEventModel>());
                }
            }
        });
    }

    public void checkDate(final String string, final IParseCallback<List<ParseEventModel>> eventsCallback) {

        //String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(string);
        } catch (java.text.ParseException i) {
            Log.e("GET TOMORROW", "GOT EXCEPTION");
        }
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010


        ParseQuery<ParseEventModel> query = ParseQuery.getQuery("ParseEventModel");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereEqualTo("start_date", string);
        query.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> results, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    //add results to the callback
                    eventsCallback.onSuccess(results);
                }
            }
        });
    }

    public static String getDateAndTime(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String showTomorrowsDate(long milliSeconds) {
        milliSeconds = milliSeconds + 86400000;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}