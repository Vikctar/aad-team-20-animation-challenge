package com.alcpluralsight.aad_team20.info;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.alcpluralsight.aad_team20.models.Result;

public class MovieInfoSourceFactory extends DataSource.Factory<Integer, Result> {
    private String sort_criteria;
    private MutableLiveData<MovieInfoSource> liveData;
    private MovieInfoSource infoSource;

    public MovieInfoSourceFactory(String sort_criteria) {
        this.sort_criteria = sort_criteria;
        liveData = new MutableLiveData<>();
    }

    @NonNull
    @Override
    public DataSource<Integer, Result> create() {
        infoSource = new MovieInfoSource(sort_criteria);
        liveData = new MutableLiveData<>();
        liveData.postValue(infoSource);
        return null;
    }
}
