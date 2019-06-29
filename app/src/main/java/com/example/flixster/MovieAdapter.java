package com.example.flixster;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flixster.models.Config;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    @NonNull
    @Override //creates and inflates a new view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);

        return new ViewHolder(movieView);
    }

    @Override //binds an inflated view to a new item
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        String imageUrl = null;

        if(isPortrait){
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        }
        else{
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
        }

        int placeholderid = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;

        //load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeholderid)
                .error(placeholderid)
                .into(imageView);

    }

    @Override //returns the total number of items in the list
    public int getItemCount() {
        return movies.size();
    }

    ArrayList<Movie> movies;
    Config config;
    Context context;

    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Nullable
        @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @Nullable
        @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }
        }
    }
}
