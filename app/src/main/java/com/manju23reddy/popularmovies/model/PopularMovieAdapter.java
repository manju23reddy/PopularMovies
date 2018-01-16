package com.manju23reddy.popularmovies.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.util.PopularMovieNetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MReddy3 on 12/5/2017.
 */

public class PopularMovieAdapter extends RecyclerView.Adapter<PopularMovieAdapter.PopularMovieHolder>{

    private ArrayList<MovieModel> mPopularMoviesList;

    public interface  MovieThumbnailClickListener{
        void onMovieThumbnailClicked(int position);
    }

    private MovieThumbnailClickListener mThumbnailClickListener;
    private Context mContext;

    public class PopularMovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView mMovieThumbnail;

        public PopularMovieHolder(View itemView) {
            super(itemView);
            mMovieThumbnail = itemView.findViewById(R.id.imgv_movie_thumbnail);
            mMovieThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.imgv_movie_thumbnail){
                mThumbnailClickListener.onMovieThumbnailClicked(getAdapterPosition());
            }
        }
    }

    public PopularMovieAdapter(Context context, MovieThumbnailClickListener listener){
        mPopularMoviesList = new ArrayList<>();
        this.mThumbnailClickListener = listener;
        mContext = context;

    }

    @Override
    public PopularMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_thumb_nail;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new PopularMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(PopularMovieHolder holder, int position) {
        MovieModel curItem = mPopularMoviesList.get(position);
        String path = PopularMovieNetworkUtil.POSTER_URL+curItem.getMoviePosterUrl();

        Picasso.with(mContext)
                .load(path)
                .into(holder.mMovieThumbnail);
    }

    @Override
    public int getItemCount() {
        return mPopularMoviesList.size();
    }

    public void addMovie(MovieModel movie){
        mPopularMoviesList.add(movie);
        notifyDataSetChanged();
    }

    public MovieModel getMovieAtPosition(int pos){
        return mPopularMoviesList.get(pos);
    }

    public void clearAll(){
        mPopularMoviesList.clear();
        notifyDataSetChanged();
    }

    public ArrayList<MovieModel>getAllMovies(){
        return mPopularMoviesList;
    }

    public void setMovies(ArrayList<MovieModel> allMovies){
        for(MovieModel curMovie : allMovies){
            mPopularMoviesList.add(curMovie);
        }
    }
}
