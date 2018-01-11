package com.manju23reddy.popularmovies.Model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.Util.PopularMovieConsts;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by MReddy3 on 1/6/2018.
 */

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.MovieReviewHolder>{

    private ArrayList<JSONObject> mMovieReviews = null;

    Context mContext;

    public class MovieReviewHolder extends RecyclerView.ViewHolder{
        private TextView reviewerName;
        private TextView reviewContent;

        public MovieReviewHolder(View view){
            super(view);

            reviewerName = view.findViewById(R.id.txtv_reviewer_name);
            reviewContent = view.findViewById(R.id.txtv_review_content);
        }


    }

    public MovieReviewsAdapter(Context context){
        mContext = context;
        mMovieReviews = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(MovieReviewHolder holder, int position) {
        JSONObject movieReviews = mMovieReviews.get(position);

        try {
            holder.reviewerName.setText(movieReviews.getString(PopularMovieConsts.REVIEW_AUTHOR));
            holder.reviewContent.setText(movieReviews.getString(PopularMovieConsts.REVIEW_CONTENT));
        }
        catch (Exception ee){
            ee.printStackTrace();
        }



    }

    @Override
    public MovieReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int thumbnailLayout = R.layout.movie_reviews_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatly = false;

        View view = inflater.inflate(thumbnailLayout, parent, shouldAttachToParentImmediatly);
        return new MovieReviewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (null != mMovieReviews){
            return mMovieReviews.size();
        }
        return 0;
    }

    public void addReviews(JSONObject review){
        mMovieReviews.add(review);
        notifyDataSetChanged();
    }

    public void resetAdapater(){
        mMovieReviews.clear();
    }

}
