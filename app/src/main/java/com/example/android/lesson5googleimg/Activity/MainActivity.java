package com.example.android.lesson5googleimg.Activity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.lesson5googleimg.Adapter.ImageAdapter;
import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.EventBus.Messages;
import com.example.android.lesson5googleimg.Fragment.SearchFragment;
import com.example.android.lesson5googleimg.Fragment.StartFragment;
import com.example.android.lesson5googleimg.Fragment.ViewImageFragment;
import com.example.android.lesson5googleimg.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageAdapter.initImageAdapter(this);

        if (savedInstanceState == null) {
            showFragment(Messages.OPEN_START_FRAGMENT);
            Log.v("frag", "OPEN_START_FRAGMENT");
        } else {
            Log.v("frag", " NOT  OPEN_START_FRAGMENT");

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public void showFragment(Messages message) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = null;
        switch (message) {
            case OPEN_SEARCH_FRAGMENT:
                fragment = new SearchFragment();
                break;
            case OPEN_START_FRAGMENT:
                fragment = new StartFragment();
                break;
            case OPEN_VIEW_IMG_FRAGMENT:
                fragment = new ViewImageFragment();
                Log.v("frag", "event open view fragment");
                break;
        }

        fragmentTransaction.replace(R.id.main_activity_container, fragment);
        if (!(message == Messages.OPEN_START_FRAGMENT)) {
            fragmentTransaction.addToBackStack("back");
        }
        fragmentTransaction.commit();
    }


    @Subscribe
    public void onMessageEvent(final MessageEvent event) {
        Log.v("frag", "event ok");
        switch (event.message) {
            case OPEN_SEARCH_FRAGMENT:
                showFragment(event.message);
                break;
            case OPEN_START_FRAGMENT:
                showFragment(event.message);
                break;
            case OPEN_VIEW_IMG_FRAGMENT:
                showFragment(event.message);
                break;
            case SEARCH_IMG:
                if (checkConnection()) {
                    Log.v("frag", "connection ok");
                    try {
                        ImageAdapter.getInstance().searchResults(event.str);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("frag", "send searchResults ok");
                } else {
                    Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
                    ImageAdapter.getInstance().getResultsFromPref(event.str);
                }
                break;
        }
    }

    public boolean checkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            ImageAdapter.getInstance().NETConnection = true;
            return true;
        } else {
            return false;
        }
    }

}
