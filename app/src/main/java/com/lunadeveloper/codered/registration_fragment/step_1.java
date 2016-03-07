package com.lunadeveloper.codered.registration_fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.RegistrationActivity;
import com.lunadeveloper.codered.login.ParseLoginDispatchActivity;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.ParseException;
import com.parse.SignUpCallback;

public class step_1 extends Fragment {
    public static String TAG = step_1.class.getSimpleName();
    LinearLayout mView;
    EditText username;
    EditText password;
    EditText confirm_passowrd;
    EditText phone;
    Button next;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = (LinearLayout) inflater.inflate(R.layout.fragment_registration_1, container, false);
        next = (Button) mView.findViewById(R.id.one_next);
        username = (EditText) mView.findViewById(R.id.reg1_username);
        password = (EditText) mView.findViewById(R.id.reg1_password);
        confirm_passowrd = (EditText) mView.findViewById(R.id.reg1_confirm_password);
        phone = (EditText) mView.findViewById(R.id.reg1_phone);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = username.getText().toString();
                String mPassword = password.getText().toString();
                String mConfirmPassword = confirm_passowrd.getText().toString();
                String mPhone = phone.getText().toString();

                if(mUsername.length() > 3 && mPassword.length() > 3 && mConfirmPassword.length() > 3) {
                    if(mPassword.equals(mConfirmPassword)) {
                        final ParseUserModel user = new ParseUserModel("legacy");
                        user.setUsername(mUsername);
                        user.setPassword(mPassword);
                        user.setPhone(mPhone);

                        user.signUpInBackground(new SignUpCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    //success go to step 2
                                    Toast.makeText(mView.getContext(), "Registration Successful\n", Toast.LENGTH_SHORT).show();
                                    System.out.println("CREATING NOTIFICATION FRAGMENT");
                                    RegistrationActivity a = (RegistrationActivity) getActivity();
                                    a.replaceFragment(new step_2(), false, FragmentTransaction.TRANSIT_FRAGMENT_FADE, "register");
                                } else {
                                    Toast.makeText(mView.getContext(), "Registration Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    // Sign up didn't succeed. Look at the ParseException
                                    // to figure out what went wrong
                                    Log.e(TAG, "Login failed: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        Toast.makeText(mView.getContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(mView.getContext(), "Username and password are required", Toast.LENGTH_LONG).show();
                }
            }
        });
        return mView;
    }
}
