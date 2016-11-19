package com.example.android.lesson5googleimg.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;


public class ImageAdapter {

    public static Bitmap decodeSampledBitmapFromUrl(String url, Boolean isFullImage) throws IOException {

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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
