package com.autroidbusiness.mynavigation.sg.fragment;

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
import androidx.fragment.app.Fragment;

import com.autroidbusiness.mynavigation.R;


public class FragmentThriller extends Fragment {

    private static final String KEY_MOVIE_TITLE = "key_title";

    public FragmentThriller() {
        // Required empty public constructor
    }

    public static FragmentThriller newInstance(String movieTitle) {
        FragmentThriller fragmentThriller = new FragmentThriller();
        Bundle args = new Bundle();
        args.putString(KEY_MOVIE_TITLE, movieTitle);
        fragmentThriller.setArguments(args);

        return fragmentThriller;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_thriller, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Drawable movieIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.movie_icon, getContext().getTheme());
        if (movieIcon != null) {
            movieIcon.setColorFilter( ContextCompat.getColor(getContext(), R.color.orange), PorterDuff.Mode.SRC_ATOP);
        }
        ((ImageView) view.findViewById(R.id.movie_icon)).setImageDrawable(movieIcon);

        String movieTitle = getArguments().getString(KEY_MOVIE_TITLE);
        ((TextView) view.findViewById(R.id.movie_title)).setText(movieTitle);
    }
}
