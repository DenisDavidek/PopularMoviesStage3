package sk.denis.davidek.popularmoviesstage3.dagger.components;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.AppModule;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.ContextModule;
import sk.denis.davidek.popularmoviesstage3.main.MainActivity;

/**
 * Created by denis on 15.11.2017.
 */

@Component(modules = {AppModule.class, ContextModule.class})
@Singleton
public interface AppComponent {

    Context getContext();

    void inject(MainActivity mainActivity);
}
