package sk.denis.davidek.popularmoviesstage3;

import android.app.Application;

import sk.denis.davidek.popularmoviesstage3.dagger.components.AppComponent;
import sk.denis.davidek.popularmoviesstage3.dagger.components.DaggerAppComponent;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.AppModule;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.ContextModule;

/**
 * Created by denis on 15.11.2017.
 */

public class App extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .contextModule(new ContextModule(getApplicationContext()))
                .build();

    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

}
