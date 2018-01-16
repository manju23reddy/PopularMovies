package com.manju23reddy.popularmovies.UI;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.manju23reddy.popularmovies.Data.FavoriteDBContract;
import com.manju23reddy.popularmovies.Model.AppGlobalData;
import com.manju23reddy.popularmovies.Model.MovieModel;
import com.manju23reddy.popularmovies.Model.MovieReviewsAdapter;
import com.manju23reddy.popularmovies.Model.MovieTrailersAdapter;
import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.Util.PopularMovieConsts;
import com.manju23reddy.popularmovies.Util.PopularMovieNetworkUtil;
import com.squareup.picasso.Picasso;
import android.support.design.widget.Snackbar;
import android.support.design.widget.CoordinatorLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by MReddy3 on 12/5/2017.
 */

public class PopularMoviesDetailedView extends AppCompatActivity implements View.OnClickListener,
        android.support.v4.app.LoaderManager.LoaderCallbacks<JSONObject>{

    final String API_KEY = AppGlobalData.getAppGlobalInstance().getAPIKEY();

    final int MOVIE_DETAILED_LOADER_ID = 200;

    String mMovieId;

    MovieTrailersAdapter mTrailersAdapter;
    MovieReviewsAdapter mReviewsAdapter;

    @BindView(R.id.txtv_reviews_internet_error)
    TextView mReviewsDesc;
    @BindView(R.id.txtv_trailers_internet_error)
    TextView mTrailersDesc;

    @BindView(R.id.rcv_movie_trailers)
    RecyclerView mTrailersRCV;
    @BindView(R.id.rcv_movie_reviews)
    RecyclerView mReviewsRCV;
    @BindView(R.id.imgv_movie_poster)
    ImageView mMoviePosterImgView;
    @BindView(R.id.txtv_release_date)
    TextView movieReleaseDatesTxtView;
    @BindView(R.id.txtv_movie_rating)
    TextView movieRating;
    @BindView(R.id.txtv_movie_plot_details)
    TextView moviePlot;
    @BindView(R.id.imgbtn_mark_as_favorite)
    ImageButton setAsFavoriteImgBtn;

    boolean isMovieFavorite;

    Bundle inBundle = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.movie_detail_description_layout);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(null != savedInstanceState){
            inBundle = savedInstanceState;
        }
        else {
            Intent inComingIntent = getIntent();
            if (inComingIntent != null && inComingIntent.hasExtra(Intent.EXTRA_TEXT)) {
                inBundle = inComingIntent.getParcelableExtra(Intent.EXTRA_TEXT);

            }
        }

        initUIViews(inBundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PopularMovieConsts.FLOW, inBundle.getString(PopularMovieConsts.FLOW));
        outState.putString(PopularMovieConsts.SELECTED_MOVIE_DETAILS,
                inBundle.getString(PopularMovieConsts.SELECTED_MOVIE_DETAILS));
    }

    private void initUIViews(Bundle inData){

        String flow = inData.getString(PopularMovieConsts.FLOW);

        MovieModel inSelectedMovie = new MovieModel(inData.getString(PopularMovieConsts.
                SELECTED_MOVIE_DETAILS));

        isMovieFavorite = flow.equalsIgnoreCase(PopularMovieConsts.FAVORITE_MOVIES)? true : false;
        getSupportActionBar().setTitle(inSelectedMovie.getMovieTitle());

        mMovieId = Integer.toString(inSelectedMovie.getMovieId());


        if (isMovieFavorite){
            setAsFavoriteImgBtn.setImageResource(R.drawable.favorite);
        }
        setAsFavoriteImgBtn.setTag(inSelectedMovie);
        movieRating.setText(""+Double.toString(inSelectedMovie.getMovieRatings())+" / 10 ");
        movieReleaseDatesTxtView.setText(""+inSelectedMovie.getMovieReleaseDate());
        moviePlot.setText(inSelectedMovie.getMoviePlot());
        if (PopularMovieNetworkUtil.isInternetAvailable(this)) {
            String path = PopularMovieNetworkUtil.POSTER_URL + inSelectedMovie.getMoviePosterUrl();
            Picasso.with(this).load(path).into(mMoviePosterImgView);
        }

        setAsFavoriteImgBtn.setOnClickListener(this);

        //check for internet and get trailers and reviews
        if (PopularMovieNetworkUtil.isInternetAvailable(this)){
            getSupportLoaderManager().restartLoader(MOVIE_DETAILED_LOADER_ID, null,
                    PopularMoviesDetailedView.this);



            LinearLayoutManager trailerLayoutManager = new LinearLayoutManager
                    (this, LinearLayoutManager.HORIZONTAL, false);
            mTrailersRCV.setLayoutManager(trailerLayoutManager);

            LinearLayoutManager reviewLayoutManager = new LinearLayoutManager
                    (this, LinearLayoutManager.HORIZONTAL, false);
            mReviewsRCV.setLayoutManager(reviewLayoutManager);

            mTrailersAdapter = new MovieTrailersAdapter(this, mPlayTrailerCallBack);
            mReviewsAdapter = new MovieReviewsAdapter(this);

            mTrailersRCV.setHasFixedSize(true);
            mReviewsRCV.setHasFixedSize(true);
            mTrailersRCV.setAdapter(mTrailersAdapter);
            mReviewsRCV.setAdapter(mReviewsAdapter);
        }
        else {

            mReviewsDesc.setVisibility(View.VISIBLE);
            mTrailersDesc.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.imgbtn_mark_as_favorite:
                    //Todo implement code to insert the movie into DB
                    MovieModel curMovie = (MovieModel) v.getTag();
                    ImageButton favButton = (ImageButton) v;
                    Uri uri = null;
                    if (isMovieFavorite) {
                        favButton.setImageResource(R.drawable.notfavorite);
                        isMovieFavorite = false;

                        uri = FavoriteDBContract.FavoriteMovie.CONTENT_URI;
                        String id = Integer.toString(curMovie.getMovieId());
                        uri = uri.buildUpon().appendPath(id).build();

                        if (getContentResolver().delete(uri, null, null) <= 0) {
                            showSnackBar("Failed to remove movie from " +
                                    "favorite list ");
                        }else{
                            String result_text = curMovie.getMovieTitle()+
                                    " is removed from your favorite list.";
                            showSnackBar(result_text);
                        }

                    } else {
                        favButton.setImageResource(R.drawable.favorite);
                        isMovieFavorite = true;
                        ContentValues values = new ContentValues();
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_ID,
                                curMovie.getMovieId());
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_OVERVIEW,
                                curMovie.getMoviePlot());
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_POSTER_URL,
                                curMovie.getMoviePosterUrl());
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_TITLE,
                                curMovie.getMovieTitle());
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_RELEASE_DATA,
                                curMovie.getMovieReleaseDate());
                        values.put(FavoriteDBContract.FavoriteMovie.MOVIE_RATINGS,
                                curMovie.getMovieRatings());

                        uri = getContentResolver().
                                insert(FavoriteDBContract.FavoriteMovie.CONTENT_URI, values);
                        if (null != uri) {
                            String result_text = curMovie.getMovieTitle()+
                                    " is added to your favorite list.";
                            showSnackBar( result_text);
                        }
                    }
                    v.setTag(curMovie);
                    break;
            }
        }
        catch (Exception ee){
            ee.printStackTrace();
        }
    }


    @Override
    public android.support.v4.content.Loader<JSONObject> onCreateLoader(int id, Bundle args) {

        return  new android.support.v4.content.AsyncTaskLoader<JSONObject>(this) {

            JSONObject mMovieExtendedData = null;

            @Override
            protected void onStartLoading() {
                if (null != mMovieExtendedData){
                    deliverResult(mMovieExtendedData);
                }
                else{
                    forceLoad();
                }
            }

            @Override
            public JSONObject loadInBackground() {

                JSONObject resultData = null;
                try {

                    //first get Movie Trailers
                    URL trailersURL = PopularMovieNetworkUtil.buildQueryURL(
                            PopularMovieConsts.MOVIE_TRAILERS, mMovieId, API_KEY);


                    String trailersResult = PopularMovieNetworkUtil.
                            getResponseFromHttpUrl(trailersURL);

                    JSONObject trailersJsonObject = new JSONObject(trailersResult);


                    URL reviewsURL = PopularMovieNetworkUtil.buildQueryURL(
                            PopularMovieConsts.MOVIE_REVIEWS, mMovieId, API_KEY);

                    String reviewsResult = PopularMovieNetworkUtil.
                            getResponseFromHttpUrl(reviewsURL);

                    JSONObject reviewsJsonObject =  new JSONObject(reviewsResult);

                    resultData = new JSONObject();
                    resultData.put(PopularMovieConsts.MOVIE_TRAILERS, trailersJsonObject);
                    resultData.put(PopularMovieConsts.MOVIE_REVIEWS, reviewsJsonObject);

                    //Next get Movie Reviews
                }
                catch (Exception ee){
                    ee.printStackTrace();
                }

                return resultData;
            }

            @Override
            public void deliverResult(JSONObject data) {
                if (mMovieExtendedData != null){
                    mMovieExtendedData = null;
                }
                mMovieExtendedData = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<JSONObject> loader,
                               JSONObject data) {
        try {
            if (null != data) {
                JSONObject trailers = data.getJSONObject(PopularMovieConsts.MOVIE_TRAILERS);
                JSONObject reviews = data.getJSONObject(PopularMovieConsts.MOVIE_REVIEWS);

                JSONArray trailerResult = trailers.getJSONArray(PopularMovieConsts.RESULT);
                JSONArray reviewsResult = reviews.getJSONArray(PopularMovieConsts.RESULT);

                int numTrailers = trailerResult.length();
                if(numTrailers <= 0){
                    mTrailersDesc.setText(getResources().getString(R.string.no_trailers));
                    mTrailersDesc.setVisibility(View.VISIBLE);
                }
                else {
                    for (int i = 0; i < numTrailers ; i++) {
                        mTrailersAdapter.addTrailer(trailerResult.getJSONObject(i));
                    }
                }

                int numReviews = reviewsResult.length();
                if (numReviews <= 0){
                    mReviewsDesc.setText(getResources().getString(R.string.no_reviews));
                    mReviewsDesc.setVisibility(View.VISIBLE);
                }
                else {
                    for (int i = 0; i < numReviews; i++) {
                        mReviewsAdapter.addReviews(reviewsResult.getJSONObject(i));
                    }
                }
            }
        }
        catch (Exception ee){
            ee.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<JSONObject> loader) {
        if (null != mReviewsAdapter){
            mReviewsAdapter.resetAdapater();
        }
        if (null != mTrailersAdapter){
            mTrailersAdapter.resetAdapter();
        }
    }

    MovieTrailersAdapter.PlayTrailerCallBack mPlayTrailerCallBack =
            new MovieTrailersAdapter.PlayTrailerCallBack() {
        @Override
        public void onPlayButtonClicked(int pos) {
            try {
                JSONObject trailer = mTrailersAdapter.getTrailerObject(pos);
                String key = trailer.getString(PopularMovieConsts.TRAILER_KEY);
                Intent launchTrailer = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("vnd.youtube:"+key));
                startActivity(launchTrailer);
            }
            catch (Exception ee){
                ee.printStackTrace();
            }

        }
    };

    private void showSnackBar(String content){
        CoordinatorLayout layout = findViewById(R.id.coordinate_layout);
        Snackbar snackBar = Snackbar.make(layout, content, Snackbar.LENGTH_LONG);
        snackBar.show();
    }

}
