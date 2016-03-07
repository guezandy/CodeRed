package com.lunadeveloper.codered.adapter;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lunadeveloper.codered.R;
import com.lunadeveloper.codered.fragment.LaunchViewPagerFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class LaunchFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] {
            "One thing",
            "Twi thing",
            "red thing",
            "blue thing", };

    protected static final int[] ICONS = new int[] {
            R.drawable.canigoout_button,
            R.drawable.canigoout_button,
            R.drawable.canigoout_button,
            R.drawable.canigoout_button
    };

    private int mCount = CONTENT.length;

    public LaunchFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return LaunchViewPagerFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return LaunchFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
