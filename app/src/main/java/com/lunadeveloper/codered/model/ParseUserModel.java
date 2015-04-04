package com.lunadeveloper.codered.model;

import com.parse.ParseUser;

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


}
