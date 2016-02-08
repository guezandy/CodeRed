package com.lunadeveloper.codered.model;

import com.parse.ParseUser;

import java.util.Date;

/*
 * An extension of ParseUser that makes
 * it more convenient to access information
 * about a given user
 */

public class ParseUserModel extends ParseUser {
    private final String TAG = ParseUserModel.class.getSimpleName();

    public ParseUserModel() {
        //required default constructer
    }

    public void setFullName(String fname) {
        this.put("full_name", fname);
    }

    public String getFullName() {
        return this.getString("full_name");
    }

<<<<<<< HEAD
    public void setNextCalendarSync(Date date) {
        this.put("next_sync", date);
    }

    public Date getNextCalendarSync() {
        return this.getDate("next_sync");
    }

    public void setEarly(String i) {
        this.put("early", i);
    }

    public int getEarly() {
        return Integer.parseInt(this.getString("early"));
=======
    public void setEarly(String val) {
        this.put("early", val);
>>>>>>> origin/master
    }

    public String getEarly() {
        return this.getString("early");
    }
}
