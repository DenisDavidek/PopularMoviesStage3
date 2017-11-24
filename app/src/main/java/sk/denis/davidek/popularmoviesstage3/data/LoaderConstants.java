package sk.denis.davidek.popularmoviesstage3.data;

/**
 * Created by denis on 21.11.2017.
 */

public class LoaderConstants {

    private static int MOVIE_GET_LOADER = 22;

    private static int REVIEWS_LOADER = 23;
    private static int TRAILERS_LOADER = 24;
    private static int MOVIES_FAVORITES_LOADER = 25;

    private static int MOVIE_CURSOR_GET_LOADER = 41;

    private int MOVIE_FAVORITES_GET_LOADER = 77;

    public static int getMovieLoader() {
        return MOVIE_GET_LOADER;
    }

    public static int getReviewsLoader() {
        return REVIEWS_LOADER;
    }

    public static int getTrailersLoader() {
        return TRAILERS_LOADER;
    }

    public static int getMoviesFavoritesLoader() {
        return MOVIES_FAVORITES_LOADER;
    }

    public static int getMovieCursorGetLoader() {
        return MOVIE_CURSOR_GET_LOADER;
    }

    public int getMovieFavoritesGetLoader() {
        return MOVIE_FAVORITES_GET_LOADER;
    }
}
