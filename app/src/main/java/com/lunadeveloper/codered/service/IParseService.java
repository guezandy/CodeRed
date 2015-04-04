package com.lunadeveloper.codered.service;


import android.content.Context;
import android.database.DataSetObserver;
import android.widget.BaseAdapter;

import com.parse.ParseQueryAdapter;

import java.util.List;


public interface IParseService {

    public void registerNewUser(final Context context, List<String> registerDetails);

}
