package sk.denis.davidek.popularmoviesstage3;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by denis on 21.11.2017.
 */

public class LocalMoviesLoader extends AsyncTaskLoader<Cursor> {


    public LocalMoviesLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return null;
    }
}
