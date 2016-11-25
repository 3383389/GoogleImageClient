package com.example.android.lesson5googleimg.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.example.android.lesson5googleimg.fragment.SearchFragment;
import com.example.android.lesson5googleimg.fragment.StartFragment;
import com.example.android.lesson5googleimg.fragment.ViewImageFragment;
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

        ImageProvider.initImageAdapter(this);

        if (savedInstanceState == null) {
            showFragment(Messages.OPEN_START_FRAGMENT, null);
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


    public void showFragment(Messages message, Integer i) {
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
                Bundle bundle = new Bundle();
                bundle.putInt("position", i);
                // set Fragmentclass Arguments
                fragment.setArguments(bundle);

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
                showFragment(event.message, null);
                break;
            case OPEN_START_FRAGMENT:
                showFragment(event.message, null);
                break;
            case OPEN_VIEW_IMG_FRAGMENT:
                showFragment(event.message, event.position);
                break;
            case SEARCH_IMG:
                if (ImageProvider.getInstance().checkConnection()) {
                    Log.v("frag", "connection ok");
                    try {
                        ImageProvider.getInstance().getGoogleResults(event.str);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v("frag", "send getGoogleResults ok");
                } else {
                    Toast.makeText(this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
                    ImageProvider.getInstance().getResultsFromPref(event.str);
                }
                break;
        }
    }

}
