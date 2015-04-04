package com.lunadeveloper.codered.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.text.NumberFormat;
import java.util.List;


/**
 * Service class to handle interactions with Parse.
 * <p/>
 * Note: This class does not spawn new Threads.
 * <p/>
 * Created by benjamin on 9/19/14.
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

    /*public void getVideoPerUser(final Context context, final IParseCallback<List<ParseVideoModel>> itemsCallback) {

        ParseQuery<ParseVideoModel> query = ParseQuery.getQuery("ParseVideoModel");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        //query.whereEqualTo("store", store);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseVideoModel>() {
            @Override
            public void done(List<ParseVideoModel> results, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    //add results to the callback
                    itemsCallback.onSuccess(results);
                }
            }
        });

    }

    public void getVideoPerAudience(final Context context, final String audienceId, final IParseCallback<List<ParseVideoModel>> itemsCallback) {

        ParseQuery<ParseVideoModel> query = ParseQuery.getQuery("ParseVideoModel");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        //query.whereEqualTo("store", store);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseVideoModel>() {
            @Override
            public void done(List<ParseVideoModel> results, ParseException e) {
                if (e != null) {
                    // There was an error
                } else {
                    //add results to the callback
                    itemsCallback.onSuccess(results);
                }
            }
        });

    }*/
}