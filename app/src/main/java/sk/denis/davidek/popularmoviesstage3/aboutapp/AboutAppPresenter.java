package sk.denis.davidek.popularmoviesstage3.aboutapp;

/**
 * Created by denis on 11.12.2017.
 */

public class AboutAppPresenter implements AboutAppContract.Presenter {

    private AboutAppContract.View aboutAppView;
    private final String tmdbUrlPage = "https://www.themoviedb.org/";

    public AboutAppPresenter(AboutAppContract.View view) {
        this.aboutAppView = view;
        aboutAppView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadUrl() {
        aboutAppView.openTMDbWebPage(tmdbUrlPage);
    }

    @Override
    public void prepareGetOtherProductsPage(String url) {
        aboutAppView.openGetOtherProductsPage(url);
    }
}
