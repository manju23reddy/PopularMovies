package com.manju23reddy.popularmovies.Model;


import com.manju23reddy.popularmovies.Util.PopularMovieConsts;

import org.json.JSONObject;

/**
 * Created by MReddy3 on 12/5/2017.
 */

public class MovieModel {
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
