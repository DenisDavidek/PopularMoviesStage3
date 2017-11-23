package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Context;
import android.net.Uri;

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

        void hideReviewsDataView();

        void hideTrailersDataView();

        void watchYoutubeMovieTrailer(String movieKey);
    }

    interface Presenter extends BasePresenter {

        void distributeMovieDetails(Movie movie);

        void formatReleaseDate(String releaseDate);

        void prepareReviewsDataView();

        void prepareNoReviewsDataView();

        void prepareNoTrailersDataView();

        void prepareYoutubeMovieTrailer(String movieKey);

        Uri downloadPosterFile(String moviePosterUrl, Movie movie, Context context);

        void insertFavoriteMovieIntoContentProvidersDatabase(Context context, Movie movie, Uri finalPosterUri, Uri finalBackgroundUri);

        Uri downloadBackgroundFile(String moviePosterUrl, Movie movie, Context context);

    }
}
