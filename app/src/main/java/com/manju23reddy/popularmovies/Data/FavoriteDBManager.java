package com.manju23reddy.popularmovies.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.manju23reddy.popularmovies.Util.PopularMovieConsts;

/**
 * Created by MReddy3 on 1/4/2018.
 */

public class FavoriteDBManager extends SQLiteOpenHelper {



    FavoriteDBManager(Context context){
        super(context, PopularMovieConsts.POPULAR_MOVIES_FAVORITE_DB, null,
                PopularMovieConsts.POPULAR_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE "+
                FavoriteDBContract.FavoriteMovie.TABLE_NAME + " ( "+
                FavoriteDBContract.FavoriteMovie.MOVIE_ID +" INTEGER PRIMARY KEY, "+
                FavoriteDBContract.FavoriteMovie.MOVIE_TITLE+" TEXT NOT NULL, "+
                FavoriteDBContract.FavoriteMovie.MOVIE_POSTER_URL+ " TEXT NOT NULL, "+
                FavoriteDBContract.FavoriteMovie.MOVIE_OVERVIEW+" TEXT NOT NULL, "+
                FavoriteDBContract.FavoriteMovie.MOVIE_RELEASE_DATA+" TEXT NOT NULL, "+
                FavoriteDBContract.FavoriteMovie.MOVIE_RATINGS+" REAL NOT NULL "+
                ");";
        db.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ FavoriteDBContract.FavoriteMovie.TABLE_NAME);
        onCreate(db);
    }
}
