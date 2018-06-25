package com.edison.android.apps.moviedb;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edison.android.apps.moviedb.tmdb.ImageLoader;
import com.edison.android.apps.moviedb.tmdb.domain.video.Video;
import com.edison.android.apps.moviedb.tmdb.domain.video.Videos;
import com.squareup.picasso.Picasso;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VIdeoHolder> {

    private Videos mVideos;
    private ImageLoader mImgLoader;
    private OnItemClickListener mListener;

    public VideoAdapter(@NonNull Videos videos, @NonNull ImageLoader imgLoader, @NonNull OnItemClickListener listener) {
        mVideos = videos;
        mImgLoader = imgLoader;
        mListener = listener;
    }

    @NonNull
    @Override
    public VIdeoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new VIdeoHolder(inflater.inflate(R.layout.video_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VIdeoHolder holder, int position) {
        Video video = mVideos.get(position);
        Picasso.get().load(mImgLoader.image(video.key())).into(holder.thumbnail);
        holder.nome.setText(video.name());
    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    class VIdeoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView thumbnail;
        final TextView nome;

        VIdeoHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thumbnail = itemView.findViewById(R.id.iv_thumbnail);
            nome = itemView.findViewById(R.id.tv_nome);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Video video = mVideos.get(position);
            mListener.onItemClick(VideoAdapter.this, position, video);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(VideoAdapter adapter, int position, Video video);
    }

}
