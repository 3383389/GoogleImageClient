package com.example.android.lesson5googleimg.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.lesson5googleimg.adapter.ViewPagerAdapter;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.R;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class ViewImageFragment extends Fragment {

    String url;
    int pos;

    ViewPager pager;
    PagerAdapter pagerAdapter;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.v("frag", "start onStart");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pos = getArguments().getInt("position");
        Log.v("view", "View image frag on create = " + pos);
        url = ImageProvider.getInstance().getResults().getLink(pos);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(pos);

        Log.v("view", "View image frag onCreateView = ok");

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.v("frag", "start onStop");
    }

    @Subscribe
    public void onMessageEvent(final MessageEvent event) {
        switch (event.message) {
            case UPDATE_RECYCLER_VIEW:
                pagerAdapter.notifyDataSetChanged();
                break;
        }
    }
}
