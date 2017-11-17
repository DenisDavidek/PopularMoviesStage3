package sk.denis.davidek.popularmoviesstage3.dagger.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by denis on 15.11.2017.
 */
@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    Context providesContext() {
        return context;
    }

    @Provides
    SharedPreferences providesSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
