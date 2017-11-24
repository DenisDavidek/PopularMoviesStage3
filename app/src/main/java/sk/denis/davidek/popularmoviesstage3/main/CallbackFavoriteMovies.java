package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import java.util.ArrayList;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.data.Movie;

/**
 * Created by denis on 24.11.2017.
 */

public class CallbackFavoriteMovies implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    @Inject
    Context mContext;

    private Cursor mCursorLocalMovieData;
    private MainContract.View mainView;

    public CallbackFavoriteMovies(Cursor data, MainContract.View view) {
        this.mCursorLocalMovieData = data;
        this.mainView = view;
        App.getAppComponent().inject(this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new FavoriteMoviesLoader(mContext, mCursorLocalMovieData, mainView);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mainView.hideLoadingProgressBar(View.INVISIBLE);
        if (!data.isEmpty()) {
            mainView.hideNoFavoriteMoviesMessage();
            mainView.showMovieDataView();
            mainView.showFavoriteMoviesData(data);
        } else {
          mainView.showNoFavoriteMoviesMessage();
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }
}