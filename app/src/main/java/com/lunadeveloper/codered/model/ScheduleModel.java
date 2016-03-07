package com.lunadeveloper.codered.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/*
 * An extension of ParseUser that makes
 * it more convenient to access information
 * about a given user
 */
@ParseClassName("ScheduleModel")
public class ScheduleModel extends ParseObject {
    private final String TAG = UserEventModel.class.getSimpleName();

    public ScheduleModel() {
        //required default constructer
    }

    public ScheduleModel(String name, List<String> days) {
        this.setWeekDays(days);
        this.setName(name);
    }

    public ParseUser getUser() {
        return this.getParseUser("user");
    }

    public void setUser(ParseUser u) {
        this.put("user", u);
    }

    public List<String> getWeekDays() {
        return this.getList("week_days");
    }

    public void setWeekDays(List<String> d) {
        this.put("week_days", d);
    }

    public void setName(String name) {
        this.put("name", name);
    }

    public String getName() {
        return this.getString("name");
    }

}
