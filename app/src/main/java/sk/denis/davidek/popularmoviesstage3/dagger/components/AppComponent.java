package sk.denis.davidek.popularmoviesstage3.dagger.components;

import dagger.Component;
import sk.denis.davidek.popularmoviesstage3.main.MainActivity;
import sk.denis.davidek.popularmoviesstage3.dagger.modules.AppModule;

/**
 * Created by denis on 15.11.2017.
 */

@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(MainActivity mainActivity);
}
