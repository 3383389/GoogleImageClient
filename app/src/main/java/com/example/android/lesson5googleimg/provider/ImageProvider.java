package com.example.android.lesson5googleimg.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.example.android.lesson5googleimg.models.GoogleResults;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;


public class ImageProvider {

    // API key
    private final String KEY = "AIzaSyA8Lls-n0MJnRmi1dOWrTyjhTnt-g2IjKM";
    //private final String KEY = "AIzaSyB3wFBNESSIC70MhQ3xGTMaxX_4YYOnAUY";
    //private final String KEY = "AIzaSyDAl2qL3_a5SCDC1-q56kUFZss8CrOc63Q";
    //API engine number
    private final String CX = "010292657076463166936:6zp0sbuhuo4";
    //private final String CX = "010292657076463166936:zsfpx8vl6v4";
    //private final String CX = "010292657076463166936:wl0im3j1p4g";
    private final String SEARCH_TYPE = "image";
    private final String SHARED_PREF = "com.example.android.lesson5googleimg.preferences";

    private static ImageProvider instance;
    public GoogleResults results;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public String searchQuery;
    private int startIndex;

    private ImageProvider(Context context) {
        this.context = context;
        results = new GoogleResults();
        pref = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static ImageProvider initImageAdapter(Context context) {
        if (instance == null) {
            instance = new ImageProvider(context);
        }
        return instance;
    }

    public static ImageProvider getInstance() {
        return instance;
    }

    public void getGoogleResults(String query) throws URISyntaxException, IOException {
        // if str repeat - increment startIndex
        setStartIndex(query);

        URL url = new URL(
                "https://www.googleapis.com/customsearch/v1?key=" + KEY
                        + "&cx=" + CX
                        + "&q=" + query
                        + "&searchType=" + SEARCH_TYPE
                        + "&start=" + startIndex
                        + "&alt=json"
        );
        new GoogleResultsProvider().execute(url);
    }

    private void setStartIndex(String query) {

        if (searchQuery != null && searchQuery.equalsIgnoreCase(query)) {
            startIndex += 10;
        } else {
            searchQuery = query;
            startIndex = 1;
        }

    }

    public void saveResultsToPref() {
        editor.putString(searchQuery, new Gson().toJson(results));
        editor.commit();
        Log.v("frag", "saveResultsToPref do = ");
    }

    // if no connection
    public void getResultsFromPref(String query) {
        String resSet = pref.getString(query, null);
        if (resSet != null) {
            results = new Gson().fromJson(resSet, GoogleResults.class);
            EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_RECYCLER_VIEW));
            searchQuery = query;
        }
    }

    public int getStartIndex() {
        return startIndex;
    }

    public GoogleResults getResults() {
        return results;
    }

    public boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
