package com.example.android.lesson5googleimg.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.android.lesson5googleimg.Adapter.RecyclerViewAdapter;
import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.EventBus.Messages;
import com.example.android.lesson5googleimg.R;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class StartFragment extends Fragment implements View.OnClickListener {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView result;
    Button mFindButton;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        result = (TextView) rootView.findViewById(R.id.resultSearch);
        mFindButton = (Button) rootView.findViewById(R.id.find_button);

        mFindButton.setOnClickListener(this);

        initRecyclerView(rootView);
        showImages();

        Log.v("frag", "start onCreateView");

        return rootView;
    }

    private void initRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_button:
                EventBus.getDefault().post(new MessageEvent(Messages.OPEN_SEARCH_FRAGMENT));
                break;
        }
    }

    private void showImages() {
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe
    public void onMessageEvent(final MessageEvent event) {
        Log.v("serv", "event ok");
        switch (event.message) {
            case UPDATE_RECYCLER_VIEW:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
                Log.v("frag", "mAdapter.notifyDataSetChanged()");
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.v("frag", "start onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.v("frag", "start onStop");
    }
}
