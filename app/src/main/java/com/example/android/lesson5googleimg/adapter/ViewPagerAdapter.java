package com.example.android.lesson5googleimg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import com.example.android.lesson5googleimg.fragment.FullImageFragment;
import com.example.android.lesson5googleimg.provider.ImageProvider;



public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        Log.v("view", "fm " + fm);
    }

    @Override
    public Fragment getItem(int position) {
        Log.v("view", "pos item = " + position +  " getCount = " + getCount());
        return FullImageFragment.newInstance(position);
    }


    @Override
    public int getCount() {
        return ImageProvider.getInstance().getResults().items == null ? 0 : ImageProvider.getInstance().getResults().items.size();
    }

}
