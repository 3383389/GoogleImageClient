package com.example.android.lesson5googleimg.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.example.android.lesson5googleimg.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView img;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.img);

    }

}
