package com.example.android.lesson5googleimg.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.android.lesson5googleimg.provider.ImageProvider;
import com.example.android.lesson5googleimg.R;
import com.squareup.picasso.Picasso;


public class ViewImageFragment extends Fragment {

    ImageView fullImage;
    String url;
    int pos;

    public ViewImageFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_view_image, container, false);

        fullImage = (ImageView) rootView.findViewById(R.id.full_img);

        pos = getArguments().getInt("position");
        url = ImageProvider.getInstance().getResults().getLink(pos);

        setFullImage();
        Log.v("frag", "start ViewImageFragment " + url);
        setRetainInstance(true);
        return rootView;
    }

    public void setFullImage() {
        Picasso.with(getContext())
                .load(url)
                .fit()
                .priority(Picasso.Priority.HIGH)
                .centerCrop()
                .into(fullImage);
    }
}
