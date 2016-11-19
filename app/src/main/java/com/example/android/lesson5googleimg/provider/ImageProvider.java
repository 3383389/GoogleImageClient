package com.example.android.lesson5googleimg.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.example.android.lesson5googleimg.models.GResults;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;


public class ImageProvider {

    // API key
    //private final String KEY = "AIzaSyA8Lls-n0MJnRmi1dOWrTyjhTnt-g2IjKM";
    //private final String KEY = "AIzaSyB3wFBNESSIC70MhQ3xGTMaxX_4YYOnAUY";
    private final String KEY = "AIzaSyDAl2qL3_a5SCDC1-q56kUFZss8CrOc63Q";

    //API engine number
    //private final String CX = "010292657076463166936:6zp0sbuhuo4";
    //private final String CX = "010292657076463166936:zsfpx8vl6v4";
    private final String CX = "010292657076463166936:wl0im3j1p4g";

    private final String SEARCH_TYPE = "image";
    private final String SHARED_PREF = "com.example.android.lesson5googleimg.preferences";

    private static ImageProvider mInstance;
    private GResults results;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int startIndex;
    public String mQuery;
    private Context context;

    private ImageProvider(Context context) {
        this.context = context;
        results = new GResults();
        pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static ImageProvider initImageAdapter(Context context) {
        Log.v("frag", "ImageProvider INIT");
        if (mInstance == null) {
            mInstance = new ImageProvider(context);
        }
        return mInstance;
    }

    public static ImageProvider getInstance() {
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
                saveResultsToPref();
            }

        }).start();
    }

    private void saveResultsToPref() {
        editor.putString(mQuery, new Gson().toJson(results));
        editor.commit();
        Log.v("frag", "saveResultsToPref do = ");
    }

    // if no connection
    public void getResultsFromPref(String query) {
        String resSet = pref.getString(query, null);
        if (resSet != null) {
            results = new Gson().fromJson(resSet, GResults.class);
            EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_RECYCLER_VIEW));
            mQuery = query;
        }
    }

    public GResults getResults() {
        return results;
    }

    public boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
