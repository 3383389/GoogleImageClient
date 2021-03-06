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

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    TextView result;
    Button findButton;

    private boolean isLoading = false;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;


    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.v("frag", "start onStart");
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
        findButton = (Button) rootView.findViewById(R.id.find_button);

        findButton.setOnClickListener(this);

        setSearchQuery();
        initRecyclerView(rootView);
        showImages();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    EventBus.getDefault().post(new MessageEvent(Messages.SEARCH_IMG, ImageProvider.getInstance().searchQuery));
                    isLoading = true;
                }
            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.v("frag", "start onStop");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_button:
                EventBus.getDefault().post(new MessageEvent(Messages.OPEN_SEARCH_FRAGMENT));
                break;
        }
    }

    private void setSearchQuery() {
        String searchQuery = ImageProvider.getInstance().searchQuery;
        if (searchQuery != null) {
            result.setText(searchQuery.replaceAll("\\+", " "));
        }
    }

    private void initRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void showImages() {
        adapter = new RecyclerViewAdapter(getContext());
        recyclerView.setAdapter(adapter);
    }

    @Subscribe
    public void onMessageEvent(final MessageEvent event) {
        Log.v("serv", "event ok");
        switch (event.message) {
            case UPDATE_RECYCLER_VIEW:
                adapter.notifyDataSetChanged();
                isLoading = false;
                break;
        }
    }
}
