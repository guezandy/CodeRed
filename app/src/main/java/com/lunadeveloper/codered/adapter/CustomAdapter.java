package com.lunadeveloper.codered.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lunadeveloper.codered.R;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class CustomAdapter extends ParseQueryAdapter<ParseObject> {

    public CustomAdapter(Context context) {
        // Use the QueryFactory to construct a PQA that will only show
        // Todos marked as high-pri
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("ParseEventModel");
                query.whereEqualTo("highPri", true);
                return query;
            }
        });
    }

    // Customize the layout by overriding getItemView
    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.urgent_item, null);
        }

        super.getItemView(object, v, parent);

        // Add the title view
        TextView titleTextView = (TextView) v.findViewById(R.id.date1);
        titleTextView.setText(object.getString("display"));

        // Add a reminder of how long this item has been outstanding
        TextView timestampView = (TextView) v.findViewById(R.id.reason1);
        timestampView.setText(object.getString("title"));
        return v;
    }

}