package com.lunadeveloper.codered.adapter;


import android.app.Application;
import android.content.Context;
import android.util.Log;
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
import com.lunadeveloper.codered.model.ResultModel;
import com.lunadeveloper.codered.service.IParseResultCallback;
import com.lunadeveloper.codered.service.ParseService;
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
    public String TAG = FriendsGoOutAdapter.class.getSimpleName();

    public ParseUser requester;
    public Date date;
    public ParseService mParseService;

    public FriendsGoOutAdapter(Context context, List<ParseUser> requests) {
        super(context, 0, requests);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ParseUser user = getItem(position);
        mParseService = new ParseService(getContext());

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

        if(user == null) {
            System.out.println("USER IS NULL FOR this one");
        } else {
            System.out.println("USER IS NOT NULL");
        }

        mParseService.checkGoOut(user, tomorrowsDate, new IParseResultCallback() {
            @Override
            public void onSuccess(ResultModel result) {
                System.out.println("FRIEND CAN I GO OUT: "+ result.getStatus()+ " "+result.getMessage());
                if(result.getStatus() == 0) {
                    canI.setText("YES");
                    canI.setBackgroundResource(R.color.green);
                } else if(result.getStatus() == 1) {
                    canI.setText("NO");
                    canI.setBackgroundResource(R.color.red);
                    canI.setEnabled(false);
                } else {
                    canI.setText("YES");
                    canI.setEnabled(true);
                    canI.setBackgroundResource(R.color.green);
                }
            }

            @Override
            public void onFail(String message) {
                Log.e(TAG, message);
            }
        });
        //notifyDataSetChanged();
        return convertView;
    }
}