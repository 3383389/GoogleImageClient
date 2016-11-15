package com.example.android.lesson5googleimg.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.EventBus.Messages;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;


public class DownloadImg extends AsyncTask<String, Void, Bitmap> {

    private String url;
    private Boolean isFullImage;

    public DownloadImg(Boolean isFull) {
        this.isFullImage = isFull;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        url = params[0];
        Bitmap b = null;
        Log.v("frag", "doInBackground  link = " + url);

        // download and decode bitmap from url
        try {
            b = decodeSampledBitmapFromUrl(url);
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
                ImageAdapter.getInstance().putToCache(bitmap, url);
                EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_RECYCLER_VIEW));
            } else {
                ImageAdapter.getInstance().putToCache(bitmap, url + "full");
                EventBus.getDefault().post(new MessageEvent(Messages.SET_FULL_IMAGE, url + "full"));
                Log.v("frag", "onPostExecute ok " + url);
            }
        }
    }

    private Bitmap decodeSampledBitmapFromUrl(String url) throws IOException {

        final int RECYCLER_WIDTH = 200;
        final int RECYCLER_HEIGHT = 200;
        final int FULL_WIDTH = 800;
        final int FULL_HEIGHT = 800;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new java.net.URL(url).openStream(), null, options);

        // Calculate inSampleSize
        if (isFullImage) {
            options.inSampleSize = calculateInSampleSize(options, FULL_WIDTH, FULL_HEIGHT);
        } else {
            options.inSampleSize = calculateInSampleSize(options, RECYCLER_WIDTH, RECYCLER_HEIGHT);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(new java.net.URL(url).openStream(), null, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}