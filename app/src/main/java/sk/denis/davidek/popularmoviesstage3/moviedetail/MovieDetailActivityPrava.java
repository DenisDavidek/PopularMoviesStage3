package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
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

public class MovieDetailActivityPrava extends AppCompatActivity implements MovieDetailContract.View,
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

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.backdrop_image)
    ImageView backdropImageView;


    private Movie movie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    @Inject
    Context context;
    private boolean isFavoriteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_prava);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbarBackIcon();
    /*    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        ButterKnife.bind(this);
        setupCollapsingToolbarLayout();
        App.getAppComponent().inject(this);
        movieDetailPresenter = new MovieDetailPresenter(this);
        Intent intent = getIntent();
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
                    Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(movie.getId()).build();
                    getContentResolver().delete(uri, null, null);
                    isFavoriteMovie = false;
                    checkIfMovieIsFavorite();
                    Snackbar.make(view, getString(R.string.movie_deleted_from_favorites), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_action), null).show();

                } else {
                    Uri finalPosterUri = movieDetailPresenter.downloadPosterFile(movie.getPosterUrl(), movie, context);
                    Uri finalBackgroundUri = movieDetailPresenter.downloadBackgroundFile(movie.getBackgroundUrl(), movie, context);
                    Toast.makeText(context, "IMAGE DOWNLOADING " + finalPosterUri, Toast.LENGTH_LONG).show();
                    Toast.makeText(context, "IMAGE DOWNLOADING " + finalBackgroundUri, Toast.LENGTH_LONG).show();
                    movieDetailPresenter.insertFavoriteMovieIntoContentProvidersDatabase(context, movie, finalPosterUri, finalBackgroundUri);
                    Snackbar.make(view, getString(R.string.movie_added_to_favorites), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.snackbar_action), null).show();
                    checkIfMovieIsFavorite();
                }

            }
        });


    }

    private void setupToolbarBackIcon() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupCollapsingToolbarLayout() {
        collapsingToolbarLayout.setTitle(getMovieTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        setTitle("");


        if (movie.getBackgroundUrl().startsWith("file://")) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(movie.getBackgroundUrl()));
                backdropImageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "URI BACKGROUND", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Picasso.with(this).load(movie.getBackgroundUrl()).into(backdropImageView);
            Toast.makeText(getApplicationContext(), "PICASSO BACKGROUND", Toast.LENGTH_LONG).show();
        }


    }

    private String getMovieTitle() {

        String returnedTitle;
        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieKey)) {
            movie = intent.getParcelableExtra(selectedMovieKey);
            returnedTitle = movie.getOriginalTitle();
        } else
            returnedTitle = "";

        return returnedTitle;
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
    public void displayMoviePoster(String posterUrl) {

        if (posterUrl.startsWith("file://")) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(posterUrl));
                moviePosterImageView.setImageBitmap(bitmap);
                Toast.makeText(getApplicationContext(), "URI ", Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Picasso.with(this).load(posterUrl).into(moviePosterImageView);
            Toast.makeText(getApplicationContext(), "PICASSO ", Toast.LENGTH_LONG).show();
        }

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
    public void hideReviewsDataView() {
        movieReviewsRecyclerView.setVisibility(View.INVISIBLE);
        noMoviewReviewsTextView.setVisibility(View.VISIBLE);
        noMoviewReviewsTextView.setText(noMovieReviewsMessage);
    }

    @Override
    public void hideTrailersDataView() {
        movieTrailersRecyclerView.setVisibility(View.INVISIBLE);
        noMovieTrailersTextView.setVisibility(View.VISIBLE);
        noMovieTrailersTextView.setText(noMovieTrailersMessage);
    /*    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBinding.trailersSectionDivider.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.tv_no_trailers_text);*/
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
            loaderManager.initLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos());
        } else
            loaderManager.restartLoader(LoaderConstants.getTrailersLoader(), argsBundle, new CallbackVideos());

    }


    public void checkIfMovieIsFavorite() {
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
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
                            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_white_36dp));
                            //   floatingActionButton.setBackground(ContextCompat.getDrawable(context,R.drawable.ic_favorite_white_36dp));
                            isFavoriteMovie = true;
                        }
                    }
                    if (!isFavoriteMovie) {
                        Toast.makeText(context, "This is NOT a favorite movie", Toast.LENGTH_SHORT).show();
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border_white_36dp));
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
                GridLayoutManager layoutManager = new GridLayoutManager(context, LayoutUtils.calculateNoOfColumns(context));
                movieTrailersRecyclerView.setLayoutManager(layoutManager);
                movieTrailersRecyclerView.setAdapter(trailersAdapter);

            } else {
                movieDetailPresenter.prepareNoTrailersDataView();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    }


    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsLoader(context, args, movie);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

        if (!data.isEmpty()) {

            ReviewsAdapter reviewsAdapter = new ReviewsAdapter(data);
            movieDetailPresenter.prepareReviewsDataView();
            movieReviewsRecyclerView.setAdapter(reviewsAdapter);

        } else {
            movieDetailPresenter.prepareNoReviewsDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

    }


}
