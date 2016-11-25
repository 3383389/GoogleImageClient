package com.example.android.lesson5googleimg.fragment;


import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.android.lesson5googleimg.utils.eventBus.MessageEvent;
import com.example.android.lesson5googleimg.utils.eventBus.Messages;
import com.example.android.lesson5googleimg.R;
import org.greenrobot.eventbus.EventBus;


public class SearchFragment extends Fragment implements View.OnClickListener {

    Button searchButton;
    EditText searchText;

    public SearchFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        searchButton = (Button) rootView.findViewById(R.id.search_button_fragment);
        searchText = (EditText) rootView.findViewById(R.id.search_text);

        searchButton.setOnClickListener(this);

        Log.v("frag", "getGoogleResults onCreateView");

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button_fragment:
                if (!(searchText.getText().toString().trim().length() == 0)) {
                    getFragmentManager().popBackStack();
                    EventBus.getDefault().post(new MessageEvent(Messages.SEARCH_IMG, searchText.getText().toString().replaceAll("\\s+", "+")));
                    closeKeyboard(getActivity(), searchText.getWindowToken());

                } else {
                    Toast.makeText(getActivity(), "Введите текст", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static void closeKeyboard(Context c, IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

}
