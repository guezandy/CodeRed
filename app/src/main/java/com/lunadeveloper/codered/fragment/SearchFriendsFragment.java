package com.lunadeveloper.codered.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.adapter.SearchResultsAdapter;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchFriendsFragment extends Fragment {
    public String TAG = SearchFriendsFragment.class.getSimpleName();

    private RelativeLayout mView;
    private EditText search;
    private ListView results;
    private Button searchButton;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (RelativeLayout) inflater.inflate(R.layout.fragment_search_friends, container, false);

        search = (EditText) mView.findViewById(R.id.timePicker);
        results = (ListView) mView.findViewById(R.id.results);
        searchButton = (Button) mView.findViewById(R.id.button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if(search.getText().toString().length() == 0) {
                    Toast.makeText(getActivity().getBaseContext(), "Search field is empty", Toast.LENGTH_SHORT).show();
                } else {

                    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                    userQuery.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    userQuery.whereContains("username", search.getText().toString());
                    userQuery.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(final List<ParseUser> parseUsers, ParseException e) {
                            final SearchResultsAdapter adapter = new SearchResultsAdapter(getActivity().getBaseContext(), parseUsers);

                            for(final ParseUser u : parseUsers) {
                                //Did i already request user u
                                ParseQuery<ParseObject> aFriends = ParseQuery.getQuery("friends");
                                aFriends.whereEqualTo("one", ParseUser.getCurrentUser());
                                aFriends.whereEqualTo("two", u);
                                aFriends.whereEqualTo("status", "requested");
                                //did user u request me
                                ParseQuery<ParseObject> mFriends = ParseQuery.getQuery("friends");
                                mFriends.whereEqualTo("two", ParseUser.getCurrentUser());
                                mFriends.whereEqualTo("one", u);
                                mFriends.whereEqualTo("status", "requested");

                                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                                queries.add(aFriends);
                                queries.add(mFriends);

                                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                                mainQuery.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                        System.out.println("FOUND already friends: "+parseObjects.size());
                                        if(parseObjects.size() > 0) {
                                            for(ParseObject x : parseObjects) {
                                                System.out.println("REMOVING: "+ x.getObjectId()+" " + u.getString("full_name"));
                                                parseUsers.remove(u);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }
                                });
                            }
                            //System.out.println("NAME: "+u.getString("full_name"));;
                            results.setAdapter(adapter);
                        }
                    });
                }
            }
        });
        return mView;
    }



}
