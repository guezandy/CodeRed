package com.lunadeveloper.codered.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ParseEventModel")
public class ParseEventModel extends ParseObject {
    private final String TAG = ParseUserModel.class.getSimpleName();

    public ParseEventModel() {
        //required default constructer
    }

    //start
    public void setStartTime(String start) {
        this.put("start", start);
    }

    public String getStartTime() {
        return this.getString("start");
    }

    //description
    public void setDescription(String description) {
        this.put("description", description);
    }

    public String getDescription() {
        return this.getString("description");
    }
    //title
    public void setTitle(String title) {
        this.put("title", title);
    }

    public String getTitle() {
        return this.getString("title");
    }
    //end
    public void setEndTime(String end) {
        this.put("end", end);
    }

    public String getEndTime() {
        return this.getString("end");
    }
    //national holiday (off?)
    public void setDayOff(Boolean day) {
        this.put("dayoff", day);
    }

    public Boolean getDayOff() {
        return this.getBoolean("dayoff");
    }

    public void setTooEarly(Boolean early) {
        this.put("tooEarly", early);
    }

    public Boolean getTooEarly() {
        return this.getBoolean("tooEarly");
    }

    //parse user
    public void setUser() {
        this.put("user", ParseUser.getCurrentUser());
    }
    public ParseUser getUser() {
        return this.getParseUser("user");
    }

    public void setLocation(String loc) {
        this.put("location", loc);
    }

    public String getLocation() {
        return this.getString("location");
    }

    public void setTimeInMilli(long sec) {
        this.put("time", sec);
    }

    //start
    public void setStartDate(String start) {
        this.put("start_date", start);
    }

    public String getStartDate() {
        return this.getString("start_date");
    }

    public void setCalendarDisplay(String display) {
        this.put("display", display);
    }

    public String getCalendarDisplay() {
        return this.getString("display");
    }

    public void setTooEarly(Boolean b) {
        this.put("tooEarly", b);
    }

    public Boolean getTooEarly() {
        return this.getBoolean("tooEarly");
    }

    public void setStartHour(int h) {
        this.put("start_hour", h);
    }
    public int getStartHour() {
        return this.getInt("start_hour");
    }
}
