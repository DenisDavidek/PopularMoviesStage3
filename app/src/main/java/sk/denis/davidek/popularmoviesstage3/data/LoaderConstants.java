package sk.denis.davidek.popularmoviesstage3.data;

/**
 * Created by denis on 21.11.2017.
 */

public  class LoaderConstants {
    private static  int REVIEWS_LOADER = 23;
    private static int TRAILERS_LOADER = 24;
    private static int MOVIES_FAVORITES_LOADER = 25;

    public static int getReviewsLoader() {
        return REVIEWS_LOADER;
    }

    public static int getTrailersLoader() {
        return TRAILERS_LOADER;
    }

    public static int getMoviesFavoritesLoader() {
        return MOVIES_FAVORITES_LOADER;
    }
}
