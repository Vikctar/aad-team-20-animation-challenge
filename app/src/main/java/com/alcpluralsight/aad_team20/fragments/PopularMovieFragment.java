package com.alcpluralsight.aad_team20.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcpluralsight.aad_team20.R;
import com.alcpluralsight.aad_team20.adaptors.MoviePageListAdaptor;
import com.alcpluralsight.aad_team20.models.Result;
import com.alcpluralsight.aad_team20.viewmodels.MainViewModel;
import com.alcpluralsight.aad_team20.viewmodels.MainViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMovieFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MoviePageListAdaptor adaptor;
    private MainViewModel viewModel;
    private String sort_criteria = "popular";


    public PopularMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_popular_movie, container, false);
        viewModel = ViewModelProviders.of(this, new MainViewModelFactory(sort_criteria))
                .get(MainViewModel.class);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adaptor = new MoviePageListAdaptor();
        recyclerView.setAdapter(adaptor);
        viewModel.getListLiveData().observe(this, new Observer<PagedList<Result>>(){

            @Override
            public void onChanged(PagedList<Result> results) {
                if(results !=null){
                    adaptor.submitList(results);
                }
            }
        });
        return view;
    }

}

