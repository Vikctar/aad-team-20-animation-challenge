package com.alcpluralsight.aad_team20.info;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.alcpluralsight.aad_team20.models.MovieResponse;
import com.alcpluralsight.aad_team20.models.Result;
import com.alcpluralsight.aad_team20.webapi.APIClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.alcpluralsight.aad_team20.webapi.Constant.API_KEY;
import static com.alcpluralsight.aad_team20.webapi.Constant.LANGUAGE;
import static com.alcpluralsight.aad_team20.webapi.Constant.FIRSTPAGE;
import static com.alcpluralsight.aad_team20.webapi.Constant.PREVIOUSPAGE_KEY_ONE;
import static com.alcpluralsight.aad_team20.webapi.Constant.PREVIOUSPAGE_KEY_TWO;

public class MovieInfoSource extends PageKeyedDataSource<Integer, Result> {

    private String sort_criteria;
    public MovieInfoSource(String sort_criteria) {

        this.sort_criteria = sort_criteria;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Result> callback) {
        APIClient.getInstance().getApiService().getAllMovie(sort_criteria,API_KEY,LANGUAGE,FIRSTPAGE)
                .enqueue(new Callback<MovieResponse>(){
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response){
                        if (response.isSuccessful())
                        {
                            callback.onResult(response.body().getResults(),
                                    PREVIOUSPAGE_KEY_ONE , PREVIOUSPAGE_KEY_TWO);
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable throwable){

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Result> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Result> callback) {
        final int currentPage = params.key;
        APIClient.getInstance().getApiService().getAllMovie(sort_criteria,API_KEY,LANGUAGE,currentPage)
                .enqueue(new Callback<MovieResponse>(){
                    @Override
                    public void onResponse(Call<MovieResponse> call , Response<MovieResponse> response){
                        if (response.isSuccessful())
                        {
                            int nextPage = currentPage + 1;
                            callback.onResult(response.body().getResults(), nextPage);
                        }
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable throwable){

                    }
                });
    }
}
