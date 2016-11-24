package com.example.android.lesson5googleimg.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lesson5googleimg.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public ImageView img;
    public ProgressBar progressBar;
    public TextView textError;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        img = (ImageView) itemView.findViewById(R.id.img);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        textError = (TextView) itemView.findViewById(R.id.text_error);

    }

}
