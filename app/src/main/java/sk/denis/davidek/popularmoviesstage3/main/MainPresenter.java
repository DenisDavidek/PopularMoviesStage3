package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.utils.AdUtils;

/**
 * Created by denis on 15.11.2017.
 */

public class MainPresenter implements MainContract.Presenter {


    private final MainContract.View mainView;
    @Inject
    Context context;

    private AdUtils adUtils;

    public MainPresenter(MainContract.View mainView) {

        this.mainView = mainView;
        adUtils = new AdUtils();
        mainView.setPresenter(this);
        App.getAppComponent().inject(this);
    }

    @Override
    public void onItemInteraction(Movie movie) {
        mainView.showItemClickData(movie);
    }

    @Override
    public void setCurrentMovieFilterSetting(SharedPreferences preferences, String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void prepareInternetErrorLoadingMessage() {
        mainView.showInternetErrorLoadingMessage();
    }

    @Override
    public void prepareMovieDataView() {
        mainView.showMovieDataView();
    }

    @Override
    public void prepareAdRequest() {

        mainView.loadAndShowAd(adUtils.getAdRequest());
    }


    @Override
    public void start() {
        mainView.prepareRecyclerView();
    }
}
