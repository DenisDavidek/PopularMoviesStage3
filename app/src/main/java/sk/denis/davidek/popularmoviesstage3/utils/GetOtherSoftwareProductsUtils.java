package sk.denis.davidek.popularmoviesstage3.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import javax.inject.Inject;

import sk.denis.davidek.popularmoviesstage3.App;

/**
 * Created by denis on 11.12.2017.
 */

public class GetOtherSoftwareProductsUtils {

    @Inject
    Context mContext;

    public GetOtherSoftwareProductsUtils() {

        App.getAppComponent().inject(this);
    }

    public void openGPlus(String profile) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus",
                    "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", profile);
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + profile)));
        }
    }
}
