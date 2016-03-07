package com.lunadeveloper.codered.fragment;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lunadeveloper.codered.CodeRedApplication;
import com.lunadeveloper.codered.MainActivity;
import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.model.ResultModel;
import com.lunadeveloper.codered.service.IParseResultCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;


public class CanIGoOutFragment extends Fragment {
    public String TAG = CanIGoOutFragment.class.getSimpleName();
    private ParseService mParseService;
    private RelativeLayout mView;
    private ImageView button;
    private int count = 0;
    public boolean none = true;
    private ImageView next;
    private ImageView prev;
    private TextView currentDateText;
    private TextView message;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_cani, container, false);
        mParseService = new ParseService(mView.getContext());
        final CodeRedApplication app = (CodeRedApplication) getActivity().getApplication();
        message = (TextView) mView.findViewById(R.id.message);
        currentDateText = (TextView) mView.findViewById(R.id.currentDate);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        currentDateText.setText(app.getGoOutDate());

        next = (ImageView) mView.findViewById(R.id.next);
        prev = (ImageView) mView.findViewById(R.id.prev);
        button = (ImageView) mView.findViewById(R.id.imageView);

        count = getArguments().getInt("key");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLicked next");
                button.setBackgroundResource(R.drawable.canigoout_button);
                count++;
                if(count == 0) {
                    prev.setVisibility(View.GONE);
                } else {
                    prev.setVisibility(View.VISIBLE);
                }
                Date d;
                try {
                    d = dateFormat.parse(app.getGoOutDate());
                    final Calendar c2 = Calendar.getInstance();
                    c2.setTime(d);
                    c2.add(Calendar.DATE, 1); //add one day
                    app.setGoOutDate(dateFormat.format(c2.getTime()));
                } catch(java.text.ParseException e) {
                    System.out.println("PARSE EXCEPTION");
                }

                Bundle args = new Bundle();
                args.putInt("key", count);
                CanIGoOutFragment newScreen = new CanIGoOutFragment();
                newScreen.setArguments(args);

                Activity main = getActivity();
                System.out.println("RESTTING DRAWER");
                ((MainActivity) main).replaceDrawerFragment(new ListCalendarFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                ((MainActivity) main).replaceFragment(newScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
            }
        });

        if(count == 0) {
            prev.setVisibility(View.GONE);
            button.setBackgroundResource(R.drawable.canigoout_button);
        } else if(count > 30) {
            next.setVisibility(View.GONE);
        } else {
            prev.setVisibility(View.VISIBLE);
            button.setBackgroundResource(R.drawable.canigoout_button);
        }
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count--;
                button.setBackgroundResource(R.drawable.canigoout_button);
                if(count == 0) {
                    prev.setVisibility(View.GONE);
                } else {
                    prev.setVisibility(View.VISIBLE);
                }
                Date d;
                try {
                    d = dateFormat.parse(app.getGoOutDate());
                    final Calendar c2 = Calendar.getInstance();
                    c2.setTime(d);
                    c2.add(Calendar.DATE, -1); //minus one day
                    app.setGoOutDate(dateFormat.format(c2.getTime()));
                } catch(java.text.ParseException e) {
                    System.out.println("PARSE EXCEPTION");
                }

                Bundle args = new Bundle();
                args.putInt("key", count);
                CanIGoOutFragment newScreen = new CanIGoOutFragment();
                newScreen.setArguments(args);

                Activity main = getActivity();
                System.out.println("RESTTING DRAWER");
                ((MainActivity) main).replaceDrawerFragment(new ListCalendarFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
                ((MainActivity) main).replaceFragment(newScreen, true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
            }
        });

        //int CAN_I_GO_OUT;  //0 yes, 1 no, 2 it's risky

        Context con = mView.getContext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initialize calendar
                final Calendar checkDate = Calendar.getInstance();
                //setup date formatter to convert string to date
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                Date d;
                try {
                    //get the date from Application and format it into a date
                    d = dateFormat.parse(app.getGoOutDate());
                    //set the calendar time to the date in the app
                    checkDate.setTime(d);
                    //add one date
                    checkDate.add(Calendar.DATE, 1);
                    //format it to tomorrows date as a string
                    String tomorrowsDate = dateFormat.format(checkDate.getTime());
                    System.out.println("CHECK DATE IS: "+ tomorrowsDate);
                    //pass it to parse service
                    mParseService.checkGoOut(ParseUser.getCurrentUser(), tomorrowsDate, new IParseResultCallback() {
                        @Override
                        public void onSuccess(ResultModel res) {
                            System.out.println("CAN I GO OUT: "+res.getMessage()+ " "+res.getStatus());
                            if(res.getStatus() == 0) {
                                button.setImageResource(R.drawable.hellyeah_response);
                                message.setText(res.getMessage());
                            } else if(res.getStatus() == 1) {
                                button.setImageResource(R.drawable.fuckno_response);
                                message.setText(res.getMessage());
                            } else {
                                button.setImageResource(R.drawable.hellyeah_response);
                            }
                        }

                        @Override
                        public void onFail(String message) {
                            Log.e(TAG, message);
                        }
                    });
                } catch(java.text.ParseException e) {
                    System.out.println("DATE PARSEING ERROR");
                }
            }
        });
        return mView;
    }
}
