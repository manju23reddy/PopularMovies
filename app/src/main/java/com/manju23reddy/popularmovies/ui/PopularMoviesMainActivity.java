package com.manju23reddy.popularmovies.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.manju23reddy.popularmovies.Data.FavoriteDBContract;
import com.manju23reddy.popularmovies.model.AppGlobalData;
import com.manju23reddy.popularmovies.model.MovieModel;
import com.manju23reddy.popularmovies.model.PopularMovieAdapter;
import com.manju23reddy.popularmovies.util.PopularMovieConsts;
import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.util.PopularMovieNetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PopularMoviesMainActivity extends AppCompatActivity implements
        PopularMovieAdapter.MovieThumbnailClickListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<JSONArray> {


    public static PopularMovieAdapter mMoviesAdapter = null;
    public final int POPULAR_MOVIE_MAIN_LOADER_ID = 100;
    private final int NUMBER_OF_COLUMNS = 4;
    @BindView(R.id.pbr_download_indicator)
    ProgressBar mDownloaderProgressBar = null;
    String API_KEY = AppGlobalData.getAppGlobalInstance().getAPIKEY();
    @BindView(R.id.rcv_movies_list)
    RecyclerView mThumbnailsRecyclerView;

    @BindView(R.id.rbtn_favorites)
    RadioButton mFilterByFavoritesRdBtn = null;

    @BindView(R.id.rbtn_most_popular)
    RadioButton mFilterByPopularRDBtn = null;

    @BindView(R.id.rbtn_top_rated)
    RadioButton mFilterByRatingRDBtn = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_main);

        initUI();

        if (null != savedInstanceState) {
            if(savedInstanceState.containsKey(PopularMovieConsts.FLOW)){
                String previous_flow = savedInstanceState.getString(PopularMovieConsts.FLOW);
                if (previous_flow.equalsIgnoreCase(PopularMovieConsts.FAVORITE_MOVIES)){
                    mFilterByPopularRDBtn.setChecked(true);
                }
                else if(previous_flow.equalsIgnoreCase(PopularMovieConsts.TOP_RATED_MOVIES_FLOW)){
                    mFilterByRatingRDBtn.setChecked(true);
                }
                else {
                    mFilterByPopularRDBtn.setChecked(true);
                }
            }

        }
        else{
            loadMovies(PopularMovieConsts.POPULAR_LIST);
        }



    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (null != savedInstanceState){

            if (savedInstanceState.containsKey(PopularMovieConsts.CONFIR_PERSIST)){
                ArrayList<MovieModel> persistData = savedInstanceState.
                        getParcelableArrayList(PopularMovieConsts.CONFIR_PERSIST);
                mMoviesAdapter.setMovies(persistData);
                mDownloaderProgressBar.setVisibility(View.GONE);
            }
            else{
                loadMovies(savedInstanceState.getString(PopularMovieConsts.FLOW));
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final int scrollPos =  savedInstanceState.getInt(PopularMovieConsts.
                            RECYCLER_LAYOUT_STATE);
                    GridLayoutManager laytManager = (GridLayoutManager)mThumbnailsRecyclerView.
                            getLayoutManager();
                    View v = mThumbnailsRecyclerView.getChildAt(scrollPos);

                    int top = (v == null)? 0 : (v.getTop() -
                            mThumbnailsRecyclerView.getPaddingTop());

                    laytManager.scrollToPositionWithOffset(scrollPos, top);
                }
            }, 1000);




        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getCurrentFlow(){
        if (mFilterByFavoritesRdBtn.isChecked()){
            return PopularMovieConsts.FAVORITE_MOVIES_FLOW;
        }
        else if (mFilterByPopularRDBtn.isChecked()){
            return PopularMovieConsts.POPULAR_LIST_FLOW;
        }
        else{
            return PopularMovieConsts.TOP_RATED_MOVIES_FLOW;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(PopularMovieConsts.FLOW,  getCurrentFlow());

        GridLayoutManager laytManager = (GridLayoutManager)mThumbnailsRecyclerView.
                getLayoutManager();

        int index = laytManager.findFirstCompletelyVisibleItemPosition();


        outState.putInt(PopularMovieConsts.RECYCLER_LAYOUT_STATE,
                index);

        outState.putParcelableArrayList(PopularMovieConsts.CONFIR_PERSIST,
                mMoviesAdapter.getAllMovies());

    }

    /**
     * inti the UI components and set initial states for UI.
     */
    private void initUI(){

        ButterKnife.bind(this);

        GridLayoutManager thumbNailLyt = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mThumbnailsRecyclerView.setHasFixedSize(true);
        mThumbnailsRecyclerView.setLayoutManager(thumbNailLyt);

        mMoviesAdapter = new PopularMovieAdapter(this, this);
        mThumbnailsRecyclerView.setAdapter(mMoviesAdapter);


        mFilterByPopularRDBtn.setOnClickListener(this);
        mFilterByRatingRDBtn.setOnClickListener(this);
        mFilterByFavoritesRdBtn.setOnClickListener(this);

        mDownloaderProgressBar.setOnClickListener(this);

    }

    /**
     * Show the progress bar and load the movies based on query
     * @param filter
     */
    private void loadMovies(String filter){
        Bundle args = null;
        if (filter.equalsIgnoreCase(PopularMovieConsts.POPULAR_LIST) ||
                filter.equalsIgnoreCase(PopularMovieConsts.TOP_RATED_LIST)) {
            mMoviesAdapter.clearAll();
            if (PopularMovieNetworkUtil.isInternetAvailable(this)) {
                mDownloaderProgressBar.setVisibility(View.VISIBLE);

                args = new Bundle();
                args.putString(PopularMovieConsts.FLOW, filter);



            } else {
                Toast.makeText(this, getText(R.string.no_internet_error).toString(),
                        Toast.LENGTH_LONG).show();
            }
        }
        else if(filter.equalsIgnoreCase(PopularMovieConsts.FAVORITE_MOVIES)){
            mMoviesAdapter.clearAll();
            args = new Bundle();
            args.putString(PopularMovieConsts.FLOW, PopularMovieConsts.FAVORITE_MOVIES);

        }
        else{
            //do nothing
            return;
        }
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> moviesLoader = loaderManager.getLoader(POPULAR_MOVIE_MAIN_LOADER_ID);
        if (null == moviesLoader) {
            loaderManager.initLoader(POPULAR_MOVIE_MAIN_LOADER_ID, args, this);
        }
        else {
            loaderManager.restartLoader(POPULAR_MOVIE_MAIN_LOADER_ID, args, this);
        }

    }

    private void downloadComplete(){
        mDownloaderProgressBar.setVisibility(View.GONE);
    }

    @Override
    /**
     * Call back when Thumbnail is clicked. Upon click launch the detailed activity with
     * extra text as movie details.
     */
    public void onMovieThumbnailClicked(int position) {
        MovieModel selectedThumbnail = mMoviesAdapter.getMovieAtPosition(position);
        Intent movieDetailsScreen = new Intent(this, PopularMoviesDetailedView.class);
        Bundle data = new Bundle();
        data.putString(PopularMovieConsts.FLOW, getCurrentFlow());
        data.putString(PopularMovieConsts.SELECTED_MOVIE_DETAILS, selectedThumbnail.toString());
        movieDetailsScreen.putExtra(Intent.EXTRA_TEXT, data);

        startActivity(movieDetailsScreen);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rbtn_most_popular:
                loadMovies(PopularMovieConsts.POPULAR_LIST);

                break;
            case R.id.rbtn_top_rated:
                loadMovies(PopularMovieConsts.TOP_RATED_LIST);
                break;
            case R.id.rbtn_favorites:
                //since movies are still loading click shall not be allowed to decedents.
                loadMovies(PopularMovieConsts.FAVORITE_MOVIES);
                break;
        }
    }

    public void clearAdapter(){
        mMoviesAdapter.clearAll();
    }

    public void addItemIntoAdapter(MovieModel item){
        mMoviesAdapter.addMovie(item);
    }

    @Override
    public Loader<JSONArray> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<JSONArray>(this) {
            JSONArray[] mCachedData = null;

            public int getCurrentFlow(String flow){
                if (PopularMovieConsts.POPULAR_LIST_FLOW.equalsIgnoreCase(flow)){
                    return PopularMovieConsts.FILTER_POPULAR_LIST;
                }
                else if(PopularMovieConsts.TOP_RATED_MOVIES_FLOW.equalsIgnoreCase(flow)){
                    return PopularMovieConsts.FILTER_TOP_RATED;
                }
                else if (PopularMovieConsts.FAVORITE_MOVIES_FLOW.equalsIgnoreCase(flow)){
                    return PopularMovieConsts.FILTER_FAVORITES;
                }
                else{
                    throw new UnsupportedOperationException("Not a valid flow");
                }
            }

            @Override
            protected void onStartLoading() {

                if (null == mCachedData){
                    mCachedData = new JSONArray[PopularMovieConsts.NUMBER_OF_FILTERS];
                    forceLoad();
                }
                else{
                    int flow = getCurrentFlow(args.getString(PopularMovieConsts.FLOW));
                    if (mCachedData[flow].length() > 0){
                        deliverResult(mCachedData[flow]);
                    }
                    else{
                        forceLoad();
                    }
                }


            }

            @Override
            public JSONArray loadInBackground() {
                JSONArray returnResult = null;

                try{
                    String flow = args.getString(PopularMovieConsts.FLOW);
                    switch (flow){
                        case PopularMovieConsts.FAVORITE_MOVIES_FLOW:

                            Cursor dbResult = getContentResolver().query(FavoriteDBContract.FavoriteMovie.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);
                            if(dbResult != null && dbResult.getCount() > 0){
                                returnResult = new JSONArray();
                                while(dbResult.moveToNext()){
                                    MovieModel curMovie = new MovieModel(
                                            dbResult.getInt(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_ID)),
                                            dbResult.getString(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_POSTER_URL)),
                                            dbResult.getString(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_TITLE)),
                                            dbResult.getString(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_RELEASE_DATA)),
                                            dbResult.getDouble(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_RATINGS)),
                                            dbResult.getString(dbResult.getColumnIndex
                                                    (FavoriteDBContract.FavoriteMovie.MOVIE_OVERVIEW)));
                                    returnResult.put(new JSONObject(curMovie.toString()));
                                }
                            }
                            break;
                        case PopularMovieConsts.POPULAR_LIST_FLOW:
                        case PopularMovieConsts.TOP_RATED_MOVIES_FLOW:
                            URL movieRequestURL = PopularMovieNetworkUtil.buildQueryURL(
                                    flow == PopularMovieConsts.POPULAR_LIST_FLOW ?
                                            PopularMovieConsts.POPULAR_LIST :
                                            PopularMovieConsts.TOP_RATED_LIST, API_KEY);

                            String moviesQueryResult =
                                    PopularMovieNetworkUtil.getResponseFromHttpUrl(movieRequestURL);
                            JSONObject result = new JSONObject(moviesQueryResult);

                            returnResult =
                                    result.getJSONArray(PopularMovieConsts.MOVIE_RESULT);


                            break;
                        default:
                            break;

                    }
                }
                catch (Exception ee){
                    ee.printStackTrace();
                }

                return returnResult;
            }

            @Override
            public void deliverResult(JSONArray data) {
                int flow = getCurrentFlow(args.getString(PopularMovieConsts.FLOW));
                mCachedData[flow] = data;
                super.deliverResult(mCachedData[flow]);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        if (null != data){
            for (int i = 0; i < data.length(); i++){
                try {
                    MovieModel curMovie = new MovieModel(data.getJSONObject(i).toString());
                    addItemIntoAdapter(curMovie);
                }
                catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }
        downloadComplete();
    }


    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {
        clearAdapter();
    }
}
