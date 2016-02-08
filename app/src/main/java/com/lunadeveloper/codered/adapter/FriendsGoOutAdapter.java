package com.lunadeveloper.codered.adapter;


import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lunadeveloper.codered.CodeRedApplication;
import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;


public class FriendsGoOutAdapter extends ArrayAdapter<ParseUser> {

    public ParseUser requester;
    public Date date;
    public static int CAN_I_GO_OUT;
    public static String CAN_I_GO_OUT_MESSAGE;

    public FriendsGoOutAdapter(Context context, List<ParseUser> requests) {
        super(context, 0, requests);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CAN_I_GO_OUT = -1;

        // Get the data item for this position
        final ParseUser user = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_friend_go_out, parent, false);
        }
        // Lookup view for data population
        final TextView tvName = (TextView) convertView.findViewById(R.id.name);
        final Button canI = (Button) convertView.findViewById(R.id.status);

        tvName.setText(user.getString("full_name"));
        String dateString = CodeRedApplication.getGoOutDate();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        try {
             date = dateFormat.parse(dateString);
        } catch(java.text.ParseException e) {
            e.printStackTrace();
        }
        //get the date to check
        final Calendar checkDate = Calendar.getInstance();
        checkDate.setTime(date);
        //we're always checking if theres something tomorrow
        checkDate.add(Calendar.DATE, 1);
        String tomorrowsDate = dateFormat.format(checkDate.getTime());
        System.out.println("FRIENDS CHECK  DATE IS: "+ tomorrowsDate);

        ParseQuery<ParseEventModel> tomorrowsEvents = ParseQuery.getQuery("ParseEventModel");
        tomorrowsEvents.whereEqualTo("user", user);
        tomorrowsEvents.whereEqualTo("start_date", tomorrowsDate);
        tomorrowsEvents.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                System.out.println("FRIEND HAS: "+parseEventModels.size() + " events tomorrow");
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
        notifyDataSetChanged();

        System.out.println("FRIEND CAN I GO OUT: "+ CAN_I_GO_OUT+ " "+CAN_I_GO_OUT_MESSAGE);
        if(CAN_I_GO_OUT == 0) {
            canI.setText("YES");
            canI.setBackgroundResource(R.color.green);
            //message.setText(CAN_I_GO_OUT_MESSAGE);
        } else if(CAN_I_GO_OUT == 1) {
            canI.setText("NO");
            canI.setBackgroundResource(R.color.red);
            canI.setEnabled(false);
            //message.setText(CAN_I_GO_OUT_MESSAGE);
        } else {
            canI.setText("YES");
            canI.setEnabled(true);
            canI.setBackgroundResource(R.color.green);
        }
        notifyDataSetChanged();
        return convertView;
    }
}