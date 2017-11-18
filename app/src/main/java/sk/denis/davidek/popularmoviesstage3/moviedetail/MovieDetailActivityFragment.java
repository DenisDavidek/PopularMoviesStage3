package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.Review;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieDetailContract.View,
        LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    @BindString(R.string.movie_key)
    String selectedMovieKey;

    @BindView(R.id.tv_movie_title)
    TextView movieTitleTextView;

    @BindView(R.id.tv_movie_plot_synopsis)
    TextView moviePlotSynopsisTextView;

    @BindView(R.id.tv_movie_user_rating)
    TextView movieUserRatingTextView;

    @BindView(R.id.tv_movie_release_date)
    TextView movieReleaseDateTextView;

    @BindView(R.id.iv_movie_poster)
    ImageView moviePosterImageView;

    private Movie movie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    @Inject
    Context context;

    private int REVIEWS_GET_LOADER = 23;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, fragmentView);
        App.getAppComponent().inject(this);
        movieDetailPresenter = new MovieDetailPresenter(this);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(selectedMovieKey)) {
            movie = intent.getParcelableExtra(selectedMovieKey);
            movieDetailPresenter.distributeMovieDetails(movie);
            initializeGetReviewsLoader(Constants.getMovieQueryText(),Constants.getReviewQueryText());

        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        movieDetailPresenter.start();
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        movieDetailPresenter = presenter;

    }

    @Override
    public void displayMovieTitle(String movieTitle) {
        movieTitleTextView.setText(movieTitle);
    }

    @Override
    public void displayMoviePlotSynopsis(String plotSynopsis) {
        moviePlotSynopsisTextView.setText(plotSynopsis);
    }

    @Override
    public void displayUserRating(double userRating) {
        movieUserRatingTextView.setText(String.valueOf(userRating));
    }

    @Override
    public void displayReleaseDate(String releaseDate) {
        movieReleaseDateTextView.setText(releaseDate);
    }

    @Override
    public void displayMoviePoster(String posterUrl) {
        Picasso.with(context).load(posterUrl).into(moviePosterImageView);
    }


    private void initializeGetReviewsLoader(String movie, String videoOrReview) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(Constants.getMovieDefault(), movie);
        argsBundle.putString(Constants.getMovieVideoOrReview(), videoOrReview);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<ArrayList<Review>> getMoviesLoader = loaderManager.getLoader(REVIEWS_GET_LOADER);

        if (getMoviesLoader == null) {
            loaderManager.initLoader(REVIEWS_GET_LOADER, argsBundle, this);
        } else
            loaderManager.restartLoader(REVIEWS_GET_LOADER, argsBundle, this);

    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
      return new ReviewsLoader(getContext(), args, movie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }
}
