package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.Movie;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by denis on 15.11.2017.
 */

public class MainPresenter implements MainContract.Presenter {


    private final MainContract.View mainView;


    public MainPresenter(MainContract.View mainView) {

        this.mainView = mainView;

        mainView.setPresenter(this);
    }


    @Override
    public void testPresenter() {

    }

    @Override
    @Inject
    public void checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mainView.workWithInternetConnection(networkInfo != null && networkInfo.isConnectedOrConnecting());


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
    public void prepareErrorLoadingMessage() {
        mainView.showErrorLoadingMessage();
    }

    @Override
    public void prepareMovieDataView() {
        mainView.showMoviewDataView();
    }


    @Override
    public void start() {
        mainView.test();
    }
}
