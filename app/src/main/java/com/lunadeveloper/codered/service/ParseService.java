package com.lunadeveloper.codered.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.lunadeveloper.codered.model.ResultModel;
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
 * Created by benjamin on 9/19/14.
 */
public class ParseService {
    private final String TAG = ParseService.class.getSimpleName();
    private Context context;
    public ParseService(Context context) {
        this.context = context;
    }
    public String CAN_I_GO_OUT_MESSAGE;
    public int CAN_I_GO_OUT;

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

    public void checkGoOut(ParseUser pu, String tomorrowsDate, final IParseResultCallback resultsCallback) {
        ParseQuery<ParseEventModel> tomorrowsEvents = ParseQuery.getQuery("ParseEventModel");
        tomorrowsEvents.whereEqualTo("start_date", tomorrowsDate);
        tomorrowsEvents.whereEqualTo("user", pu);
        tomorrowsEvents.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                if(e != null)
                    resultsCallback.onFail(e.getMessage());

                System.out.println(parseEventModels.size() + " events tomorrow");
                //no events tomorrow
                if(parseEventModels.size() == 0) {
                    CAN_I_GO_OUT = 0;
                    CAN_I_GO_OUT_MESSAGE = "NO EVENTS TOMORROW!";
                } else {
                    boolean event_too_early = false;
                    boolean tomorrow_day_off = false;
                    int earliest_event_tomorrow = 25;
                    for(ParseEventModel event : parseEventModels) {
                        //tomorrows a holiday
                        if(event.getDayOff()) {
                            tomorrow_day_off = true;
                            CAN_I_GO_OUT_MESSAGE = "Tomorrow is a holiday";
                            CAN_I_GO_OUT = 0;
                            break;
                        }
                        if(event.getTooEarly()) {
                            //got soemthign early tomorrow
                            CAN_I_GO_OUT = 1; //NO
                            CAN_I_GO_OUT_MESSAGE = "You got: "+ event.getTitle() + " at "+ event.getStartHour();
                            break;
                        }
                        //store the earliest event to show the user their earliest event tomorrow
                        if(earliest_event_tomorrow > event.getStartHour()) {
                            earliest_event_tomorrow = event.getStartHour();
                            CAN_I_GO_OUT_MESSAGE = "Earliest event tomorrow is: "+ event.getTitle() + " at "+ event.getStartHour()%12 + ((event.getStartHour() > 12) ? "PM" : "AM");
                            CAN_I_GO_OUT = 0;
                        }
                    }
                }
                ResultModel res = new ResultModel(CAN_I_GO_OUT, CAN_I_GO_OUT_MESSAGE);
                resultsCallback.onSuccess(res);
            }
        });
    }
}