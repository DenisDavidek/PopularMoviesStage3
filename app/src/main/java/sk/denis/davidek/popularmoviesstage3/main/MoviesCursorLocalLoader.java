package sk.denis.davidek.popularmoviesstage3.main;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;

/**
 * Created by denis on 23.11.2017.
 */

public class MoviesCursorLocalLoader extends AsyncTaskLoader<Cursor> {

    Cursor mMoviesData = null;
    private Context mContext;

    public MoviesCursorLocalLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (mMoviesData != null) {
            deliverResult(mMoviesData);
        } else forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        try {
            return mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    MovieContract.MovieEntry.COLUMN_MOVIE_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
