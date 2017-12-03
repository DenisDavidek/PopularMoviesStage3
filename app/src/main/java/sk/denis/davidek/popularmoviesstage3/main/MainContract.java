package sk.denis.davidek.popularmoviesstage3.main;

import android.content.SharedPreferences;

import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;
import sk.denis.davidek.popularmoviesstage3.data.Movie;

/**
 * Created by denis on 15.11.2017.
 */

public interface MainContract {

    //show - UI actions which will be reflected on the screen.
    interface View extends BaseView<Presenter> {

        void prepareRecyclerView();

        void showFavoriteMoviesData(ArrayList<Movie> movies);

        void showItemClickData(Movie movie);

        void showLoadingProgressBar(int load);

        void hideLoadingProgressBar(int load);

        void showInternetErrorLoadingMessage();

        void hideInternetErrorLoadingMessage();

        void showMovieDataView();

        void showNoFavoriteMoviesMessage();

        void hideNoFavoriteMoviesMessage();

        void loadAndShowAd(AdRequest adRequest);
    }

    //vsetko ostatné // Presenter žiaden android kód.
    interface Presenter extends BasePresenter {

        void onItemInteraction(Movie movie);

        void setCurrentMovieFilterSetting(SharedPreferences preferences, String key, String value);

        void prepareInternetErrorLoadingMessage();

        void prepareMovieDataView();

        void prepareAdRequest();

    }
}
