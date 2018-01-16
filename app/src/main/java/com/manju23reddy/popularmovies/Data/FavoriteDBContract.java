package com.manju23reddy.popularmovies.Data;

import android.net.Uri;

import com.manju23reddy.popularmovies.util.PopularMovieConsts;

/**
 * Created by MReddy3 on 1/4/2018.
 */

public class FavoriteDBContract {

    public static final class FavoriteMovie {

        public static final Uri CONTENT_URI = PopularMovieConsts.BASE_CONTENT_URI.buildUpon().
                appendPath(PopularMovieConsts.FAVORITE_MOVIES).build();

        public static final String TABLE_NAME = "favoriteTable";
        public static final String MOVIE_ID = PopularMovieConsts.MOVIE_ID;
        public static final String MOVIE_TITLE = PopularMovieConsts.MOVIE_TITLE;
        public static final String MOVIE_POSTER_URL = PopularMovieConsts.MOVIE_POSTER_URL;
        public static final String MOVIE_RELEASE_DATA = PopularMovieConsts.MOVIE_RELEASE_DATE;
        public static final String MOVIE_RATINGS = PopularMovieConsts.MOVIE_RATINGS;
        public static final String MOVIE_OVERVIEW = PopularMovieConsts.MOVIE_OVERVIEW;

        public static final String MOVIE_REVIEWS = PopularMovieConsts.MOVIE_REVIEWS;
    }

}
