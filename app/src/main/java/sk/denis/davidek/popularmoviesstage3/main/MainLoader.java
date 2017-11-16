package sk.denis.davidek.popularmoviesstage3.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.BuildConfig;
import sk.denis.davidek.popularmoviesstage3.Movie;
import sk.denis.davidek.popularmoviesstage3.utils.NetworkUtils;

/**
 * Created by denis on 15.11.2017.
 */

class MainLoader extends AsyncTaskLoader<ArrayList<Movie>> {
    private Bundle bundle;
    ArrayList<Movie> movies = new ArrayList<>();

    /**
     * Stores away the application context associated with context.
     * Since Loaders can be used across multiple activities it's dangerous to
     * store the context directly; always use {@link #getContext()} to retrieve
     * the Loader's Context, don't use the constructor argument directly.
     * The Context returned by {@link #getContext} is safe to use across
     * Activity instances.
     *
     * @param context used to retrieve the application context.
     */
    public MainLoader(Context context, Bundle bundle) {
        super(context);
        this.bundle = bundle;
    }

    private static final String QUERY_MOVIE_FILTER = "movie_filter";

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    private static final String MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIES_IMAGE_SIZE = "w500/";

    @Override
    public ArrayList<Movie> loadInBackground() {
        String movieFilter = bundle.getString(QUERY_MOVIE_FILTER);

        final String apiKey = BuildConfig.API_KEY;
        URL moviesRequestUrl = NetworkUtils.buildUrl(apiKey, movieFilter);

        String responseJSONString = NetworkUtils.getResponseFromHttp(moviesRequestUrl);

        try {
            JSONObject parentJSONObject = new JSONObject(responseJSONString);
            JSONArray moviesArray = parentJSONObject.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {

                JSONObject childMovieObject = moviesArray.getJSONObject(i);

                String movieId = childMovieObject.getString("id");
                String originalTitle = childMovieObject.getString("original_title");
                String moviePosterUrl = MOVIES_IMAGE_BASE_URL + MOVIES_IMAGE_SIZE + childMovieObject.getString("poster_path");
                String plotSynopsis = childMovieObject.getString("overview");
                double userRating = childMovieObject.getDouble("vote_average");
                String releaseDate = childMovieObject.getString("release_date");

                movies.add(new Movie(movieId, originalTitle, moviePosterUrl, plotSynopsis, userRating, releaseDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

}
