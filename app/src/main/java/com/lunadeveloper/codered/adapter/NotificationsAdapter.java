package com.lunadeveloper.codered.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lunadeveloper.codered.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class NotificationsAdapter extends ArrayAdapter<ParseUser> {

    public ParseUser requester;

    public NotificationsAdapter(Context context, List<ParseUser> requests) {
        super(context, 0, requests);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final ParseUser user = getItem(position);
        System.out.println("REQU: 1 " + user.getObjectId());

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_notifications, parent, false);
        }
        // Lookup view for data population
        final TextView tvName = (TextView) convertView.findViewById(R.id.name);
        final Button approve = (Button) convertView.findViewById(R.id.approve);
        final Button deny = (Button) convertView.findViewById(R.id.deny);

        // Populate the data into the template view using the data object
        /*req.getParseUser("one").fetchInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                requester = user;
            }
        });*/

        tvName.setText(user.getString("full_name"));

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approve.setVisibility(View.GONE);
                deny.setText("APPROVED");
                deny.setBackgroundResource(R.color.red);

                ParseQuery<ParseObject> confirm = ParseQuery.getQuery("friends");
                confirm.whereEqualTo("one", user);
                confirm.whereEqualTo("two", ParseUser.getCurrentUser());
                confirm.whereEqualTo("status", "requested");
                confirm.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        System.out.println("HEY HEY HYE WE GOT" + parseObject.getObjectId());
                        if(parseObject != null) {
                            parseObject.put("status", "approved");
                            parseObject.put("hey", 1);
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        System.out.println("ERRROR " + e.getMessage());
                                    } else {
                                        System.out.println("SAVEEEDDDDDD: ");
                                    }
                                }
                            });
                        } else {
                            System.out.println("THERES NO OBJECT");
                        }
                    }
                });

                /*ParseObject friendRequest = new ParseObject("friends");
                friendRequest.put("two", ParseUser.getCurrentUser());
                friendRequest.put("one", requester);
                friendRequest.put("status", "approved");
                friendRequest.saveInBackground();
                */

                ParseObject reverseFriendRequest = new ParseObject("friends");
                reverseFriendRequest.put("one", ParseUser.getCurrentUser());
                reverseFriendRequest.put("two", user);
                reverseFriendRequest.put("status", "approved");
                reverseFriendRequest.saveInBackground();
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "By ignoring it you'll be denying it, haha", Toast.LENGTH_LONG).show();
            }
        });


        return convertView;
    }
}