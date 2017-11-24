package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import sk.denis.davidek.popularmoviesstage3.adapters.TrailersAdapter;
import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;
import sk.denis.davidek.popularmoviesstage3.data.Movie;

/**
 * Created by denis on 17.11.2017.
 */

public interface MovieDetailContract {


    interface View extends BaseView<MovieDetailContract.Presenter> {

        void displayMovieTitle(String movieTitle);

        void displayMoviePlotSynopsis(String plotSynopsis);

        void displayUserRating(double userRating);

        void displayReleaseDate(String releaseDate);

        void displayMoviePoster(String posterUrl);

        void prepareRecyclerView();

        void showReviewsDataView();

        void hideReviewsDataView(String message);

        void hideTrailersDataView();

        void watchYoutubeMovieTrailer(String movieKey);

        void setupCollapsingToolbarLayout(Movie movie);

        void displayMovieImageBackground(Bitmap bitmap);

        void displayMovieImageBackground(String movieBackgroundUrl);

        void prepareTrailersRecyclerView(TrailersAdapter trailersAdapter);
    }

    interface Presenter extends BasePresenter {

        void distributeMovieDetails(Movie movie);

        void formatReleaseDate(String releaseDate);

        void prepareReviewsDataView();

        void prepareNoReviewsDataView(String message);

        void prepareNoTrailersDataView();

        void prepareYoutubeMovieTrailer(String movieKey);

        Uri downloadPosterFile(String moviePosterUrl, Movie movie, Context context);

        void insertFavoriteMovieIntoContentProvidersDatabase(Context context, Movie movie, Uri finalPosterUri, Uri finalBackgroundUri);

        Uri downloadBackgroundFile(String moviePosterUrl, Movie movie, Context context);

        void prepareCollapsingToolbarLayout(Movie movie);

        void getBackgroundMovieImage(Context context, Movie movie);

    }
}
