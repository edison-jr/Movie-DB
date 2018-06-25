package com.edison.android.apps.moviedb;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.ImageLoader;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movie;
import com.edison.android.apps.moviedb.tmdb.domain.movie.Movies;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Movies mMovies;
    private ImageLoader mImgLoader;
    private OnItemClickListener mListener;

    public MovieAdapter(@NonNull Movies movies, @NonNull ImageLoader imgLoader, @NonNull OnItemClickListener listener) {
        mMovies = movies;
        mImgLoader = imgLoader;
        mListener = listener;
    }

    public Movies getMovies() {
        return mMovies;
    }

    public void setMovies(Movies movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieHolder(inflater.inflate(R.layout.movie_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Picasso.get().load(mImgLoader.image(movie.posterPath())).into(holder.poster);
        holder.originalTitle.setText(movie.originalTitle());
        holder.setFavorite(movie.favorite());
    }

    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView poster;
        final TextView originalTitle;
        final ImageView favorite;
        final Drawable favoriteIcon;
        final Drawable favoriteBorderIcon;

        MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            favoriteIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.favorite_white_24);
            favoriteBorderIcon = ContextCompat.getDrawable(itemView.getContext(), R.drawable.favorite_border_white_24);
            poster = itemView.findViewById(R.id.iv_poster);
            originalTitle = itemView.findViewById(R.id.tv_original_title);
            favorite = itemView.findViewById(R.id.favorite);
            favorite.setOnClickListener(this);
        }

        void setFavorite(boolean favorite) {
            this.favorite.setImageDrawable(favorite ? favoriteIcon : favoriteBorderIcon);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (v.getId() == R.id.favorite) {
                mListener.onFavoriteItemClick(MovieAdapter.this, position);
            } else {
                mListener.onItemClick(MovieAdapter.this, position);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(MovieAdapter adapter, int position);
        void onFavoriteItemClick(MovieAdapter adapter, int position);
    }

}
