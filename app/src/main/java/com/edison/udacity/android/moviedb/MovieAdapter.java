package com.edison.udacity.android.moviedb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.udacity.android.moviedb.model.Movie;
import com.edison.udacity.android.moviedb.model.Movies;
import com.edison.udacity.android.moviedb.model.Poster;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private Movies mMovies;
    private Poster mPoster;
    private OnItemClickListener mListener;

    public MovieAdapter(@NonNull Movies movies, @NonNull Poster poster, @NonNull OnItemClickListener listener) {
        mMovies = movies;
        mPoster = poster;
        mListener = listener;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new MovieHolder(inflater.inflate(R.layout.movie_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = mMovies.get(position);
        Picasso.get().load(mPoster.poster(movie.posterPath())).into(holder.poster);
        holder.originalTitle.setText(movie.originalTitle());
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

        MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            poster = itemView.findViewById(R.id.iv_poster);
            originalTitle = itemView.findViewById(R.id.tv_original_title);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.onItemClick(MovieAdapter.this, position);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(MovieAdapter adapter, int position);
    }

}
