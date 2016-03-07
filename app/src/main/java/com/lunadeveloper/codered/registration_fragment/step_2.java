package com.lunadeveloper.codered.registration_fragment;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.RegistrationActivity;
import com.lunadeveloper.codered.model.ParseUserModel;
import com.parse.ParseUser;

public class step_2 extends Fragment {
    public LinearLayout mView;
    public TextView banner;
    public Button ImageUpload;
    public Button next;
    public EditText name;
    private int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("STEP2");
        mView = (LinearLayout) inflater.inflate(R.layout.fragment_registration_2, container, false);
        banner = (TextView) mView.findViewById(R.id.reg2_banner);
        next = (Button) mView.findViewById(R.id.reg2_next);
        name = (EditText) mView.findViewById(R.id.reg2_name);
        ImageUpload = (Button) mView.findViewById(R.id.imageUpload);

        final ParseUserModel user = (ParseUserModel) ParseUser.getCurrentUser();
        banner.setText("You snagged @"+user.getUsername() + " but don't you want your name/image in the mix? ");

        ImageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
// Always show the chooser (if there are multiple options available)
                getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length() > 0) {
                    user.setFullName(name.getText().toString());
                    user.saveInBackground();
                    RegistrationActivity a = (RegistrationActivity) getActivity();
                    a.replaceFragment(new step_3(), false, FragmentTransaction.TRANSIT_FRAGMENT_FADE, "register");
                } else {
                    Toast.makeText(mView.getContext(), "Hey, forgot your name", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return mView;
    }
}
