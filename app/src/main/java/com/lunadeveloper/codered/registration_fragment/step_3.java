package com.lunadeveloper.codered.registration_fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.RegistrationActivity;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.ParseUser;

public class step_3 extends Fragment {
    LinearLayout mView;
    Switch syncCalendar;
    Switch syncContacts;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("STEP3");
        mView = (LinearLayout) inflater.inflate(R.layout.fragment_registration_3, container, false);
        syncCalendar = (Switch) mView.findViewById(R.id.sync_calendar);
        syncContacts = (Switch) mView.findViewById(R.id.sync_contacts);
        next = (Button) mView.findViewById(R.id.reg3_next);

        final ParseUserModel user = (ParseUserModel) ParseUser.getCurrentUser();

        syncCalendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    RegistrationActivity a = (RegistrationActivity) getActivity();
                    a.syncCalendar();
                    user.setCalendarSync(true);
                    user.saveInBackground();
                } else{
                    user.setCalendarSync(false);
                    user.saveInBackground();
                    Toast.makeText(mView.getContext(), "Your CALENDAR will not be sync'd anymore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        syncContacts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()) {
                    RegistrationActivity a = (RegistrationActivity) getActivity();
                    a.syncContacts();
                    user.setContactSync(true);
                    user.saveInBackground();
                } else {
                    user.setContactSync(false);
                    user.saveInBackground();
                    Toast.makeText(mView.getContext(), "Your CONTACTS will not be sync'd anymore", Toast.LENGTH_SHORT).show();

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationActivity a = (RegistrationActivity) getActivity();
                a.replaceFragment(new step_4(), false, FragmentTransaction.TRANSIT_FRAGMENT_FADE, "register");
            }
        });

        return mView;
    }
}