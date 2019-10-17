package com.alcpluralsight.aad_team20.models;




import com.alcpluralsight.aad_team20.genres.GenresResponse;
import com.alcpluralsight.aad_team20.genres.OnGetGenresCallback;
import com.alcpluralsight.aad_team20.interfaces.OnGetMovieCallback;
import com.alcpluralsight.aad_team20.reviews.OnGetReviewsCallback;
import com.alcpluralsight.aad_team20.reviews.ReviewResponse;
import com.alcpluralsight.aad_team20.trailers.OnGetTrailersCallback;
import com.alcpluralsight.aad_team20.trailers.TrailerResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRepository {

    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";

    private static final String TAG = "MoviesRepository";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;

    private TMDbApi api;
    private String apiKey = "703cde2368384bc23967944952515e96";

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError();
            }
           };
            switch (sortBy) {
                case TOP_RATED:
                    api.getTopRatedMovies(apiKey, LANGUAGE, page)
                            .enqueue(call);
                    break;
                case UPCOMING:
                    api.getUpcomingMovies(apiKey, LANGUAGE, page)
                            .enqueue(call);
                    break;
                case POPULAR:
                default:
                    api.getPopularMovies(apiKey, LANGUAGE, page)
                            .enqueue(call);
                    break;
            }
        }

            public void getGenres (final OnGetGenresCallback callback){
                api.getGenres(apiKey, LANGUAGE)
                    .enqueue(new Callback<GenresResponse>() {
                        @Override
                        public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {
                            if (response.isSuccessful()) {
                                GenresResponse genresResponse = response.body();
                                if (genresResponse != null && genresResponse.getGenres() != null) {
                                    callback.onSuccess(genresResponse.getGenres());
                                } else {
                                    callback.onError();
                                }
                            } else {
                                callback.onError();
                            }
                        }

                        @Override
                        public void onFailure(Call<GenresResponse> call, Throwable t) {
                            callback.onError();
                        }
                    });
        }
    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, apiKey, LANGUAGE)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful()) {
                            Movie movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, apiKey, LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
    public void getReviews(int movieId, final OnGetReviewsCallback callback) {
        api.getReviews(movieId, apiKey, LANGUAGE)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null && reviewResponse.getReviews() != null) {
                                callback.onSuccess(reviewResponse.getReviews());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
