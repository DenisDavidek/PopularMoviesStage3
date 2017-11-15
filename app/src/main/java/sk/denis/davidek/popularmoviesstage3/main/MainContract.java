package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;

import sk.denis.davidek.popularmoviesstage3.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.BaseView;

/**
 * Created by denis on 15.11.2017.
 */

public interface MainContract {
//show
    interface View extends BaseView<Presenter> {
        void test();
        void prepareRecyclerView();
        void setMovieFilter();
        void spracujInternetConnection(boolean hasInternetConnection);


    }
    //vsetko ostatné
    interface Presenter extends BasePresenter {

        void testPresenter();
        void checkInternetConnection(Context context);
    }
}
