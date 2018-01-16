package com.manju23reddy.popularmovies.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.manju23reddy.popularmovies.util.PopularMovieConsts;

import org.json.JSONObject;

/**
 * Created by MReddy3 on 12/5/2017.
 */

public class MovieModel implements Parcelable{
    private int movieId;
    private String moviePosterUrl;
    private String movieTitle;
    private String movieReleaseDate;
    private double movieRatings;
    private String moviePlot;


    public MovieModel(int id, String url, String title, String releaseDate, double ratings,
                      String plot) {
        this.movieId = id;
        this.moviePosterUrl = url;
        this.movieTitle = title;
        this.movieReleaseDate = releaseDate;
        this.movieRatings = ratings;
        this.moviePlot = plot;
    }

    private MovieModel(Parcel in){
        this.movieId = in.readInt();
        this.moviePosterUrl = in.readString();
        this.movieTitle = in.readString();
        this.movieReleaseDate = in.readString();
        this.movieRatings = in.readDouble();
        this.moviePlot = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(moviePosterUrl);
        dest.writeString(movieTitle);
        dest.writeString(movieReleaseDate);
        dest.writeDouble(movieRatings);
        dest.writeString(moviePlot);
    }

    public MovieModel(final String paracelString){
        try{
            JSONObject parcelJsonObj = new JSONObject(paracelString);
            this.movieId = parcelJsonObj.getInt(PopularMovieConsts.MOVIE_ID);
            this.moviePosterUrl = parcelJsonObj.getString(PopularMovieConsts.MOVIE_POSTER_URL);
            this.movieTitle = parcelJsonObj.getString(PopularMovieConsts.MOVIE_TITLE);
            this.movieReleaseDate = parcelJsonObj.getString(PopularMovieConsts.MOVIE_RELEASE_DATE);
            this.movieRatings = parcelJsonObj.getDouble(PopularMovieConsts.MOVIE_RATINGS);
            this.moviePlot = parcelJsonObj.getString(PopularMovieConsts.MOVIE_OVERVIEW);
        }
        catch (Exception ee){
            ee.printStackTrace();
        }
    }

    public static final Parcelable.Creator<MovieModel> CREATOR =
            new ClassLoaderCreator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel source, ClassLoader loader) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel createFromParcel(Parcel source) {
            return new MovieModel(source);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public int getMovieId(){
        return movieId;
    }
    public String getMoviePosterUrl(){
        return this.moviePosterUrl;
    }

    public String getMovieTitle(){
        return this.movieTitle;
    }

    public String getMovieReleaseDate(){
        return this.movieReleaseDate;
    }

    public double getMovieRatings(){
        return this.movieRatings;
    }

    public String getMoviePlot(){
        return this.moviePlot;
    }

    public void setMovieId(int id){
        this.movieId = id;
    }
    public void setMoviePosterUrl(String moviePosterUrl){
        this.moviePosterUrl = moviePosterUrl;
    }

    public void setMovieReleaseDate(String ratings){
        this.movieReleaseDate = ratings;
    }

    public void setMovieTitle(String title){
        this.movieTitle = title;
    }

    public void setMovieRatings(double ratings){
        this.movieRatings = ratings;
    }

    public void setMoviePlot(String plot){
        this.moviePlot = plot;
    }

    @Override
    public String toString() {
        try{
            JSONObject toStringObj = new JSONObject();
            toStringObj.put(PopularMovieConsts.MOVIE_ID, movieId);
            toStringObj.put(PopularMovieConsts.MOVIE_POSTER_URL, moviePosterUrl);
            toStringObj.put(PopularMovieConsts.MOVIE_TITLE, movieTitle);
            toStringObj.put(PopularMovieConsts.MOVIE_RELEASE_DATE, movieReleaseDate);
            toStringObj.put(PopularMovieConsts.MOVIE_RATINGS, movieRatings);
            toStringObj.put(PopularMovieConsts.MOVIE_OVERVIEW, moviePlot);

            return toStringObj.toString();
        }
        catch (Exception ee){
            ee.printStackTrace();
        }
        return null;
    }
}
