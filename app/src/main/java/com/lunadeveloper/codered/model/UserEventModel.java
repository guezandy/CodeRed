package com.lunadeveloper.codered.model;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/*
 * An extension of ParseUser that makes
 * it more convenient to access information
 * about a given user
 */

public class UserEventModel extends ParseObject {
    private final String TAG = UserEventModel.class.getSimpleName();

    public UserEventModel() {
        //required default constructer
    }

    public UserEventModel(String t, ParseUser o, List<ParseUser> invited) {
        this.put("title", t);
        this.put("owner", o);
        this.addAllUnique("invited", invited);
    }

    public String getTitle() {
        return this.getString("title");
    }

    public void setTitle(String s) {
        this.put("title", s);
    }

    public ParseUser getOwner() {
        return this.getParseUser("owner");
    }

    public void setParseUser(ParseUser u) {
        this.put("owner", u);
    }

    public List<ParseUser> getInvited() {
        return this.getList("invited");
    }

    public void setInvited(List<ParseUser> invited) {
        this.addAllUnique("invited", invited);
    }

}
