package com.lunadeveloper.codered.service;


import android.content.Context;
import android.database.DataSetObserver;
import android.widget.BaseAdapter;

import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public interface IParseService {

    //TODO update interface
    public void registerNewUser(final Context context, List<String> registerDetails);
    public void checkGoOut(ParseUser pu, String tomorrowsDate, final IParseResultCallback resultsCallback);

}
