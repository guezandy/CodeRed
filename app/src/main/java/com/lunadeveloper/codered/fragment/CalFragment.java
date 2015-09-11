package com.lunadeveloper.codered.fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.lunadeveloper.codered.adapter.CaldroidSampleCustomAdapter;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import hirondelle.date4j.DateTime;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class CalFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
 /*       final HashMap<String, Boolean> extra = new HashMap<String, Boolean>();
        new ParseService(this.getActivity().getBaseContext()).checkDate(string, new IParseCallback<List<ParseEventModel>>() {
            @Override
            public void onSuccess(List<ParseEventModel> items) {
                for (final ParseEventModel item : items) {
                    extra.put(item.getStartDate(), item.getDayOff());
                    //System.out.print("DT: "+dateTime.toString()+" ");
                    System.out.print("EVENT: "+item.getTitle()+" ");
                    if(item.getDayOff()) {
                        System.out.println("GREEN");
                    } else {
                        System.out.println("RED");
                    }
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
*/
        return new CaldroidSampleCustomAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
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
