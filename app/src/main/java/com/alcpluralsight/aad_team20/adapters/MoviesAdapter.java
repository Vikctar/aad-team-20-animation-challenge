package com.alcpluralsight.aad_team20.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alcpluralsight.aad_team20.R;
import com.alcpluralsight.aad_team20.genres.Genre;
import com.alcpluralsight.aad_team20.interfaces.OnMoviesClickCallback;
import com.alcpluralsight.aad_team20.models.Movie;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185";

    private List<Movie> movies;
    private List<Genre> allGenres;
    private OnMoviesClickCallback callback;
    private Context context;
    private int lastAnimatedPosition = -1;

    public MoviesAdapter(List<Movie> movies, List<Genre> allGenres, OnMoviesClickCallback callback, Context context) {
        this.movies = movies;
        this.allGenres = allGenres;
        this.callback = callback;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(movies.get(position));
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down);
            holder.container.setAnimation(animation);
            animation.start();
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MovieViewHolder holder) {
        holder.clearAnimation();
        super.onViewDetachedFromWindow(holder);
    }

    public void appendMovies(List<Movie> moviesToAppend) {
        movies.addAll(moviesToAppend);
        notifyDataSetChanged();
    }
    public void clearMovies() {
        movies.clear();
        notifyDataSetChanged();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        Movie movie;
        View container;

        public MovieViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);

            itemView.setOnClickListener(v -> callback.onClick(movie));
        }

        public void bind(Movie movie) {
            this.movie = movie;
            releaseDate.setText(movie.getReleaseDate().split("-")[0]);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getRating()));
            genres.setText(getGenres(movie.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movie.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }
        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (Genre genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }

        public void clearAnimation(){
            container.clearAnimation();
        }

    }
}