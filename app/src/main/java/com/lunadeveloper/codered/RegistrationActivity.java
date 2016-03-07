package com.lunadeveloper.codered;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.Toast;

import com.lunadeveloper.codered.model.ParseEventModel;
import com.lunadeveloper.codered.registration_fragment.step_1;
import com.lunadeveloper.codered.service.ParseService;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class RegistrationActivity extends ActionBarActivity {
    public static String TAG = RegistrationActivity.class.getSimpleName();
    private int PICK_IMAGE_REQUEST = 1;
    public String Holidays[] = {
            "New Year’s Day	Thursday",
            "Martin Luther King, Jr.",
            "Good Friday",
            "Easter Sunday",
            "Memorial Day",
            "Independence Day",
            "Presidents' Day",
            "Labor Day",
            "Columbus Day",
            "General Election Day",
            "Veterans Day",
            "Thanksgiving Day",
            "Lincoln’s Birthday",
            "Washington’s Birthday",
            "Christmas Day"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Begin registration screen with step 1
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.registration_container, new step_1())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_registration_1, container, false);
            return rootView;
        }
    }

    /**
     * Handles adding all fragments to the view.
     * @param newFragment The fragment to add.
     * @param addToBackstack Whether this Fragment should appear in the backstack or not.
     * @param transition The transition animation to apply
     * @param backstackName The name
     */
    public void replaceFragment(android.support.v4.app.Fragment newFragment, boolean addToBackstack, int transition, String backstackName) {
        // use fragmentTransaction to replace the fragment
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.registration_container, newFragment, backstackName);
        if (addToBackstack) {
            fragmentTransaction.addToBackStack(backstackName);
        }
        fragmentTransaction.setTransition(transition);
        fragmentTransaction.commit();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                Bitmap imageScaled = Bitmap.createScaledBitmap(bitmap, 400, 400
                        * bitmap.getHeight() / bitmap.getWidth(), false);

                // Override Android default landscape orientation and save portrait
                Matrix matrix = new Matrix();
                //matrix.postRotate(-90);
                Bitmap rotatedScaledImage = Bitmap.createBitmap(imageScaled, 0,
                        0, imageScaled.getWidth(), imageScaled.getHeight(),
                        matrix, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                rotatedScaledImage.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                byte[] scaledData = bos.toByteArray();

                // always happens
                System.out.println("SIZE: "+scaledData.length);
                final ParseFile pf = new ParseFile("user.png", scaledData);
                pf.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            ParseUser.getCurrentUser().put("image", pf);
                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getApplicationContext(), "Profile image saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        System.out.println("SAVING ERROR: " + e.getMessage());
                                    }
                                }
                            });
                        } else {
                            System.out.println("FILE SAVING FAILED" + e.getMessage());
                        }
                    }
                });
                ImageView imageView = (ImageView) findViewById(R.id.userImage);
                imageView.setImageBitmap(rotatedScaledImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("ERROR");
            if(requestCode != PICK_IMAGE_REQUEST)
                System.out.println("ERROR1" + requestCode);
            if(resultCode != RESULT_OK)
                System.out.println("ERROR2" + resultCode);
            if(data == null)
                System.out.println("ERROR3"+ data);
            if(data.getData() == null)
                System.out.println("ERROR4" + data.getData());
        }
    }
    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getTime(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String addOneToDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "MM-dd-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        calendar.add(Calendar.DATE, 1);
        return formatter.format(calendar.getTime());
    }

    public static int getHour(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "hh");
        SimpleDateFormat amOrPm = new SimpleDateFormat(
                "a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        String hour = formatter.format(calendar.getTime());
        int mhour = Integer.parseInt(hour);
        if(amOrPm.format(calendar.getTime()).equals("PM")) {
            mhour = ((Integer.parseInt(hour) + 12)%24);
        }
        return mhour;
    }
    public void syncCalendar() {

        Calendar startTime = Calendar.getInstance();
        Calendar endTime= Calendar.getInstance();
        endTime.add(Calendar.MONTH, 1);
        //get all events a month and 2 days in advance just in case. users can check up to a month ahead so 2 day buffer
        endTime.add(Calendar.DATE, 2);
        String selection = "(( " + CalendarContract.Events.DTSTART + " >= " + startTime.getTimeInMillis() + " ) AND ( " + CalendarContract.Events.DTEND + " <= " + endTime.getTimeInMillis() + " ))";
        final Cursor cursor = getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, selection,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        //TODO: delete all entries from this person
        //Query limited to 100 entries
        ParseQuery<ParseEventModel> deleteQuery = ParseQuery.getQuery("ParseEventModel");
        deleteQuery.whereEqualTo("user", ParseUser.getCurrentUser());
        deleteQuery.findInBackground(new FindCallback<ParseEventModel>() {
            @Override
            public void done(List<ParseEventModel> parseEventModels, ParseException e) {
                System.out.println("Found this many entries to delete: "+parseEventModels.size());
                for(ParseEventModel event : parseEventModels) {
                    event.deleteInBackground();
                }
            }
        });

        //TODO: Insert only these entries
        System.out.println("Number of events: "+cursor.getCount());
        System.out.println("Length: "+ CNames.length);

        for (int i = 0; i < CNames.length; i++) { //for each event
            System.out.println(i+ " "+cursor.getString(1));
            System.out.println("START HOUR: "+ getHour(Long.parseLong(cursor.getString(3))));
            //minimum we need is a title and start time
            if(cursor.getString(1) != null && cursor.getString(3) != null) {
                ParseEventModel n = new ParseEventModel();
                n.setTitle(cursor.getString(1));
                String eStartDate = getDate(Long.parseLong(cursor.getString(3)));
                String eStartTime = getTime(Long.parseLong(cursor.getString(3)));
                if(eStartTime.equals("04:00:00 PM")) {
                    eStartDate = addOneToDate(Long.parseLong(cursor.getString(3)));
                }
                n.setStartDate(eStartDate);
                n.setStartTime(eStartTime);
                n.setStartHour(getHour(Long.parseLong(cursor.getString(3))));
                //if event is too early tomorrow
                if(getHour(Long.parseLong(cursor.getString(3))) <= Integer.parseInt(ParseUser.getCurrentUser().getString("early"))) {
                    n.setTooEarly(true);
                } else {
                    n.setTooEarly(false);
                }
                if(cursor.getString(2) != null) {
                    n.setDescription(cursor.getString(2));
                }
                if (cursor.getString(5) != null) {
                    n.setLocation(cursor.getString(5));
                }
                //is this one of the days off?
                if(Arrays.asList(Holidays).contains(cursor.getString(1))) {
                    n.setDayOff(true);
                } else {
                    n.setDayOff(false);
                }
                n.setUser();
                //n.put("hash", (cursor.getString(1) + cursor.getString(2) + Long.parseLong(cursor.getString(3))).hashCode());
                n.saveInBackground();
            }
            if(!cursor.isLast())
                cursor.moveToNext();
        }
        Toast.makeText(this.getBaseContext(), "Sync'd "+ CNames.length+ " calendar events", Toast.LENGTH_SHORT).show();
    }

    public void syncContacts() {

        final ParseUser currentUser = ParseUser.getCurrentUser();

        ArrayList<String> phoneNumbers = new ArrayList<String>();
        String phoneNumber = null;

        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        // Loop for every contact in the phone
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                //String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
                if (hasPhoneNumber > 0) {
                    //output.append("\n First Name:" + name);
                    // Query and loop for every phone number of the contact
                    Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        //output.append("\n Phone number:" + phoneNumber);
                        //BUILD LIST TO QUERY
                        phoneNumbers.add(phoneNumber);
                    }
                    phoneCursor.close();
                }
                //output.append("\n");
            }
        }
        System.out.println("FOUND: "+phoneNumbers.size()+ " contacts");
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereContainedIn("phone",phoneNumbers);
        //TODO: friends that aren't already friends
        userQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if(e == null) {
                    if(parseUsers.size() <= 0) {
                        System.out.println("FOUND NO MATCHES");
                    } else {
                        Toast.makeText(getBaseContext(), "Added "+parseUsers.size()+ " contacts", Toast.LENGTH_LONG).show();
                        System.out.println("FOUND");
                        for(ParseUser p : parseUsers) {
                            System.out.println("PERSON: "+p.getString("full_name"));
                            ParseObject friendRequest = new ParseObject("friends");
                            friendRequest.put("one", currentUser);
                            friendRequest.put("two", p);
                            friendRequest.put("status", "approved");
                            friendRequest.saveInBackground();

                            //TODO figure out reverse situation
                            ParseObject reverseFriendRequest = new ParseObject("friends");
                            friendRequest.put("two", currentUser);
                            friendRequest.put("one", p);
                            friendRequest.put("status", "syncd");
                            friendRequest.saveInBackground();
                        }
                    }
                } else {
                    System.out.println("FOUND AN ERROR: "+e.getMessage());
                }
            }
        });
    }

}
