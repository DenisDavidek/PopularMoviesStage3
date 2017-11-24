package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import java.util.ArrayList;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.adapters.TrailersAdapter;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;

/**
 * Created by denis on 24.11.2017.
 */

public class CallbackVideos implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    private Movie movie;
    @Inject
    Context context;

    private MovieDetailContract.Presenter movieDetailPresenter;
    private final MovieDetailContract.View movieDetailView;

    public CallbackVideos(Movie movie, MovieDetailContract.Presenter movieDetailPresenter, MovieDetailContract.View movieDetailView) {
        this.movie = movie;
        this.movieDetailPresenter = movieDetailPresenter;
        this.movieDetailView = movieDetailView;
        App.getAppComponent().inject(this);
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
        return new TrailersLoader(context, args, movie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {

        if (!data.isEmpty()) {

            TrailersAdapter trailersAdapter = new TrailersAdapter(data, movieDetailPresenter);
            movieDetailView.prepareTrailersRecyclerView(trailersAdapter);
        } else {
            movieDetailPresenter.prepareNoTrailersDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }
}
