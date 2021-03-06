package sk.denis.davidek.popularmoviesstage3.dagger.components;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import sk.denis.davidek.popularmoviesstage3.adapters.ReviewsAdapter;
import sk.denis.davidek.popularmoviesstage3.adapters.TrailersAdapter;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.AppModule;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.ContextModule;
import sk.denis.davidek.popularmoviesstage3.main.CallbackFavoriteMovies;
import sk.denis.davidek.popularmoviesstage3.main.MainActivity;
import sk.denis.davidek.popularmoviesstage3.main.MainFragment;
import sk.denis.davidek.popularmoviesstage3.main.MainPresenter;
import sk.denis.davidek.popularmoviesstage3.moviedetail.CallbackVideos;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailActivity;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailFragment;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailPresenter;
import sk.denis.davidek.popularmoviesstage3.utils.AdUtils;
import sk.denis.davidek.popularmoviesstage3.utils.GetOtherSoftwareProductsUtils;

/**
 * Created by denis on 15.11.2017.
 */

@Component(modules = {AppModule.class, ContextModule.class})
@Singleton
public interface AppComponent {

    Context getContext();

    SharedPreferences getSharedPreferences();

    void inject(MainActivity mainActivity);

    void inject(MainFragment mainFragment);

    void inject(MainPresenter mainPresenter);

    void inject(MovieDetailFragment movieDetailFragment);

    void inject(ReviewsAdapter reviewsAdapter);

    void inject(TrailersAdapter trailersAdapter);

    void inject(MovieDetailActivity movieDetailActivity);

    void inject(CallbackFavoriteMovies callbackFavoriteMovies);

    void inject(CallbackVideos callbackVideos);

    void inject(MovieDetailPresenter movieDetailPresenter);

    void inject(AdUtils adUtils);

    void inject(GetOtherSoftwareProductsUtils getOtherSoftwareProductsUtils);
}
