package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.BuildConfig;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;
import sk.denis.davidek.popularmoviesstage3.utils.NetworkUtils;

/**
 * Created by denis on 19.11.2017.
 */

public class TrailersLoader extends AsyncTaskLoader<ArrayList<Trailer>> {

    private ArrayList<Trailer> trailers = new ArrayList<>();
    private final Bundle bundle;
    private final Movie movie;

    public TrailersLoader(Context context, Bundle bundle, Movie movie) {
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
    public ArrayList<Trailer> loadInBackground() {
        String movieDefault = bundle.getString(Constants.getMovieDefault());

        String videoOrReview = bundle.getString(Constants.getMovieVideoOrReview());

        final String apiKey = BuildConfig.API_KEY;

        URL trailerRequestUrl = NetworkUtils.buildReviewsAndVideosUrl(apiKey, movieDefault, movie.getId(), videoOrReview);
        String responseJSONString = NetworkUtils.getResponseFromHttp(trailerRequestUrl);

        try {
            JSONObject parentJSONObject = new JSONObject(responseJSONString);
            JSONArray trailersArray = parentJSONObject.getJSONArray("results");

            if (trailersArray.length() != 0) {

                for (int i = 0; i < trailersArray.length(); i++) {

                    JSONObject childMoviewObject = trailersArray.getJSONObject(i);

                    String name = childMoviewObject.getString("name");
                    String key = childMoviewObject.getString("key");

                    trailers.add(new Trailer(name, key));

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return trailers;
    }
    }

