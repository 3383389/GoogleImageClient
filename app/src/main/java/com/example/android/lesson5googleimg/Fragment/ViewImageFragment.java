package com.example.android.lesson5googleimg.Fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.lesson5googleimg.Adapter.DownloadImg;
import com.example.android.lesson5googleimg.Adapter.ImageAdapter;
import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ViewImageFragment extends Fragment {

    ImageView fullImage;
    Bitmap bitmap;
    String url;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);

        fullImage = (ImageView) rootView.findViewById(R.id.full_img);

        Log.v("frag", "start ViewImageFragment");
        setRetainInstance(true);
        return rootView;
    }

    @Subscribe
    public void onMessageEvent(final MessageEvent event) {

        switch (event.message) {
            case SET_FULL_IMAGE:
                url = event.str;
                setFullImage();
                break;
        }
    }

    public void setFullImage() {
        Log.v("serv", "event SET_FULL_IMAGE url = " + url);
        if (url != null) {
            bitmap = ImageAdapter.getInstance().getCache(url);
            if (bitmap != null) {
                fullImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.v("frag", "start onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.v("frag", "start onStop");
    }


}
