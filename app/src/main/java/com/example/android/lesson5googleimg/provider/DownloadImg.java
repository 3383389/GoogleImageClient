package com.example.android.lesson5googleimg.provider;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.lesson5googleimg.adapter.ImageAdapter;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import org.greenrobot.eventbus.EventBus;
import java.io.IOException;


public class DownloadImg extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private Boolean isFullImage;
    private int pos;


    public DownloadImg(Boolean isFull, int pos) {
        this.isFullImage = isFull;
        this.pos = pos;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        url = params[0];
        Bitmap b = null;
        Log.v("frag", "doInBackground  link = " + url);

        // download and decode bitmap from url
        try {
            b = ImageAdapter.decodeSampledBitmapFromUrl(url, isFullImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            if (!isFullImage) {
                Log.v("frag", "put in cache and update " + url);
                // put bitmap to cache and update recycler view
                ImageProvider.getInstance().putToCache(bitmap, url);
                EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_ITEM, pos));
            } else {
                ImageProvider.getInstance().putToCache(bitmap, url + "full");
                EventBus.getDefault().post(new MessageEvent(Messages.SET_FULL_IMAGE, url));
                Log.v("frag", "onPostExecute full ok " + url);
            }
        }
    }
}