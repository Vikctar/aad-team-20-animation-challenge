package com.alcpluralsight.aad_team20.interfaces;


import com.alcpluralsight.aad_team20.models.Movie;

public interface OnGetMovieCallback {

    void onSuccess(Movie movie);

    void onError();
}
