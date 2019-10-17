package com.alcpluralsight.aad_team20.models;

import java.util.List;

public interface OnGetMoviesCallback {

    void onSuccess(int page, List<Movie> movies);

    void onError();
}
