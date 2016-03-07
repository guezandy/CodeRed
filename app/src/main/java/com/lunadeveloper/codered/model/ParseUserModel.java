package com.lunadeveloper.codered.model;

import com.parse.ParseFile;
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

    }

    public ParseUserModel(String v) {
        //TODO fix legacy go out algorithm dependency on tooearly variable
        setEarly("8");

        setCalendarSync(false);
        setCalendarSync(false);
    }

    public void setFullName(String fname) {
        this.put("full_name", fname);
    }

    public String getFullName() {
        return this.getString("full_name");
    }

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
    }

    public ParseFile getImage() {
        return this.getParseFile("image");
    }

    public void setImage(ParseFile f) {
        this.put("image", f);
    }

    public String getPhone() {
        return this.getString("phone");
    }

    public void setPhone(String p) {
        this.put("phone", p);
    }

    public Boolean getCalendarSync() {
        return this.getBoolean("calendar_sync");
    }

    public void setCalendarSync(Boolean sync) {
        this.put("calendar_sync", sync);
    }

    public Boolean getContactSync() {
        return this.getBoolean("contact_sync");
    }

    public void setContactSync(Boolean sync) {
        this.put("contact_sync", sync);
    }

}
