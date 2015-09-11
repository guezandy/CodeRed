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
import android.widget.RelativeLayout;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class CalendarFragment extends Fragment {
    public String TAG = CalendarFragment.class.getSimpleName();
    private ParseService mParseService;
    public CalendarView calView;
    private RelativeLayout mView;
    public static int reasonsNotToGoOut = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_calendar, container, false);
        calView = (CalendarView) mView.findViewById(R.id.calendarView);

        mParseService = new ParseService(mView.getContext());
//test ten days
        new ParseService(mView.getContext()).getEvents(new IParseCallback<List<ParseEventModel>>() {
            @Override
            public void onSuccess(List<ParseEventModel> items) {
                for (ParseEventModel item : items) {
                    //System.out.println("PArse: "+item.getStartTime());
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
                    Date date = null;
                    try {
                        date = format.parse(item.getStartTime());
                        //System.out.println("DATE: "+date);
                    } catch (java.text.ParseException i) {
                        Log.e("format parse date", "GOT EXCEPTION");
                    }
                    //System.out.println("SEC: "+date.getTime()); // Sat Jan 02 00:00:00 GMT 2010
                    System.out.println("CalDATE: "+showDate(date.getTime()));
                    Date now = new Date();
                    if(item.getDayOff()) {
                        //set square to green
                        System.out.println(showDate(date.getTime()) + ": YES");
                    } else {
                        //set square to red
                        System.out.println(showDate(date.getTime()) + ": NO");
                    }
                }
            }
            @Override
            public void onFail(String message) {

            }
        });
        return mView;
    }

    public static String showTomorrowsDate(long milliSeconds) {
        milliSeconds = milliSeconds + 86400000;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String showDate(long milliSeconds) {
        //milliSeconds = milliSeconds + 86400000;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
