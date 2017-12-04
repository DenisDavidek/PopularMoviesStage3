package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;

/**
 * Created by denis on 21.11.2017.
 */

public class LocalMoviesLoader extends AsyncTaskLoader<Cursor> {

    Cursor moviesData = null;

    public LocalMoviesLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (moviesData != null) {
            deliverResult(moviesData);
            Log.e("Deliver result ", "called");
        } else forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        try {
            Log.e("Load in background ", "called");
            return getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
