package com.alcpluralsight.aad_team20.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.alcpluralsight.aad_team20.info.MovieInfoSourceFactory;
import com.alcpluralsight.aad_team20.models.Result;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.alcpluralsight.aad_team20.webapi.Constant.NUMBER_OF_THREADS_SIX;
import static com.alcpluralsight.aad_team20.webapi.Constant.PAGE_LOAD_SIZE_HINT;
import static com.alcpluralsight.aad_team20.webapi.Constant.PAGE_SIZE;
import static com.alcpluralsight.aad_team20.webapi.Constant.PREFETCH_DISTANCE;

public class MainViewModel extends ViewModel {
    private  String sort_criteria;
    private LiveData<PagedList<Result>> listLiveData ;

    public MainViewModel(String sort_criteria){
        this.sort_criteria = sort_criteria;

        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS_SIX);

        MovieInfoSourceFactory sourceFactory = new MovieInfoSourceFactory(sort_criteria);
        PagedList.Config  config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setInitialLoadSizeHint(PAGE_LOAD_SIZE_HINT)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .build();
        listLiveData = new LivePagedListBuilder<>(sourceFactory, config)
                .setFetchExecutor(executor)
                .build();
    }
    public LiveData<PagedList<Result>> getListLiveData(){
        return listLiveData;
    }
}
