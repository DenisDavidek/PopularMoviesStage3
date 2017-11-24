package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;
import sk.denis.davidek.popularmoviesstage3.data.Movie;

/**
 * Created by denis on 15.11.2017.
 */

public interface MainContract {
    //show
    interface View extends BaseView<Presenter> {


        void prepareRecyclerView();

        void showFavoriteMoviesData(ArrayList<Movie> movies);

        void workWithInternetConnection(boolean hasInternetConnection);

        void showItemClickData(Movie movie);

        void showLoadingProgressBar(int load);

        void hideLoadingProgressBar(int load);

        void showInternetErrorLoadingMessage();

        void showMovieDataView();
    }

    //vsetko ostatné // Presenter žiaden android kód.
    interface Presenter extends BasePresenter {


        void getInternetStatus(Context context);


        void onItemInteraction(Movie movie);

        void setCurrentMovieFilterSetting(SharedPreferences preferences, String key, String value);

        void prepareInternetErrorLoadingMessage();

        void prepareMovieDataView();


    }
}
