package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.LocalMoviesLoader;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.adapters.ReviewsAdapter;
import sk.denis.davidek.popularmoviesstage3.adapters.TrailersAdapter;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.LoaderConstants;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.Review;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;
import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;
import sk.denis.davidek.popularmoviesstage3.utils.LayoutUtils;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment implements MovieDetailContract.View,
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


    @BindView(R.id.rv_movie_reviews)
    RecyclerView movieReviewsRecyclerView;

    @BindView(R.id.tv_no_reviews_text)
    TextView noMoviewReviewsTextView;

    @BindString(R.string.movie_no_review)
    String noMovieReviewsMessage;


    @BindView(R.id.rv_movie_trailers)
    RecyclerView movieTrailersRecyclerView;

    @BindView(R.id.tv_no_trailers_text)
    TextView noMovieTrailersTextView;

    @BindString(R.string.movie_no_trailer)
    String noMovieTrailersMessage;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private Movie movie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    @Inject
    Context context;
    private boolean isFavoriteMovie;


    public MovieDetailFragment() {
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
            initializeGetReviewsLoader(Constants.getMovieQueryText(), Constants.getReviewQueryText());
            initializeGetTrailersLoader(Constants.getMovieQueryText(), Constants.getTrailerQueryText());

            checkIfMovieIsFavorite();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavoriteMovie) {

                } else {

                }

            }
        });
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

    @Override
    public void prepareRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        movieReviewsRecyclerView.setLayoutManager(layoutManager);
        movieReviewsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void showReviewsDataView() {
        movieReviewsRecyclerView.setVisibility(View.VISIBLE);
        noMoviewReviewsTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideReviewsDataView(String message) {
        movieReviewsRecyclerView.setVisibility(View.INVISIBLE);
        noMoviewReviewsTextView.setVisibility(View.VISIBLE);
        noMoviewReviewsTextView.setText(noMovieReviewsMessage);
    }

    @Override
    public void hideTrailersDataView(String message) {
        movieTrailersRecyclerView.setVisibility(View.INVISIBLE);
        noMovieTrailersTextView.setVisibility(View.VISIBLE);
        noMovieTrailersTextView.setText(message);
    }




    @Override
    public void watchYoutubeMovieTrailer(String movieKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + movieKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + movieKey));

        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }
    }

    @Override
    public void setupCollapsingToolbarLayout(Movie movie) {

    }

    @Override
    public void displayMovieImageBackground(Bitmap bitmap) {

    }

    @Override
    public void displayMovieImageBackground(String movieBackgroundUrl) {

    }

    @Override
    public void prepareTrailersRecyclerView(TrailersAdapter trailersAdapter) {

    }


    private void initializeGetReviewsLoader(String movie, String videoOrReview) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(Constants.getMovieDefault(), movie);
        argsBundle.putString(Constants.getMovieVideoOrReview(), videoOrReview);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<ArrayList<Review>> getMoviesLoader = loaderManager.getLoader(LoaderConstants.getReviewsLoader());

        if (getMoviesLoader == null) {
            loaderManager.initLoader(LoaderConstants.getReviewsLoader(), argsBundle, this);
        } else
            loaderManager.restartLoader(LoaderConstants.getReviewsLoader(), argsBundle, this);

    }


    private void initializeGetTrailersLoader(String movie, String videoOrReview) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(Constants.getMovieDefault(), movie);
        argsBundle.putString(Constants.getMovieVideoOrReview(), videoOrReview);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<ArrayList<Trailer>> getMoviesLoader = loaderManager.getLoader(LoaderConstants.getTrailersLoader());

        if (getMoviesLoader == null) {
            loaderManager.initLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos());
        } else
            loaderManager.restartLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos());

    }


    public void checkIfMovieIsFavorite() {
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<Cursor> getCusorLoader = loaderManager.getLoader(LoaderConstants.getMoviesFavoritesLoader());

        if (getCusorLoader == null) {
            loaderManager.initLoader(LoaderConstants.getMoviesFavoritesLoader(), null, new CallbackQuery());
        } else
            loaderManager.restartLoader(LoaderConstants.getMoviesFavoritesLoader(), null, new CallbackQuery());
    }


    private class CallbackQuery implements LoaderManager.LoaderCallbacks<Cursor> {


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new LocalMoviesLoader(context);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data != null) {
                if (data.getCount() > 0) {
                    while (data.moveToNext()) {

                        if (data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)).equals(movie.getId())) {
                            Toast.makeText(context, "This is a favorite movie", Toast.LENGTH_SHORT).show();
                            isFavoriteMovie = true;
                        }
                    }
                    if (!isFavoriteMovie) {
                        Toast.makeText(context, "This is a favorite movie", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }


    private class CallbackVideos implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle args) {
            return new TrailersLoader(context, args, movie);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {

            if (!data.isEmpty()) {

                TrailersAdapter trailersAdapter = new TrailersAdapter(data, movieDetailPresenter);

                movieTrailersRecyclerView.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), LayoutUtils.calculateNoOfColumns(context));
                movieTrailersRecyclerView.setLayoutManager(layoutManager);
                movieTrailersRecyclerView.setAdapter(trailersAdapter);

            } else {
                movieDetailPresenter.prepareNoTrailersDataView(noMovieTrailersMessage);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    }


    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsLoader(getContext(), args, movie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

        if (!data.isEmpty()) {

            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(data);
            movieDetailPresenter.prepareReviewsDataView();
            movieReviewsRecyclerView.setAdapter(reviewsAdapter);

        } else {
            movieDetailPresenter.prepareNoReviewsDataView(noMovieReviewsMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }

    private OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void onClick(int position);
    }
}
