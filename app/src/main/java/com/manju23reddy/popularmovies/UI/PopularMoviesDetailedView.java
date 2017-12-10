package com.manju23reddy.popularmovies.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.manju23reddy.popularmovies.Model.MovieModel;
import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.Util.PopularMovieNetworkUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by MReddy3 on 12/5/2017.
 */

public class PopularMoviesDetailedView extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail_description_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent inComingIntent = getIntent();
        if (inComingIntent != null && inComingIntent.hasExtra(Intent.EXTRA_TEXT)){
            initUIViews((MovieModel)inComingIntent.getParcelableExtra(Intent.EXTRA_TEXT));
        }
    }
    private void initUIViews(MovieModel inSelectedMovie){

        ImageView moviePosterImgView = findViewById(R.id.imgv_movie_poster);
        TextView movieTitleTxtView = findViewById(R.id.txtv_movie_name);
        TextView movieReleaseDatesTxtView = findViewById(R.id.txtv_release_date);
        TextView movieRating = findViewById(R.id.txtv_movie_rating);
        TextView moviePlot = findViewById(R.id.txtv_movie_plot_details);

        movieTitleTxtView.setText("Title : "+inSelectedMovie.getMovieTitle());
        movieRating.setText("Ratings : "+Double.toString(inSelectedMovie.getMovieRatings()));
        movieReleaseDatesTxtView.setText("Release Date : "+inSelectedMovie.getMovieReleaseDate());
        moviePlot.setText(inSelectedMovie.getMoviePlot());
        String path = PopularMovieNetworkUtil.POSTER_URL+inSelectedMovie.getMoviePosterUrl();
        Picasso.with(this).load(path).into(moviePosterImgView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
