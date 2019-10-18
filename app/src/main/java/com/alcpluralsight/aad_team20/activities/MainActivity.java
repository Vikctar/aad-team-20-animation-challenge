package com.alcpluralsight.aad_team20.activities;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alcpluralsight.aad_team20.R;
import com.alcpluralsight.aad_team20.adapters.MoviesAdapter;
import com.alcpluralsight.aad_team20.genres.Genre;
import com.alcpluralsight.aad_team20.genres.OnGetGenresCallback;
import com.alcpluralsight.aad_team20.interfaces.OnMoviesClickCallback;
import com.alcpluralsight.aad_team20.models.Movie;
import com.alcpluralsight.aad_team20.models.MoviesRepository;
import com.alcpluralsight.aad_team20.models.OnGetMoviesCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView moviesList;
    private MoviesAdapter adapter;
    private MoviesRepository moviesRepository;

    private List<Genre> movieGenres;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private String sortBy = MoviesRepository.POPULAR;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moviesRepository = MoviesRepository.getInstance();

        moviesList = findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getGenres();
        int resId = R.anim.layout_animation;
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(this, resId);
        moviesList.setLayoutAnimation(animationController);
        moviesList.scheduleLayoutAnimation();

        setupOnScrollListener();
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        moviesList.setLayoutManager(manager);
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getGenres() {
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<Genre> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getMovies(int page) {
        isFetchingMovies = true;

        moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, List<Movie> movies) {
                if (adapter == null) {
                    adapter = new MoviesAdapter(movies, movieGenres, callback);
                    moviesList.setAdapter(adapter);
                } else {
                    if (page == 1) {
                        adapter.clearMovies();
                    }
                    adapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                setTitle();
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(Movie movie) {
            Intent intent = new Intent(MainActivity.this, MovieActivity.class);
            intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
            if (Build.VERSION.SDK_INT > 20) {
                ActivityOptions options =
                        ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(intent, options.toBundle());
            } else {
                startActivity(intent);
            }

        }
    };

        private void setTitle() {
            switch (sortBy) {
                case MoviesRepository.POPULAR:
                    setTitle(getString(R.string.popular));
                    break;
                case MoviesRepository.TOP_RATED:
                    setTitle(getString(R.string.top_rated));
                    break;
                case MoviesRepository.UPCOMING:
                    setTitle(getString(R.string.upcoming));
                    break;
            }
        }

        private void showError() {
            Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.sort:
                    showSortMenu();
                    rotateMenu();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public void rotateMenu() {
            Animator animator = AnimatorInflater.loadAnimator(this, R.animator.rotate);
            animator.setTarget(R.drawable.ic_sort);
            animator.start();

        }


        private void showSortMenu() {
            PopupMenu sortMenu = new PopupMenu(this, findViewById(R.id.sort));
            sortMenu.setOnMenuItemClickListener(item -> {
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            });
            sortMenu.inflate(R.menu.menu_movies_sort);
            sortMenu.show();
        }
}
