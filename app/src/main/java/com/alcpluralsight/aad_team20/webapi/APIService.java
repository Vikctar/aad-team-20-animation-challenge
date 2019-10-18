package com.alcpluralsight.aad_team20.webapi;

import com.alcpluralsight.aad_team20.models.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
    @GET("movie/{sort_criteria}")
    Call<MovieResponse> getAllMovie(
            @Path("sort_criteria") String sort_criteria,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );
}
