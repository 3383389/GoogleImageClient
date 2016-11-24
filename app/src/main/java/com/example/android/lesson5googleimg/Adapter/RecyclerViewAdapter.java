package com.example.android.lesson5googleimg.adapter;


import android.content.Context;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import org.greenrobot.eventbus.EventBus;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private ImageProvider imageProvider;
    Context context;

    public RecyclerViewAdapter(Context context) {
        imageProvider = ImageProvider.getInstance();
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_image, parent, false);
        return new RecyclerViewHolder(layoutView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerViewHolder holder) {
        super.onViewAttachedToWindow(holder);


    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {

        holder.img.setImageBitmap(null);
        holder.progressBar.setVisibility(View.VISIBLE);
        holder.textError.setVisibility(View.INVISIBLE);

        Picasso.with(context)
                .load(imageProvider.getResults().getLink(position))
                .fit()
                .centerCrop()
                .into(holder.img, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                        holder.textError.setVisibility(View.VISIBLE);
                    }
                });

        // if click on photo - run loading full image
        holder.img.setOnClickListener(v -> {
            Log.v("frag", "click on photo ok " + " on position = " + position);
            EventBus.getDefault().post(new MessageEvent(Messages.OPEN_VIEW_IMG_FRAGMENT, position));
        });

    }

    @Override
    public int getItemCount() {
        return imageProvider.getResults().items == null ? 0 : imageProvider.getResults().items.size();
    }
}
