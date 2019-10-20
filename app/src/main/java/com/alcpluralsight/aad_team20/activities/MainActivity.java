package com.alcpluralsight.aad_team20.activities;


import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
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
    private Toolbar toolbar;


    private static final String TAG = "MainActivity";
    private int resId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAnimation();

        initializations();
        setSupportActionBar(toolbar);

        getGenres();
        resId = R.anim.layout_animation;
        setupOnScrollListener();
    }

    private void initializations() {
        moviesRepository = MoviesRepository.getInstance();

        moviesList = findViewById(R.id.movies_list);
        moviesList.setLayoutManager(new LinearLayoutManager(this));

        toolbar = findViewById(R.id.toolbar);
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
                    adapter = new MoviesAdapter(movies, movieGenres, callback, MainActivity.this);
                    moviesList.setAdapter(adapter);
                    runAnimation();
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

    OnMoviesClickCallback callback = movie -> {
        Intent intent = new Intent(MainActivity.this, MovieActivity.class);
        intent.putExtra(MovieActivity.MOVIE_ID, movie.getId());
        ActivityOptions options =
                ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
        startActivity(intent, options.toBundle());
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

    private void runAnimation() {
        final LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(getApplicationContext(), resId);
        moviesList.setLayoutAnimation(animationController);
        moviesList.scheduleLayoutAnimation();
    }


    private void showError() {
        Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        //Inorder for ObjectAnimator to work the target needs to be cast to an ImageView - petekmunz.
        ImageView sortImage = (ImageView) menu.findItem(R.id.sort).getActionView();
        if (sortImage != null) {
            sortImage.setImageResource(R.drawable.ic_sort);
        }
        //Since the target of the animation is now an ImageView, we use an onclick on the view &..
        //..pass the target view as an argument to rotateMenu() -petekmunz.
        sortImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rotateMenu(view);
                showSortMenu();
            }
        });
        return true;
    }

    public void rotateMenu(View view) {
        Animator animator = AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.rotate);
        animator.setTarget(view);
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
                    runAnimation();
                    return true;
                case R.id.top_rated:
                    sortBy = MoviesRepository.TOP_RATED;
                    getMovies(currentPage);
                    runAnimation();
                    return true;
                case R.id.upcoming:
                    sortBy = MoviesRepository.UPCOMING;
                    getMovies(currentPage);
                    runAnimation();
                    return true;
                default:
                    return false;
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    private void setAnimation() {
        //No need for sdk check since our app min sdk is 21 which will guarantee animation to always run -petekmunz.
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.START);
        slide.setDuration(1300);
        slide.setInterpolator(new DecelerateInterpolator());
        getWindow().setEnterTransition(slide);
    }

    //Default onBack pressed conflicts with animation hence need for custom handling.
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
