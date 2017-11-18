package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.BuildConfig;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.Review;
import sk.denis.davidek.popularmoviesstage3.utils.NetworkUtils;

/**
 * Created by denis on 18.11.2017.
 */

public class ReviewsLoader extends AsyncTaskLoader<ArrayList<Review>> {

    private Movie movie;
    private ArrayList<Review> reviews = new ArrayList<>();
    private Bundle bundle;


    public ReviewsLoader(Context context, Bundle bundle, Movie movie) {
        super(context);
        this.bundle = bundle;
        this.movie = movie;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<Review> loadInBackground() {
        String movieDefault = bundle.getString(Constants.getMovieDefault());

        String videoOrReview = bundle.getString(Constants.getMovieVideoOrReview());

        final String apiKey = BuildConfig.API_KEY;

        URL reviewRequestUrl = NetworkUtils.buildReviewsAndVideosUrl(apiKey, movieDefault, movie.getId(), videoOrReview);
        String responseJSONString = NetworkUtils.getResponseFromHttp(reviewRequestUrl);

        try {
            JSONObject parentJSONObject = new JSONObject(responseJSONString);
            JSONArray reviewsArray = parentJSONObject.getJSONArray("results");

            if (reviewsArray.length() != 0) {

                for (int i = 0; i < reviewsArray.length(); i++) {

                    JSONObject childMoviewObject = reviewsArray.getJSONObject(i);

                    String author = childMoviewObject.getString("author");
                    String content = childMoviewObject.getString("content");

                    reviews.add(new Review(author, content));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviews;
    }

}
