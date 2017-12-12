package sk.denis.davidek.popularmoviesstage3.aboutapp;

import sk.denis.davidek.popularmoviesstage3.base.BasePresenter;
import sk.denis.davidek.popularmoviesstage3.base.BaseView;

/**
 * Created by denis on 11.12.2017.
 */

public interface AboutAppContract {


    interface View extends BaseView<Presenter> {

        void openTMDbWebPage(String url);
        void openGetOtherProductsPage(String url);
    }

    interface Presenter extends BasePresenter {


        void loadUrl();
        void prepareGetOtherProductsPage(String url);

    }

}
