package com.lunadeveloper.codered.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CanIGoOutFragment extends Fragment {
    public String TAG = CanIGoOutFragment.class.getSimpleName();
    private ParseService mParseService;


    private RelativeLayout mView;
    private ImageView button;
    private String currentDate;
    private int count = 0;
    public  int reasonsNotToGoOut = 0;
    public boolean goOut = false;
    public boolean none = true;
    private ImageView next;
    private ImageView prev;
    private TextView currentDateText;
    private TextView result;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_cani, container, false);
        mParseService = new ParseService(mView.getContext());

        result = (TextView) mView.findViewById(R.id.result);
        currentDateText = (TextView) mView.findViewById(R.id.currentDate);
        final Date d = new Date();

        currentDateText.setText(showCurrentDate(d.getTime(), count).toString());
        next = (ImageView) mView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLicked next");
                count++;
                button.setBackgroundResource(R.drawable.canigoout_button);
                currentDateText.setText(showCurrentDate(d.getTime(), count).toString());
                reasonsNotToGoOut = 1;
                System.out.println("CUREENT DATE: "+showNOWDate());
                System.out.println("TOMORROW DATE: "+showNOWTomorrowDate());
            }

        });

        prev = (ImageView) mView.findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                button.setBackgroundResource(R.drawable.canigoout_button);
                currentDateText.setText(showCurrentDate(d.getTime(), count).toString());
                reasonsNotToGoOut = 1;

            }

        });


        Context con = mView.getContext();
        button = (ImageView) mView.findViewById(R.id.imageView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //count++;
                Date date = new Date();
                date.getTime();
                System.out.println(showCurrentDate(date.getTime(), count));
                new ParseService(mView.getContext()).getEvents(new IParseCallback<List<ParseEventModel>>() {
                    @Override
                    public void onSuccess(List<ParseEventModel> items) {
                        for (ParseEventModel item : items) {
                            System.out.println("Item: " + item.getStartDate() + " H: " + item.getDayOff() + " E: " + item.getTooEarly() + " D: " + item.getTitle());
                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
                            Date date = null;
                            try {
                                date = format.parse(item.getStartTime());
                                //System.out.println("DATE: "+date);
                            } catch (java.text.ParseException i) {
                                Log.e("GET TOMORROW", "GOT EXCEPTION");
                            }
                            //System.out.println("SEC: "+date.getTime()); // Sat Jan 02 00:00:00 GMT 2010
                            System.out.println("DATEcomp: "+showDate(date.getTime()));
                            Date now = new Date();
                            System.out.println("Tomorrow: "+showCurrentTomorrowDate(now.getTime(), count));
                            if (showDate(date.getTime()).equals(showCurrentTomorrowDate(now.getTime(), count))) {

                                System.out.println("THIS IS TOMORROW");

                                if(!item.getDayOff()) {
                                    //TOMORROW's event IS NOT A HOLIDAY

                                    //IS THE EVENT early
                                } else {
                                    //TOMORROW's event IS A HOLIDAY YAY
                                }
                            }
                        }
                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
            }
        });
        return mView;
    }



    public static String showCurrentDate(long milliSeconds, int Count) {
        long adjust = 86400000*(Count+1);
        milliSeconds = milliSeconds + adjust;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String showCurrentTomorrowDate(long milliSeconds, int Count) {
        long adjust = 86400000*(Count+2);
        milliSeconds = milliSeconds + adjust;
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

    public static String showTomorrowsDate(long milliSeconds) {
        milliSeconds = milliSeconds + 86400000;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static String showNOWDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        Date d = calendar.getTime();
        System.out.println("Time zone: " + calendar.getTimeZone());
        //calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String showNOWTomorrowDate() {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        System.out.println("Time zone: " + calendar.getTimeZone());
        //calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
