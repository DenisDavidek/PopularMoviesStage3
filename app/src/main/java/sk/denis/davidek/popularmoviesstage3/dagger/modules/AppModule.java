package sk.denis.davidek.popularmoviesstage3.dagger.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by denis on 15.11.2017.
 */

@Module
public class AppModule {

    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }
/*
@Provides
    @Singleton
    SharedPreferences providesSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(application);
}

@Provides
    @Singleton
    SharedPreferences.Editor providesSharedPreferencesEditor(){
        return  providesSharedPreferences().edit();
}*/
}
