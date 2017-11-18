package sk.denis.davidek.popularmoviesstage3.moviedetail;

import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;

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
    }

    interface Presenter extends BasePresenter {

        void distributeMovieDetails(Movie movie);

        void formatReleaseDate(String releaseDate);

    }
}
