package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import javax.inject.Inject;

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

    @Override @Inject
    public void checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        mainView.spracujInternetConnection(networkInfo != null && networkInfo.isConnectedOrConnecting());


    }

    @Override
    public void start() {
mainView.test();
    }
}
