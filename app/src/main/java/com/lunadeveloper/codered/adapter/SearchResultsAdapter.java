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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends ArrayAdapter<ParseUser> {

    public String status;

    public SearchResultsAdapter(Context context, List<ParseUser> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ParseUser user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_search_result, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.name);
        final Button add = (Button) convertView.findViewById(R.id.addAsFriend);
        // Populate the data into the template view using the data object
        tvName.setText(user.getString("full_name"));

        ParseQuery<ParseObject> friends = ParseQuery.getQuery("friends");
        friends.whereEqualTo("one", ParseUser.getCurrentUser());
        friends.whereEqualTo("two", user);

        friends.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if(parseObject != null) {
                    status = parseObject.getString("status");
                }
            }
        });
        if(status == null) {
            add.setText("Add as friend");
        } else if(status.equals("approved")) {
            add.setText("Friends");
            add.setEnabled(false);
        } else if(status.equals("requested")) {
            add.setText("Requested");
            add.setEnabled(false);
        } else {
            //TODO: something
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseObject friendRequest = new ParseObject("friends");
                friendRequest.put("one", ParseUser.getCurrentUser());
                friendRequest.put("two", user);
                friendRequest.put("status", "requested");
                friendRequest.saveInBackground();

                /*
                ParseObject reverseFriendRequest = new ParseObject("friends");
                reverseFriendRequest.put("two", ParseUser.getCurrentUser());
                reverseFriendRequest.put("one", user);
                reverseFriendRequest.put("status", "requested");
                reverseFriendRequest.saveInBackground();
                */

                Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
                add.setText("Requested");
                add.setEnabled(false);
            }
        });

        return convertView;
    }
}