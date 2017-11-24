package sk.denis.davidek.popularmoviesstage3.main;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.view.View;

import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;

/**
 * Created by denis on 23.11.2017.
 */

public class FavoriteMoviesLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    ArrayList<Movie> movieArrayList = new ArrayList<>();
    private Cursor moviesCursorData;
    private MainContract.View mainFragmentView;

    public FavoriteMoviesLoader(Context context, Cursor movieData, MainContract.View view) {
        super(context);
        this.moviesCursorData = movieData;
        this.mainFragmentView = view;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        mainFragmentView.showLoadingProgressBar(View.VISIBLE);
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        while (moviesCursorData.moveToNext()) {
            String movieCursorId = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            String movieOriginalTitle = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE));
            String moviePosterUri = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URI));
            String movieBackgroundUri = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_BACKGROUND_URI));
            String plotSynopsis = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW));
            double userRating = moviesCursorData.getDouble(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE));
            String releaseDate = moviesCursorData.getString(moviesCursorData.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE));
            movieArrayList.add(new Movie(movieCursorId, movieOriginalTitle, moviePosterUri, movieBackgroundUri, plotSynopsis, userRating, releaseDate));
        }
        return movieArrayList;
    }
}
