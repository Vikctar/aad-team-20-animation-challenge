package com.alcpluralsight.aad_team20.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alcpluralsight.aad_team20.R;
import com.alcpluralsight.aad_team20.genres.Genre;
import com.alcpluralsight.aad_team20.genres.OnGetGenresCallback;
import com.alcpluralsight.aad_team20.interfaces.OnGetMovieCallback;
import com.alcpluralsight.aad_team20.models.Movie;
import com.alcpluralsight.aad_team20.models.MoviesRepository;
import com.alcpluralsight.aad_team20.reviews.OnGetReviewsCallback;
import com.alcpluralsight.aad_team20.reviews.Review;
import com.alcpluralsight.aad_team20.trailers.OnGetTrailersCallback;
import com.alcpluralsight.aad_team20.trailers.Trailer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that handles all movie related logic.
 */
public class MovieActivity extends AppCompatActivity {

    public static String MOVIE_ID = "movie_id";

    private static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView movieBackdrop;
    private TextView movieTitle;
    private TextView movieGenres;
    private TextView movieOverview;
    private TextView movieOverviewLabel;
    private TextView movieReleaseDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailers;
    private LinearLayout movieReviews;

    private TextView reviewsLabel;
    private TextView trailersLabel;

    private MoviesRepository moviesRepository;
    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        movieId = getIntent().getIntExtra(MOVIE_ID, movieId);

        moviesRepository = MoviesRepository.getInstance();

        setupToolbar();

        initUI();

        getMovie();
        setAnimation();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        movieBackdrop = findViewById(R.id.movieDetailsBackdrop);
        movieTitle = findViewById(R.id.movieDetailsTitle);
        movieGenres = findViewById(R.id.movieDetailsGenres);
        movieOverview = findViewById(R.id.movieDetailsOverview);
        movieOverviewLabel = findViewById(R.id.summaryLabel);
        movieReleaseDate = findViewById(R.id.movieDetailsReleaseDate);
        movieRating = findViewById(R.id.movieDetailsRating);
        movieTrailers = findViewById(R.id.movieTrailers);
        movieReviews = findViewById(R.id.movieReviews);
        reviewsLabel = findViewById(R.id.reviewsLabel);
        trailersLabel = findViewById(R.id.trailersLabel);
    }

    /**
     * Callback that gets a movie based on the movieId of type [int].
     * On success, it sets the title, overview, rating, releaseDate.
     * On error it finishes the activity
     */
    private void getMovie() {
        moviesRepository.getMovie(movieId, new OnGetMovieCallback() {
            @Override
            public void onSuccess(Movie movie) {
                movieTitle.setText(movie.getTitle());
                movieOverviewLabel.setVisibility(View.VISIBLE);
                movieOverview.setText(movie.getOverview());
                movieRating.setVisibility(View.VISIBLE);
                movieRating.setRating(movie.getRating() / 2);
                getGenres(movie);
                movieReleaseDate.setText(movie.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(MovieActivity.this)
                            .load(IMAGE_BASE_URL + movie.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(movieBackdrop);
                }
                getTrailers(movie);
                getReviews(movie);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }

    /**
     * Callback that gets movie trailers based on the movie of type [Movie].
     * On success, it sets the youtube thumbnail and plays the trailer youtube URL
     * @param movie of type [Movie]
     * On error it hides the trailer label on the UI
     */
    private void getTrailers(Movie movie) {
        moviesRepository.getTrailers(movie.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                movieTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, movieTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(v -> showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey())));
                    Glide.with(MovieActivity.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.drawable.poster_placeholder)
                                    .error(R.drawable.error_state)
                                    .centerCrop())
                            .into(thumbnail);
                    movieTrailers.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Shows a trailer by starting up a new ACTION_VIEW Intent.
     * @param url of type [String]
     */
    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /**
     * Callback that gets movie reviews from moviesRepository based on the movie of type [Movie].
     * On success, this method loops through all the reviews and sets an author and content and
     * then adds them to the parent view.
     * @param movie of type [Movie]
     * On error it does nothing.
     */
    private void getReviews(Movie movie) {
        moviesRepository.getReviews(movie.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsLabel.setVisibility(View.VISIBLE);
                movieReviews.removeAllViews();
                for (Review review : reviews) {
                    View parent = getLayoutInflater().inflate(R.layout.review, movieReviews, false);
                    TextView author = parent.findViewById(R.id.reviewAuthor);
                    TextView content = parent.findViewById(R.id.reviewContent);
                    author.setText(review.getAuthor());
                    content.setText(review.getContent());
                    movieReviews.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
            }
        });
    }

    /**
     * Callback that gets genres reviews from moviesRepository based on the movie of type [Movie].
     * On success, this method loops through all the genres and sets a list of movieGenres of type [List<Genre>]
     * @param movie of type [Movie]
     * On error it displays an error.
     */
    private void getGenres(final Movie movie) {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                if (movie.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (Genre genre : movie.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    movieGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Displays an error toast.
     */
    private void showError() {
        Toast.makeText(MovieActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
    //set your animation
    public void setAnimation() {
        //No need for sdk check since our app min sdk is 21 which will guarantee animation to always run -petekmunz.
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        //Changed duration to 700ms to give time for image to be loaded hence better ux -petekmunz.
        slide.setDuration(700);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setExitTransition(slide);
        getWindow().setEnterTransition(slide);
    }
}