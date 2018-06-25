package com.edison.android.apps.moviedb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.domain.review.Review;
import com.edison.android.apps.moviedb.tmdb.domain.review.Reviews;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.VIdeoHolder> {

    private Reviews mReviews;
    private OnItemClickListener mListener;

    public ReviewAdapter(@NonNull Reviews reviews, @NonNull OnItemClickListener listener) {
        mReviews = reviews;
        mListener = listener;
    }

    @NonNull
    @Override
    public VIdeoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VIdeoHolder(inflater.inflate(R.layout.review_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VIdeoHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.content.setText(review.content());
        holder.author.setText(review.author());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    class VIdeoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView content;
        final TextView author;

        VIdeoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            content = itemView.findViewById(R.id.tv_content);
            author = itemView.findViewById(R.id.tv_author);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Review review = mReviews.get(position);
            mListener.onItemClick(ReviewAdapter.this, position, review);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(ReviewAdapter adapter, int position, Review review);
    }

}
