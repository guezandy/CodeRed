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
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.adapter.CustomAdapter;
import com.lunadeveloper.codered.adapter.NotificationsAdapter;
import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.service.IParseCallback;
import com.lunadeveloper.codered.service.ParseService;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class NotificationsFragment extends Fragment {
    public String TAG = CalendarFragment.class.getSimpleName();
    private ParseService mParseService;
    private RelativeLayout mView;
    private ListView list;
    public Set<String> myFriends;
    public List<ParseUser> theFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("CREATING NOTIFICATION FRAGMENT");
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        list = (ListView) mView.findViewById(R.id.list);
        theFriends = new ArrayList<ParseUser>();

        final NotificationsAdapter adapter = new NotificationsAdapter(getActivity().getApplicationContext(), theFriends);
        list.setAdapter(adapter);

        ParseQuery<ParseObject> friendRequests = ParseQuery.getQuery("friends");
        friendRequests.whereEqualTo("two", ParseUser.getCurrentUser());
        friendRequests.whereEqualTo("status", "requested");
        friendRequests.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> parseObjects, ParseException e) {
                System.out.println("FOUND this many requests: "+parseObjects.size());

                for(ParseObject f : parseObjects) {
                    f.getParseUser("one").fetchInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser one, ParseException e) {
                            System.out.println("ONE ONE ONE: "+ one.getUsername());
                            theFriends.add(one);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        return mView;
    }
}
