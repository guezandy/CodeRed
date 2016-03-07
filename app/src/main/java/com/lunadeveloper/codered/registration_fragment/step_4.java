package com.lunadeveloper.codered.registration_fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lunadeveloper.codered.R;

public class step_4 extends Fragment {
    LinearLayout mView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("STEP2");
        mView = (LinearLayout) inflater.inflate(R.layout.fragment_registration_4, container, false);

        return mView;
    }
}
