package com.example.android.lesson5googleimg.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.EventBus.Messages;
import com.example.android.lesson5googleimg.Utils.DiskCache;
import com.example.android.lesson5googleimg.Models.GResults;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

public class ImageAdapter {

    //private final String KEY = "AIzaSyA8Lls-n0MJnRmi1dOWrTyjhTnt-g2IjKM";
    private final String KEY = "AIzaSyB3wFBNESSIC70MhQ3xGTMaxX_4YYOnAUY";
    //private final String CX = "010292657076463166936:6zp0sbuhuo4";
    private final String CX = "010292657076463166936:zsfpx8vl6v4";
    private final String SEARCH_TYPE = "image";

    private static ImageAdapter mInstance;
    private GResults results;
    private DiskCache diskCache;
    private SparseArray<Bitmap> images;
    private int startIndex;
    public String mQuery;

    private ImageAdapter(Context context) {
        results = new GResults();
        diskCache = new DiskCache(context);
        images = new SparseArray<>();
    }

    public static ImageAdapter initImageAdapter(Context context) {
        Log.v("frag", "ImageAdapter INIT");
        if (mInstance == null) {
            mInstance = new ImageAdapter(context);
        }
        return mInstance;
    }

    public static ImageAdapter getInstance() {
        return mInstance;
    }

    public void searchResults(String query) throws URISyntaxException, IOException {

        // if str repeat - increment startIndex
        if (mQuery != null && mQuery.equalsIgnoreCase(query)) {
            startIndex += 10;
            Log.v("frag", "startIndex = " + startIndex);
            Log.v("frag", "mQuery = " + mQuery + " str = " + query);
        } else {
            Log.v("frag", "New searchResults ... mQuery = " + mQuery + " str = " + query);
            mQuery = query;
            startIndex = 1;
            Log.v("frag", "images clear");
        }

        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + KEY
                + "&cx=" + CX
                + "&q=" + query
                + "&searchType=" + SEARCH_TYPE
                + "&start=" + startIndex
                + "&alt=json"
        );

        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        new Thread(new Runnable() {
            @Override
            public void run() {
                GResults tempRes;
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    tempRes = new Gson().fromJson(br, GResults.class);
                    Log.v("frag", "searchResults do");
                    // если это новый запрос - заменяем результаты,
                    // если запрос повторяется добавляем результаты к существующим
                    if (startIndex > 1)
                        results.getItems().addAll(tempRes.getItems());
                    else
                        results = tempRes;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                conn.disconnect();
                EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_RECYCLER_VIEW));
            }

        }).start();


    }

    public Bitmap getCache(String url) {
        return diskCache.getBitmapFromDiskCache(getKey(url));
    }

    void putToCache(Bitmap b, String url) {
        diskCache.put(getKey(url), b);
    }

    public SparseArray<Bitmap> getImages() {
        return images;
    }

    GResults getResults() {
        return results;
    }

    private String getKey(String url) {
        return url.hashCode() + "";
    }

}
