package com.example.android.lesson5googleimg.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.adapter.RecyclerViewAdapter;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        result = (TextView) rootView.findViewById(R.id.resultSearch);
        mFindButton = (Button) rootView.findViewById(R.id.find_button);

        mFindButton.setOnClickListener(this);

        setSearchQuery();
        initRecyclerView(rootView);
        showImages();

        Log.v("frag", "start onCreateView");

        return rootView;
    }

    private void setSearchQuery() {
        String searchQuery = ImageProvider.getInstance().mQuery;
        if (searchQuery != null) {
            result.setText(searchQuery.replaceAll("\\+", " "));
        }
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
        mAdapter = new RecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    public Boolean isItemVisible(int pos) {
        return (pos >= ((GridLayoutManager)mLayoutManager).findFirstVisibleItemPosition())
                || (pos <= ((GridLayoutManager)mLayoutManager).findLastVisibleItemPosition());
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
            case UPDATE_ITEM:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(event.position);
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
