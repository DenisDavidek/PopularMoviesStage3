package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.adapters.ReviewsAdapter;
import sk.denis.davidek.popularmoviesstage3.adapters.TrailersAdapter;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.LoaderConstants;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.Review;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;
import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;
import sk.denis.davidek.popularmoviesstage3.utils.AdUtils;
import sk.denis.davidek.popularmoviesstage3.utils.NetworkUtils;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View,
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

    @BindView(R.id.fab_favorite)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.backdrop_image)
    ImageView backdropImageView;


    private Movie selectedMovie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    @BindString(R.string.no_internet_connection)
    String noInternetConnectionMessage;

    @Inject
    Context context;
    private boolean isFavoriteMovie;

    @BindInt(R.integer.amountOfItems)
    int amountOfItems;

    @BindView(R.id.adView)
    AdView mAdView;

    @BindView(R.id.cv_movie_plot_synopsis)
    CardView moviePlotSynopsisCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbarBackIcon();


        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        movieDetailPresenter = new MovieDetailPresenter(this);


        mAdView.setAdListener(new AdListener() {


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) moviePlotSynopsisCardView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.BELOW, R.id.cv_basic_movie_info);
                moviePlotSynopsisCardView.setLayoutParams(layoutParams);
                Log.e("onADFAILED ", "called");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieKey)) {
            selectedMovie = intent.getParcelableExtra(selectedMovieKey);
            movieDetailPresenter.distributeMovieDetails(selectedMovie);

            if (NetworkUtils.checkInternetConnection(context)) {
                initializeGetReviewsLoader(Constants.getMovieQueryText(), Constants.getReviewQueryText());
                initializeGetTrailersLoader(Constants.getMovieQueryText(), Constants.getTrailerQueryText());
            } else {
                hideReviewsDataView(noInternetConnectionMessage);
                hideTrailersDataView(noInternetConnectionMessage);
            }

            checkIfMovieIsFavorite();
            movieDetailPresenter.prepareCollapsingToolbarLayout(selectedMovie);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isFavoriteMovie) {
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(selectedMovie.getId()).build();
                    getContentResolver().delete(uri, null, null);
                    isFavoriteMovie = false;
                    checkIfMovieIsFavorite();
                    Snackbar.make(view, getString(R.string.snackBar_movie_deleted_from_favorites), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackBar_action), null).show();

                } else {
                    Uri finalPosterUri = movieDetailPresenter.downloadPosterFile(selectedMovie.getPosterUrl(), selectedMovie, context);
                    Uri finalBackgroundUri = movieDetailPresenter.downloadBackgroundFile(selectedMovie.getBackgroundUrl(), selectedMovie, context);
                    movieDetailPresenter.insertFavoriteMovieIntoContentProvidersDatabase(context, selectedMovie, finalPosterUri, finalBackgroundUri);
                    Snackbar.make(view, getString(R.string.snackBar_movie_added_to_favorites), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackBar_action), null).show();
                    checkIfMovieIsFavorite();
                }

            }
        });


        AdUtils adUtils = new AdUtils();
        if (!(adUtils.isTestDevice())) {

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }


    }

    private void setupToolbarBackIcon() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
        noMoviewReviewsTextView.setText(message);
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
        collapsingToolbarLayout.setTitle(movie.getOriginalTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        setTitle("");
        movieDetailPresenter.getBackgroundMovieImage(context, movie);
    }

    @Override
    public void displayMovieImageBackground(Bitmap bitmap) {
        backdropImageView.setImageBitmap(bitmap);
    }

    @Override
    public void displayMovieImageBackground(String movieBackgroundUrl) {
        Picasso.with(this).load(movieBackgroundUrl).into(backdropImageView);
    }

    @Override
    public void prepareTrailersRecyclerView(TrailersAdapter trailersAdapter) {
        movieTrailersRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, amountOfItems);
        movieTrailersRecyclerView.setLayoutManager(layoutManager);
        movieTrailersRecyclerView.setAdapter(trailersAdapter);
    }

    @Override
    public void displayMovieImagePoster(Bitmap bitmap) {
        moviePosterImageView.setImageBitmap(bitmap);
    }

    @Override
    public void displayMovieImagePoster(String moviePosterUrl) {
        Picasso.with(this).load(moviePosterUrl).into(moviePosterImageView);
    }


    private void initializeGetReviewsLoader(String movie, String videoOrReview) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(Constants.getMovieDefault(), movie);
        argsBundle.putString(Constants.getMovieVideoOrReview(), videoOrReview);

        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
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

        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        android.support.v4.content.Loader<ArrayList<Trailer>> getMoviesLoader = loaderManager.getLoader(LoaderConstants.getTrailersLoader());

        if (getMoviesLoader == null) {
            loaderManager.initLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos(selectedMovie, movieDetailPresenter, this));
        } else
            loaderManager.restartLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos(selectedMovie, movieDetailPresenter, this));

    }


    public void checkIfMovieIsFavorite() {
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        android.support.v4.content.Loader<Cursor> getCursorLoader = loaderManager.getLoader(LoaderConstants.getMoviesFavoritesLoader());

        if (getCursorLoader == null) {
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

                        if (data.getString(data.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)).equals(selectedMovie.getId())) {
                            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_favorite));
                            isFavoriteMovie = true;
                        }
                    }
                    if (!isFavoriteMovie) {
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_unfavorite));
                    }
                } else {
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.button_unfavorite));
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //   Log.e("onLoadReset TU ", "called");
        }

    }


    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsLoader(context, args, selectedMovie);
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


}
