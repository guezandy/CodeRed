package com.lunadeveloper.codered.fragment;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.adapter.CustomAdapter;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class ListCalendarFragment extends Fragment {
    public String TAG = CalendarFragment.class.getSimpleName();
    private ParseService mParseService;
    public CalendarView calView;
    private RelativeLayout mView;
    public static int reasonsNotToGoOut = 0;
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private CustomAdapter urgentTodosAdapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);

// Initialize main ParseQueryAdapter
        mainAdapter = new ParseQueryAdapter<ParseObject>(mView.getContext(), "ParseEventModel");
        mainAdapter.setTextKey("display");

        //mainAdapter.setTextKey("start_date");

        // Initialize the subclass of ParseQueryAdapter
        urgentTodosAdapter = new CustomAdapter(this.getActivity());

        // Initialize ListView and set initial view to mainAdapter
        listView = (ListView) mView.findViewById(R.id.list);
        listView.setAdapter(mainAdapter);
        mainAdapter.loadObjects();


        return mView;
    }
}
