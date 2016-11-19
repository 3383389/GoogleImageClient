package com.example.android.lesson5googleimg.fragment;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.lesson5googleimg.provider.DownloadImg;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ViewImageFragment extends Fragment {

    ImageView fullImage;
    Bitmap bitmap;
    String url;
    int pos;

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

        pos = getArguments().getInt("position");
        url = ImageProvider.getInstance().getResults().getLink(pos);

        setFullImage();
        Log.v("frag", "start ViewImageFragment " + url);
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

    public boolean setFullImage() {
        Log.v("frag", " SET_FULL_IMAGE url = " + url);
        if (url != null) {
            bitmap = ImageProvider.getInstance().getCache(url + "full");
            if (bitmap != null) {
                fullImage.setImageBitmap(bitmap);
                return true;
            } else {
                new DownloadImg(true, pos).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            }
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


}
