package sk.denis.davidek.popularmoviesstage3.data;

/**
 * Created by denis on 18.11.2017.
 */

public class Constants {
    private static final String MOVIES_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String MOVIES_IMAGE_SIZE = "w500/";
    private static final String MOVIES_IMAGE_BACKGROUND_SIZE = "w780";

    private static final String QUERY_MOVIE_FILTER = "movie_filter";

    private static final String MOVIES_POPULAR = "/movie/popular";
    private static final String MOVIES_TOP_RATED = "/movie/top_rated";
    private static final String MOVIES_FAVORITES = "movie_favorites";

//DetailActivity
    private static final String MOVIE_DEFAULT = "movie_default";
    private static final String MOVIE_VIDEOORREVIEW = "moview_videoorreview";

    private static final String MOVIE_QUERY_TEXT = "/movie/";
    private static final String REVIEW_QUERY_TEXT = "/reviews";




    public static String getMoviesImageBaseUrl() {
        return MOVIES_IMAGE_BASE_URL;
    }

    public static String getMoviesImageSize() {
        return MOVIES_IMAGE_SIZE;
    }

    public static String getMoviesImageBackgroundSize() {
        return MOVIES_IMAGE_BACKGROUND_SIZE;
    }

    public static String getQueryMovieFilter() {
        return QUERY_MOVIE_FILTER;
    }

    public static String getMoviesPopular() {
        return MOVIES_POPULAR;
    }

    public static String getMoviesTopRated() {
        return MOVIES_TOP_RATED;
    }

    public static String getMoviesFavorites() {
        return MOVIES_FAVORITES;
    }

    public static String getMovieDefault() {
        return MOVIE_DEFAULT;
    }

    public static String getMovieVideoOrReview() {
        return MOVIE_VIDEOORREVIEW;
    }

    public static String getMovieQueryText() {
        return MOVIE_QUERY_TEXT;
    }

    public static String getReviewQueryText() {
        return REVIEW_QUERY_TEXT;
    }
}
