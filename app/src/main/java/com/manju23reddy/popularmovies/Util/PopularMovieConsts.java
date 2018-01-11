package com.manju23reddy.popularmovies.Util;

import android.net.Uri;

/**
 * Constants used for KEYS value pairs in Intent Extras, JSON Objects or Models.
 */

public class PopularMovieConsts {
    //intent extra key
    public final static String SELECTED_MOVIE_DETAILS = "movie_details";

    //ket to hold movie id
    public final static String MOVIE_ID = "id";

    //key to hold movie title
    public final static String MOVIE_TITLE = "title";
    //key to hold movie poster url
    public final static String MOVIE_POSTER_URL = "poster_path";
    //key to hold movie release date
    public final static String MOVIE_RELEASE_DATE = "release_date";
    //key to hold movie ratings
    public final static String MOVIE_RATINGS = "vote_average";
    //key to hold movie plot
    public final static String MOVIE_OVERVIEW = "overview";
    //key to hold if movie is favorite
    public final static String MOVIE_IS_FAVORITE = "favorite";

    //JSON keys for movie json object
    public final static String POPULAR_LIST = "popular";
    public final static String TOP_RATED_LIST = "top_rated";
    public final static String MOVIE_RESULT = "results";
    public final static String API_KEY = "api_key";
    public final static String MOVIE_TRAILERS = "videos";
    public final static String MOVIE_REVIEWS = "reviews";

    //DB Constants
    //DB Name
    public final static String POPULAR_MOVIES_FAVORITE_DB = "favorite_movies.db";
    //DB Version
    public final static int POPULAR_DB_VERSION = 1;

    //Content Provider Authority
    public final static String AUTHORITY = "com.manju23reddy.popularmovies";
    public final static String FAVORITE_MOVIES = "FAVORITE_MOVIES";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://"+AUTHORITY);


    public final static String REVIEW_AUTHOR = "author";
    public final static String REVIEW_CONTENT = "content";

    public final static String TRAILER_KEY = "key";
    public final static String RESULT = "results";

    public final static String FLOW = "flow";
    public final static String POPULAR_LIST_FLOW = POPULAR_LIST;
    public final static String TOP_RATED_MOVIES_FLOW =  TOP_RATED_LIST;
    public final static String FAVORITE_MOVIES_FLOW = FAVORITE_MOVIES;

    public final static int NUMBER_OF_FLOWS = 3;

}
