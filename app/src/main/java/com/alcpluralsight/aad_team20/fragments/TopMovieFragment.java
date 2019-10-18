package com.alcpluralsight.aad_team20.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcpluralsight.aad_team20.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TopMovieFragment extends Fragment {


    public TopMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_movie, container, false);
    }

}
