package com.example.android.lesson5googleimg.fragment;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.lesson5googleimg.R;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FullImageFragment extends Fragment {

    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";

    ImageView img;
    TextView textError;
    ProgressBar progressBar;
    String url;
    int pageNumber;


    public static FullImageFragment newInstance(int page) {
        FullImageFragment fullFragment = new FullImageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_PAGE_NUMBER, page);
        fullFragment.setArguments(arguments);
        return fullFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootVeiw = inflater.inflate(R.layout.fragment_full_image, container, false);
        img = (ImageView) rootVeiw.findViewById(R.id.full_img);
        progressBar = (ProgressBar) rootVeiw.findViewById(R.id.full_progress_bar);
        textError = (TextView) rootVeiw.findViewById(R.id.full_text_error);

        pageNumber = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        url = ImageProvider.getInstance().getResults().getLink(pageNumber);

        setFullImage();

        return rootVeiw;
    }

    public void setFullImage() {
        Picasso.with(getContext())
                .load(url)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .centerCrop()
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.INVISIBLE);
                        textError.setVisibility(View.VISIBLE);
                    }
                });
    }

}
