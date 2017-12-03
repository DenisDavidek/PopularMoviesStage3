package sk.denis.davidek.popularmoviesstage3.utils;

import android.content.Context;
import android.provider.Settings;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.App;

/**
 * Created by denis on 01.12.2017.
 */

public class AdUtils {

    @Inject
    Context context;

    public AdUtils() {
        App.getAppComponent().inject(this);
    }

    public boolean isTestDevice() {
        String testLabSetting = Settings.System.getString(context.getContentResolver(), "firebase.test.lab");
        return "true".equals(testLabSetting);
    }

    public void loadAd(AdView mAdView){
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
