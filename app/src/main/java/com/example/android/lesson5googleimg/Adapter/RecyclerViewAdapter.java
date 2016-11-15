package com.example.android.lesson5googleimg.Adapter;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.android.lesson5googleimg.EventBus.MessageEvent;
import com.example.android.lesson5googleimg.EventBus.Messages;
import com.example.android.lesson5googleimg.Holder.RecyclerViewHolder;
import com.example.android.lesson5googleimg.R;
import org.greenrobot.eventbus.EventBus;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ImageAdapter imageAdapter;

    public RecyclerViewAdapter() {
        imageAdapter = ImageAdapter.getInstance();
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_image, parent, false);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        holder.img.setImageBitmap(null);
        // get URL of image
        final String url = imageAdapter.getResults().getLink(position);
        // get img from cache
        Bitmap bitmapFromCache = imageAdapter.getCache(url);
        Log.v("frag", "bitmapFromCache = " + position + url);

        // if image exist in cache - set to holder, else load from internet and save in cache
        if (bitmapFromCache != null) {
            holder.img.setImageBitmap(bitmapFromCache);
            Log.v("frag", "set bitmap from Cache" + " on position = " + position + " link " + url);
        } else if (!imageAdapter.getResults().items.get(position).isLoading && imageAdapter.NETConnection) {
            new DownloadImg(false).execute(url);
            imageAdapter.getResults().items.get(position).isLoading = true;
        }

        // if and of list - get more photos
        if (position == getItemCount() - 1 && imageAdapter.NETConnection) {
            EventBus.getDefault().post(new MessageEvent(Messages.SEARCH_IMG, imageAdapter.mQuery));
        }

        // if click on photo - run loading full image
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("frag", "click on photo ok " + " on position = " + position + "url = " + url);
                EventBus.getDefault().post(new MessageEvent(Messages.OPEN_VIEW_IMG_FRAGMENT));
                new DownloadImg(true).execute(url);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageAdapter.getResults().items == null ? 0 : imageAdapter.getResults().items.size();
    }
}
