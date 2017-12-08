package com.manju23reddy.popularmovies.Util;

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.manju23reddy.popularmovies.Util.PopularMovieConsts.API_KEY;
import static com.manju23reddy.popularmovies.Util.PopularMovieConsts.POPULAR_LIST;
import static com.manju23reddy.popularmovies.Util.PopularMovieConsts.TOP_RATED_LIST;

/**
 * Network util to handle downloading of movies from http url.
 */

public class PopularMovieNetworkUtil {
    //Base url for themoviedb
    private static final String POPULAR_MOVIES_BASE_URL =
            "https://api.themoviedb.org/3/movie";

    //url for downloading movie poster
    public static final String POSTER_URL = "http://image.tmdb.org/t/p/w185";

    //URL for popular movies list
    private static final String POPULAR_MOVIES_MOST_POPULAR = POPULAR_MOVIES_BASE_URL+"/"+POPULAR_LIST;
    //URL for top rated movies list
    private static final String POPULAR_MOVIES_TOP_RATED = POPULAR_MOVIES_BASE_URL+"/"+TOP_RATED_LIST;

    /**
     * Generates the URL based on the filter query
     * @param filter filter applied for downloading the movies list
     * @param api_key api key
     * @return returns the URL with API_KEY and Query string formatted
     */
    public static URL buildQueryURL(String filter, String api_key){
        String url = "";
        URL generatedURL = null;
        switch (filter){
            case POPULAR_LIST:
                url = POPULAR_MOVIES_MOST_POPULAR;
                break;
            case TOP_RATED_LIST:
                url = POPULAR_MOVIES_TOP_RATED;
                break;

        }
        Uri queryUrl = Uri.parse(url).buildUpon()
                .appendQueryParameter(API_KEY, api_key)
                .build();
        try{
            generatedURL = new URL(queryUrl.toString());
        }
        catch (MalformedURLException ee){
            ee.printStackTrace();
        }
        return generatedURL;
    }

    /**
     * Network function to download the movies list from network.
     * @param url to download from network
     * @return returns the string representation of result. or Null if fails.
     * @throws IOException
     */
    public static String getResponseFromHttpUrl(URL url)throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            }
            else{
                return null;
            }
        }
        finally {
            urlConnection.disconnect();
        }
    }
}
