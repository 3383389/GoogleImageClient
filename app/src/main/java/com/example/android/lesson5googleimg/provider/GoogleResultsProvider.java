package com.example.android.lesson5googleimg.provider;

import android.os.AsyncTask;
import android.util.Log;
import com.example.android.lesson5googleimg.models.GoogleResults;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;


public class GoogleResultsProvider extends AsyncTask<URL, Void, Void> {

    @Override
    protected Void doInBackground(URL... params) {
        URL url = params[0];
        GoogleResults tempRes;
        BufferedReader br = null;

        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        }

        try {
            assert conn != null;
            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        } catch (FileNotFoundException f) {
            Log.v("frag", "have no limit");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        tempRes = new Gson().fromJson(br, GoogleResults.class);
        Log.v("frag", "getGoogleResults do");
        // если это новый запрос - заменяем результаты,
        // если запрос повторяется добавляем результаты к существующим
        if (ImageProvider.getInstance().getStartIndex() > 1)
            ImageProvider.getInstance().getResults().getItems().addAll(tempRes.getItems());
        else {
            ImageProvider.getInstance().results = tempRes;
        }

        conn.disconnect();
        ImageProvider.getInstance().saveResultsToPref();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        EventBus.getDefault().post(new MessageEvent(Messages.UPDATE_RECYCLER_VIEW));
    }
}
