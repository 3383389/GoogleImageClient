package com.example.android.lesson5googleimg.adapter;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.example.android.lesson5googleimg.holder.RecyclerViewHolder;
import com.example.android.lesson5googleimg.R;
import org.greenrobot.eventbus.EventBus;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ImageProvider imageProvider;

    public RecyclerViewAdapter() {
        imageProvider = ImageProvider.getInstance();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_image, parent, false);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.img.setImageBitmap(null);

        // set image
        holder.img.setImageBitmap(imageProvider.getImage(position));

        // if click on photo - run loading full image
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("frag", "click on photo ok " + " on position = " + position);
                EventBus.getDefault().post(new MessageEvent(Messages.OPEN_VIEW_IMG_FRAGMENT, position));
            }
        });

        // if and of list - get more photos
        if (position == getItemCount() - 3 && imageProvider.checkConnection()) {
            EventBus.getDefault().post(new MessageEvent(Messages.SEARCH_IMG, imageProvider.mQuery));
        }

    }

    @Override
    public int getItemCount() {
        return imageProvider.getResults().items == null ? 0 : imageProvider.getResults().items.size();
    }
}
