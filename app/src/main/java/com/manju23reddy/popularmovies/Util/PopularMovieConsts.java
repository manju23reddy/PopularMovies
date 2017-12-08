package com.manju23reddy.popularmovies.Util;

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
    //key yyo hold movie plot
    public final static String MOVIE_OVERVIEW = "overview";

    //JSON keys for movie json object
    public final static String POPULAR_LIST = "popular";
    public final static String TOP_RATED_LIST = "top_rated";
    public final static String MOVIE_RESULT = "results";
    public final static String API_KEY = "api_key";
}
