package com.autroidbusiness.mynavigation.sg.fragment;

import android.app.Fragment;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.autroidbusiness.mynavigation.R;


public class FragmentComedy extends Fragment {

    private static final String KEY_MOVIE_TITLE = "key_title";

    public FragmentComedy() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment FragmentComedy.
     */
    public static FragmentComedy newInstance(String movieTitle) {
        FragmentComedy fragmentComedy = new FragmentComedy();
        Bundle args = new Bundle();
        args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentComedy.setArguments(args);

        return fragmentComedy;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comedy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Drawable movieIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.movie_icon, getContext().getTheme());
        if (movieIcon != null) {
            movieIcon.setColorFilter( ContextCompat.getColor(getContext(), R.color.purple), PorterDuff.Mode.SRC_ATOP);
        }
        ((ImageView) view.findViewById(R.id.movie_icon)).setImageDrawable(movieIcon);

        String movieTitle = getArguments().getString(KEY_MOVIE_TITLE);
        ((TextView) view.findViewById(R.id.movie_title)).setText(movieTitle);
    }
}
