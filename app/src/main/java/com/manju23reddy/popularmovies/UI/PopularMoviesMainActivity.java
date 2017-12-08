package com.manju23reddy.popularmovies.UI;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.manju23reddy.popularmovies.Model.MovieModel;
import com.manju23reddy.popularmovies.Model.PopularMovieAdapter;
import com.manju23reddy.popularmovies.Util.PopularMovieConsts;
import com.manju23reddy.popularmovies.R;
import com.manju23reddy.popularmovies.Util.PopularMovieNetworkUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

public class PopularMoviesMainActivity extends AppCompatActivity implements
        PopularMovieAdapter.MovieThumbnailClickListener, View.OnClickListener {

    private static final String TAG = PopularMoviesMainActivity.class.getSimpleName();

    private String API_KEY;
    private static PopularMovieAdapter mMoviesAdapter = null;
    private final int NUMBER_OF_COLUMNS = 4;
    private ProgressBar mDownloaderProgressBar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies_main);

        API_KEY = getAPIKey();

        initUI();

        loadMovies(PopularMovieConsts.POPULAR_LIST);
    }

    /**
     * inti the UI components and set initial states for UI.
     */
    private void initUI(){
        RecyclerView thumbnailsRecyclerView = findViewById(R.id.rcv_movies_list);

        GridLayoutManager thumbNailLyt = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        thumbnailsRecyclerView.setHasFixedSize(true);
        thumbnailsRecyclerView.setLayoutManager(thumbNailLyt);

        mMoviesAdapter = new PopularMovieAdapter(this, this);
        thumbnailsRecyclerView.setAdapter(mMoviesAdapter);

        RadioButton filterByPopularRDBtn = findViewById(R.id.rbtn_most_popular);
        RadioButton filterByRatingRDBtn = findViewById(R.id.rbtn_top_rated);

        filterByPopularRDBtn.setOnClickListener(this);
        filterByRatingRDBtn.setOnClickListener(this);

        mDownloaderProgressBar = findViewById(R.id.pbr_download_indicator);

        mDownloaderProgressBar.setOnClickListener(this);

    }

    /**
     * Show the progress bar and load the movies based on query
     * @param filter
     */
    private void loadMovies(String filter){
        mDownloaderProgressBar.setVisibility(View.VISIBLE);
        new FetchMovieListTask(this).execute(filter, API_KEY);
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
        movieDetailsScreen.putExtra(Intent.EXTRA_TEXT, selectedThumbnail.toString());
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
            case R.id.pbr_download_indicator:
                //since movies are still loading click shall not be allowed to decedents.
                break;
        }
    }

    public void clearAdapter(){
        mMoviesAdapter.clearAll();
    }

    public void addItemIntoAdapter(MovieModel item){
        mMoviesAdapter.addMovie(item);
    }

    /**
     * Get the API key for themoviedb
     * @return
     */
    private String getAPIKey(){
        InputStream isStream = getResources().openRawResource(R.raw.credentials);
        Writer wt = new StringWriter();
        char [] buffer = new char[1024];
        try{
            Reader reader = new BufferedReader(new InputStreamReader(isStream,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer))!= -1){
                wt.write(buffer, 0, n);
            }
            isStream.close();
            JSONObject tempCreds = new JSONObject(wt.toString());
            return tempCreds.getString(PopularMovieConsts.API_KEY);
        }
        catch (Exception ee){
            Log.e("error", ee.getMessage());
        }
        return null;
    }

    /**
     * Async task to perform Network activities
     */
    public static class FetchMovieListTask extends AsyncTask<String, MovieModel, Void>{

        PopularMoviesMainActivity mUIActivityInstance;

        public FetchMovieListTask(PopularMoviesMainActivity uiInst){
            mUIActivityInstance = uiInst;
        }

        @Override
        protected void onPreExecute() {
            mUIActivityInstance.clearAdapter();
        }

        @Override
        protected Void doInBackground(String... queryParams) {
            if (0 == queryParams.length){
                return null;
            }
            String filter_query = queryParams[0];
            String api_key = queryParams[1];
            URL movieRequestURL = PopularMovieNetworkUtil.buildQueryURL(filter_query, api_key);
            try{
                String moviesQueryResult =
                        PopularMovieNetworkUtil.getResponseFromHttpUrl(movieRequestURL);
                JSONObject movieListJsonQut = new JSONObject(moviesQueryResult);
                JSONArray movieResults =
                        movieListJsonQut.getJSONArray(PopularMovieConsts.MOVIE_RESULT);
                for(int i = 0; i < movieResults.length(); i++){
                    JSONObject movie = movieResults.getJSONObject(i);
                    MovieModel curMovie = new MovieModel(
                            movie.getInt(PopularMovieConsts.MOVIE_ID),
                            movie.getString(PopularMovieConsts.MOVIE_POSTER_URL),
                            movie.getString(PopularMovieConsts.MOVIE_TITLE),
                            movie.getString(PopularMovieConsts.MOVIE_RELEASE_DATE),
                            movie.getDouble(PopularMovieConsts.MOVIE_RATINGS),
                            movie.getString(PopularMovieConsts.MOVIE_OVERVIEW));
                    publishProgress(curMovie);
                }
            }
            catch (Exception ee){
                ee.printStackTrace();
            }
            return  null;
        }

        @Override
        protected void onProgressUpdate(MovieModel... values) {
            mUIActivityInstance.addItemIntoAdapter(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mUIActivityInstance.downloadComplete();
        }
    }
}
