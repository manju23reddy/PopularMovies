package com.manju23reddy.popularmovies.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.manju23reddy.popularmovies.Util.PopularMovieConsts;

public class FavoriteMovieContentProvider extends ContentProvider {

    FavoriteDBManager mFavDBManager = null;

    public static final int ALL_MOVIES = 100;
    public static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(PopularMovieConsts.AUTHORITY, PopularMovieConsts.FAVORITE_MOVIES,
                ALL_MOVIES);
        uriMatcher.addURI(PopularMovieConsts.AUTHORITY,
                PopularMovieConsts.FAVORITE_MOVIES+"/#", MOVIE_ID);

        return uriMatcher;
    }

    public FavoriteMovieContentProvider() {

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final SQLiteDatabase db = mFavDBManager.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int deletedMovie;

        if (MOVIE_ID == match){
            String movieID = uri.getPathSegments().get(1);
            deletedMovie = db.delete(FavoriteDBContract.FavoriteMovie.TABLE_NAME,
                    FavoriteDBContract.FavoriteMovie.MOVIE_ID+"=?",
                    new String[]{movieID});
        }
        else {
            throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        if (deletedMovie != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedMovie;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mFavDBManager.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        if (ALL_MOVIES == match){
            try {
                long id = db.insert(FavoriteDBContract.FavoriteMovie.TABLE_NAME, null,
                        values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteDBContract.FavoriteMovie.CONTENT_URI,
                            id);
                } else {
                    throw new UnsupportedOperationException("Failed to add movie as favorite " + uri);
                }
            }
            catch (Exception ee){
                throw new UnsupportedOperationException("Failed to add movie as favorite " + uri);
            }
        }
        else {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavDBManager = new FavoriteDBManager(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mFavDBManager.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor resultCursor;

        switch (match){
            case ALL_MOVIES:
            case MOVIE_ID:
                resultCursor = db.query(FavoriteDBContract.FavoriteMovie.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unkown uri "+uri);
        }
        return resultCursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
