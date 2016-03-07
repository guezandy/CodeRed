package com.lunadeveloper.codered.fragment;

import android.app.Application;
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

import com.lunadeveloper.codered.CodeRedApplication;
import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.adapter.CustomAdapter;
import com.lunadeveloper.codered.adapter.FriendsGoOutAdapter;
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


public class ListCalendarFragment extends Fragment {
    public String TAG = CalendarFragment.class.getSimpleName();
    private ParseService mParseService;
    public CalendarView calView;
    private RelativeLayout mView;
    public static int reasonsNotToGoOut = 0;
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private CustomAdapter urgentTodosAdapter;
    private ListView listView;
    private List<ParseUser> myFriends;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) mView.findViewById(R.id.list);

        myFriends = new ArrayList<ParseUser>();
        final FriendsGoOutAdapter adapter = new FriendsGoOutAdapter(getActivity().getApplicationContext(), myFriends);
        listView.setAdapter(adapter);
        listView.setEmptyView(mView.findViewById(android.R.id.empty));
        Application a = getActivity().getApplication();
        System.out.println("LISTLISTLIST "+((CodeRedApplication) a).getGoOutDate());
        ParseQuery<ParseObject> friends = ParseQuery.getQuery("friends");
        friends.whereEqualTo("one", ParseUser.getCurrentUser());
        friends.whereEqualTo("status", "approved");

        friends.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                //all my friends
                System.out.println("FOUND "+parseObjects.size() + " friends");
                for(final ParseObject request : parseObjects) {
                    System.out.println("MY FRIEND: "+ request.getObjectId());
                    request.getParseUser("two").fetchInBackground(new GetCallback<ParseUser>() {
                        @Override
                        public void done(ParseUser friend, ParseException e) {
                            myFriends.add(friend);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });

        if(myFriends != null) {
            for (ParseUser m : myFriends) {
                System.out.println("FRIEND: " + m.getString("full_name"));
            }
        }
        return mView;
    }
}
