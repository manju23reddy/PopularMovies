package com.manju23reddy.popularmovies.UI;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.manju23reddy.popularmovies.Model.AppGlobalData;
import com.manju23reddy.popularmovies.Model.MovieModel;
import com.manju23reddy.popularmovies.Model.PopularMovieAdapter;
import com.manju23reddy.popularmovies.Util.PopularMovieConsts;
import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.Util.PopularMovieNetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

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

        String previous_flow = PopularMovieConsts.POPULAR_LIST;
        if (null != savedInstanceState) {
            if(savedInstanceState.containsKey(PopularMovieConsts.FLOW)){
                previous_flow = savedInstanceState.getString(PopularMovieConsts.FLOW);
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
            previous_flow = PopularMovieConsts.POPULAR_LIST;
        }

        loadMovies(previous_flow);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (null != savedInstanceState){
            final int scrollPos =  savedInstanceState.getInt(PopularMovieConsts.
                    RECYCLER_LAYOUT_STATE);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GridLayoutManager laytManager = (GridLayoutManager)mThumbnailsRecyclerView.
                            getLayoutManager();
                    View v = mThumbnailsRecyclerView.getChildAt(scrollPos);

                    int top = (v == null)? 0 : (v.getTop() -
                            mThumbnailsRecyclerView.getPaddingTop());

                    laytManager.scrollToPositionWithOffset(scrollPos, top);
                }
            }, 200);

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
            JSONArray mCachedData;
            String previousFlow = new String();

            @Override
            protected void onStartLoading() {

                if (previousFlow.equalsIgnoreCase(args.getString(PopularMovieConsts.FLOW))){
                    deliverResult(mCachedData);
                }
                else{
                    previousFlow = args.getString(PopularMovieConsts.FLOW);
                    forceLoad();
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
                mCachedData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, JSONArray data) {
        if (null != data){
            for (int i = 0; i < data.length(); i++){
                try {

                    addItemIntoAdapter(new MovieModel(data.getJSONObject(i).toString()));
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

    static class RecyclerViewState extends View.BaseSavedState{
        public int mScrollPosition;

        RecyclerViewState(Parcel in){
            super(in);
            mScrollPosition = in.readInt();
        }

        RecyclerViewState(Parcelable superState){
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mScrollPosition);
        }

        public static final Parcelable.Creator<RecyclerViewState> CREATOR = new
            Parcelable.Creator<RecyclerViewState>(){

                @Override
                public RecyclerViewState createFromParcel(Parcel source) {
                    return new RecyclerViewState(source);
                }

                @Override
                public RecyclerViewState[] newArray(int size) {
                    return new RecyclerViewState[size];
                }
        };


    }
}
