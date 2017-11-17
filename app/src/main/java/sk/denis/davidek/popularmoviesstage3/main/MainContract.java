package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.SharedPreferences;

import sk.denis.davidek.popularmoviesstage3.Movie;
import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;

/**
 * Created by denis on 15.11.2017.
 */

public interface MainContract {
    //show
    interface View extends BaseView<Presenter> {
        void test();

        void prepareRecyclerView();

        void setMovieFilter();

        void workWithInternetConnection(boolean hasInternetConnection);

        void showItemClickData(Movie movie);

        void showLoadingProgressBar(int load);

        void showErrorLoadingMessage();

        void showMoviewDataView();
    }

    //vsetko ostatné // Presenter žiaden android kód.
    interface Presenter extends BasePresenter {

        void testPresenter();

        void checkInternetConnection(Context context);

        void onItemInteraction(Movie movie);

        void setCurrentMovieFilterSetting(SharedPreferences preferences, String key, String value);

        void prepareErrorLoadingMessage();

        void prepareMovieDataView();


    }
}
