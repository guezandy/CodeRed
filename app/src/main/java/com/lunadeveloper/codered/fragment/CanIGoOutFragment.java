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
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    public static int CAN_I_GO_OUT;
    public static String CAN_I_GO_OUT_MESSAGE;
    private TextView message;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_cani, container, false);
        mParseService = new ParseService(mView.getContext());

        message = (TextView) mView.findViewById(R.id.message);
        currentDateText = (TextView) mView.findViewById(R.id.currentDate);

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, count);

        currentDateText.setText(dateFormat.format(c.getTime()));
        final Application a = getActivity().getApplication();
        ((CodeRedApplication) a).setGoOutDate(dateFormat.format(c.getTime()));

        next = (ImageView) mView.findViewById(R.id.next);
        prev = (ImageView) mView.findViewById(R.id.prev);
        button = (ImageView) mView.findViewById(R.id.imageView);

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
                final Calendar c2 = Calendar.getInstance();
                c2.add(Calendar.DATE, count);

                currentDateText.setText(dateFormat.format(c2.getTime()));
                ((CodeRedApplication) a).setGoOutDate(dateFormat.format(c2.getTime()));


                button.setBackgroundResource(R.drawable.canigoout_button);
                button.setBackgroundResource(R.drawable.canigoout_button);
                button.setBackgroundResource(R.drawable.canigoout_button);
                button.setBackgroundResource(R.drawable.canigoout_button);
                button.setBackgroundResource(R.drawable.canigoout_button);
                button.setBackgroundResource(R.drawable.canigoout_button);

                //get the date to check
                final Calendar checkDate = Calendar.getInstance();
                //we're always checking if theres something tomorrow
                checkDate.add(Calendar.DATE, count+1);
                String tomorrowsDate = dateFormat.format(checkDate.getTime());
                System.out.println("CHECK DATE IS: "+ tomorrowsDate);

                ParseQuery<ParseEventModel> tomorrowsEvents = ParseQuery.getQuery("ParseEventModel");
                tomorrowsEvents.whereEqualTo("start_date", tomorrowsDate);
                tomorrowsEvents.whereEqualTo("user", ParseUser.getCurrentUser());
                tomorrowsEvents.findInBackground(new FindCallback<ParseEventModel>() {
                    @Override
                    public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                        System.out.println(parseEventModels.size() + " events tomorrow");
                        //no events tomorrow
                        if(parseEventModels.size() == 0) {
                            CAN_I_GO_OUT = 0;
                            CAN_I_GO_OUT_MESSAGE = "NO EVENTS TOMORROW!";
                        } else {
                            boolean event_too_early = false;
                            boolean tomorrow_day_off = false;
                            int earliest_event_tomorrow = 25;
                            for(ParseEventModel event : parseEventModels) {
                                //tomorrows a holiday
                                if(event.getDayOff()) {
                                    tomorrow_day_off = true;
                                    CAN_I_GO_OUT_MESSAGE = "Tomorrow is a holiday";
                                    CAN_I_GO_OUT = 0;
                                    break;
                                }
                                if(event.getTooEarly()) {
                                    //got soemthign early tomorrow
                                    CAN_I_GO_OUT = 1; //NO
                                    CAN_I_GO_OUT_MESSAGE = "You got: "+ event.getTitle() + " at "+ event.getStartHour() + ((event.getStartHour() > 12) ? "PM" : "AM");
                                    break;
                                }
                                //store the earliest event to show the user their earliest event tomorrow
                                if(earliest_event_tomorrow > event.getStartHour()) {
                                    earliest_event_tomorrow = event.getStartHour();
                                    CAN_I_GO_OUT_MESSAGE = "Earliest event tomorrow is: "+ event.getTitle() + " at "+ event.getStartHour()%12 + ((event.getStartHour() > 12) ? "PM" : "AM");
                                    CAN_I_GO_OUT = 0;
                                }
                            }
                        }
                    }
                });
                System.out.println("CAN I GO OUT: "+ CAN_I_GO_OUT+ " "+CAN_I_GO_OUT_MESSAGE);
                Activity main = getActivity();
                System.out.println("RESTTING DRAWER");
                ((MainActivity) main).replaceDrawerFragment(new ListCalendarFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));
            }

        });

        if(count == 0) {
            prev.setVisibility(View.GONE);
            button.setBackgroundResource(R.drawable.canigoout_button);
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
                final Calendar c3 = Calendar.getInstance();
                c3.add(Calendar.DATE, count);
                currentDateText.setText(dateFormat.format(c3.getTime()));
                ((CodeRedApplication) a).setGoOutDate(dateFormat.format(c3.getTime()));

                System.out.println("CALLING THIS:!!!!");
                button.setBackgroundResource(R.drawable.canigoout_button);
                System.out.println("CALLED IT");

                //get the date to check
                final Calendar checkDate = Calendar.getInstance();
                //we're always checking if theres something tomorrow
                checkDate.add(Calendar.DATE, count+1);
                String tomorrowsDate = dateFormat.format(checkDate.getTime());
                System.out.println("CHECK DATE IS: "+ tomorrowsDate);

                ParseQuery<ParseEventModel> tomorrowsEvents = ParseQuery.getQuery("ParseEventModel");
                tomorrowsEvents.whereEqualTo("start_date", tomorrowsDate);
                tomorrowsEvents.whereEqualTo("user", ParseUser.getCurrentUser());
                tomorrowsEvents.findInBackground(new FindCallback<ParseEventModel>() {
                    @Override
                    public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                        System.out.println(parseEventModels.size() + " events tomorrow");
                        //no events tomorrow
                        if (parseEventModels.size() == 0) {
                            CAN_I_GO_OUT = 0;
                            CAN_I_GO_OUT_MESSAGE = "NO EVENTS TOMORROW!";
                        } else {
                            boolean event_too_early = false;
                            boolean tomorrow_day_off = false;
                            int earliest_event_tomorrow = 25;
                            for (ParseEventModel event : parseEventModels) {
                                //tomorrows a holiday
                                if (event.getDayOff()) {
                                    tomorrow_day_off = true;
                                    CAN_I_GO_OUT_MESSAGE = "Tomorrow is a holiday";
                                    CAN_I_GO_OUT = 0;
                                    break;
                                }
                                if (event.getTooEarly()) {
                                    //got soemthign early tomorrow
                                    CAN_I_GO_OUT = 1; //NO
                                    CAN_I_GO_OUT_MESSAGE = "You got: " + event.getTitle() + " at " + event.getStartHour() + ((event.getStartHour() > 12) ? "PM" : "AM");
                                    break;
                                }
                                //store the earliest event to show the user their earliest event tomorrow
                                if (earliest_event_tomorrow > event.getStartHour()) {
                                    earliest_event_tomorrow = event.getStartHour();
                                    CAN_I_GO_OUT_MESSAGE = "Earliest event tomorrow is: " + event.getTitle() + " at " + event.getStartHour() % 12 + ((event.getStartHour() > 12) ? "PM" : "AM");
                                    CAN_I_GO_OUT = 0;
                                }
                            }
                        }
                    }
                });
                System.out.println("CAN I GO OUT: "+ CAN_I_GO_OUT+ " "+CAN_I_GO_OUT_MESSAGE);
                Activity main = getActivity();
                System.out.println("RESTTING DRAWER");
                ((MainActivity) main).replaceDrawerFragment(new ListCalendarFragment(), true, FragmentTransaction.TRANSIT_FRAGMENT_FADE, getString(R.string.title_section3));

            }
        });

        //final int CAN_I_GO_OUT;  //0 yes, 1 no, 2 it's risky
        //final String CAN_I_GO_OUT_MESSAGE;

        Context con = mView.getContext();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the date to check
                final Calendar checkDate = Calendar.getInstance();
                //we're always checking if theres something tomorrow
                checkDate.add(Calendar.DATE, count+1);
                String tomorrowsDate = dateFormat.format(checkDate.getTime());
                System.out.println("CHECK DATE IS: "+ tomorrowsDate);

                ParseQuery<ParseEventModel> tomorrowsEvents = ParseQuery.getQuery("ParseEventModel");
                tomorrowsEvents.whereEqualTo("start_date", tomorrowsDate);
                tomorrowsEvents.whereEqualTo("user", ParseUser.getCurrentUser());
                tomorrowsEvents.findInBackground(new FindCallback<ParseEventModel>() {
                    @Override
                    public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                       System.out.println(parseEventModels.size() + " events tomorrow");
                        //no events tomorrow
                       if(parseEventModels.size() == 0) {
                           CAN_I_GO_OUT = 0;
                           CAN_I_GO_OUT_MESSAGE = "NO EVENTS TOMORROW!";
                       } else {
                           boolean event_too_early = false;
                           boolean tomorrow_day_off = false;
                           int earliest_event_tomorrow = 25;
                           for(ParseEventModel event : parseEventModels) {
                               //tomorrows a holiday
                               if(event.getDayOff()) {
                                   tomorrow_day_off = true;
                                   CAN_I_GO_OUT_MESSAGE = "Tomorrow is a holiday";
                                   CAN_I_GO_OUT = 0;
                                   break;
                               }
                               if(event.getTooEarly()) {
                                   //got soemthign early tomorrow
                                   CAN_I_GO_OUT = 1; //NO
                                   CAN_I_GO_OUT_MESSAGE = "You got: "+ event.getTitle() + " at "+ event.getStartHour();
                                   break;
                               }
                               //store the earliest event to show the user their earliest event tomorrow
                               if(earliest_event_tomorrow > event.getStartHour()) {
                                   earliest_event_tomorrow = event.getStartHour();
                                   CAN_I_GO_OUT_MESSAGE = "Earliest event tomorrow is: "+ event.getTitle() + " at "+ event.getStartHour()%12 + ((event.getStartHour() > 12) ? "PM" : "AM");
                                   CAN_I_GO_OUT = 0;
                               }
                           }
                       }
                    }
                });
                System.out.println("CAN I GO OUT: "+ CAN_I_GO_OUT+ " "+CAN_I_GO_OUT_MESSAGE);
                if(CAN_I_GO_OUT == 0) {
                    button.setImageResource(R.drawable.hellyeah_response);
                    message.setText(CAN_I_GO_OUT_MESSAGE);
                } else if(CAN_I_GO_OUT == 1) {
                    button.setImageResource(R.drawable.fuckno_response);
                    message.setText(CAN_I_GO_OUT_MESSAGE);
                } else {
                   button.setImageResource(R.drawable.hellyeah_response);
                }
            }
        });


        /*
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
                            //System.out.println("PArse: "+item.getStartTime());
                            DateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH);
                            Date date = null;
                            try {
                                date = format.parse(item.getStartTime());
                                //System.out.println("DATE: "+date);
                            } catch (java.text.ParseException i) {
                                Log.e("GET TOMORROW", "GOT EXCEPTION");
                            }
                            //System.out.println("SEC: "+date.getTime()); // Sat Jan 02 00:00:00 GMT 2010
                            //System.out.println("DATEcomp: "+showDate(date.getTime()));
                            Date now = new Date();
                            //System.out.println("Tomorrow: "+showCurrentTomorrowDate(now.getTime(), count));
                            if(showDate(date.getTime()).equals(showCurrentTomorrowDate(now.getTime(), count))) {
                                // System.out.println("THEY ARE EQUAL");
                                if(!item.getDayOff()) {
                                    System.out.println("dont go out: "+item.getTitle()+" count:"+reasonsNotToGoOut);
                                    reasonsNotToGoOut = 1;
                                    button.setImageResource(R.drawable.fuckno_response);
                                    currentDateText.setText(currentDateText.getText()+"\n"+item.getTitle());
                                    goOut = false;
                                    none = false;
                                    break;
                                }
                                else if(item.getDayOff()) {
                                    System.out.println("going out: "+item.getTitle()+" count: "+reasonsNotToGoOut);
                                    reasonsNotToGoOut = -1;
                                    button.setImageResource(R.drawable.hellyeah_response);
                                    currentDateText.setText(currentDateText.getText()+"\n"+item.getTitle());
                                    goOut = true;
                                    none = false;
                                    break;
                                }
                            }
                        }
                        if(none) {
                            button.setImageResource(R.drawable.hellyeah_response);
                        }
                        currentDateText.setText(currentDateText.getText()+"\n"+"Nothing to do");

                    }

                    @Override
                    public void onFail(String message) {

                    }
                });
                System.out.println("COUNT: "+reasonsNotToGoOut+ "GO OUT: "+goOut);
                if(reasonsNotToGoOut == 1) {
                    //button.setBackgroundResource(R.color.grey_dark);
                    button.setImageResource(R.drawable.fuckno_response);
                    //reasonsNotToGoOut = 0;
                    goOut = true;
                } else if(reasonsNotToGoOut == -1){
                    //can go out
                    //button.setBackgroundResource(R.color.blue);
                    button.setImageResource(R.drawable.hellyeah_response);
                    //reasonsNotToGoOut = 0;
                } else {
                    //button.setBackgroundResource(R.color.blue);
                    button.setImageResource(R.drawable.hellyeah_response);
                    //reasonsNotToGoOut = 0;
                }
                if(goOut) {
                    //button.setBackgroundResource(R.color.blue);
                    button.setImageResource(R.drawable.hellyeah_response);
                    reasonsNotToGoOut = 0;
                } else {
                    //button.setBackgroundResource(R.color.grey_dark);
                    button.setImageResource(R.drawable.fuckno_response);
                    reasonsNotToGoOut = 0;
                    goOut = true;
                }
            }
        });
        */
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

}
