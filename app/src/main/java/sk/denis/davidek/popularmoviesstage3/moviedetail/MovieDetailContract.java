package sk.denis.davidek.popularmoviesstage3.moviedetail;

import sk.denis.davidek.popularmoviesstage3.Movie;
import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;
import sk.denis.davidek.popularmoviesstage3.main.MainContract;

/**
 * Created by denis on 17.11.2017.
 */

public interface MovieDetailContract {



        interface View extends BaseView<MovieDetailContract.Presenter> {

            void displayMovieTitle(String movieTitle);

    }

    interface Presenter extends BasePresenter {

         void distributeMovieDetails(Movie movie);
    }
}
