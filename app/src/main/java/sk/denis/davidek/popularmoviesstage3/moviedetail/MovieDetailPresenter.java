package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.data.contentprovider.MovieContract;

/**
 * Created by denis on 17.11.2017.
 */

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private final MovieDetailContract.View movieDetailView;

    public MovieDetailPresenter(MovieDetailContract.View view) {
        this.movieDetailView = view;
        view.setPresenter(this);
    }

    @Override
    public void start() {
        movieDetailView.prepareRecyclerView();
    }

    @Override
    public void distributeMovieDetails(Movie movie) {
        movieDetailView.displayMovieTitle(movie.getOriginalTitle());
        movieDetailView.displayMoviePlotSynopsis(movie.getPlotSynopsis());
        movieDetailView.displayUserRating(movie.getUserRating());
        movieDetailView.displayMoviePoster(movie.getPosterUrl());
        formatReleaseDate(movie.getReleaseDate());
    }

    @Override
    public void formatReleaseDate(String releaseDate) {
        SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyy-MM-dd");
        SimpleDateFormat simpleDateFormatOutput = new SimpleDateFormat("dd.MM.yyyy");

        Date date;
        try {
            date = simpleDateFormatInput.parse(releaseDate);
            movieDetailView.displayReleaseDate(simpleDateFormatOutput.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void prepareReviewsDataView() {
        movieDetailView.showReviewsDataView();
    }

    @Override
    public void prepareNoReviewsDataView(String message) {
        movieDetailView.hideReviewsDataView(message);
    }

    @Override
    public void prepareNoTrailersDataView(String message) {
        movieDetailView.hideTrailersDataView(message);
    }

    @Override
    public void prepareYoutubeMovieTrailer(String movieKey) {
        movieDetailView.watchYoutubeMovieTrailer(movieKey);

    }

    @Override
    public Uri downloadPosterFile(String moviePosterUrl, Movie movie, Context context) {
        Uri finalMoviePosterUri;
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/PopularMoviesDownloadedPosters");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        File testIfExists = new File(direct + File.separator + movie.getOriginalTitle() + ".jpg");

        if (!testIfExists.exists()) {

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //  Toast.makeText(context, "IMAGE DOWNLOADING", Toast.LENGTH_LONG).show();
            Uri downloadUri = Uri.parse(moviePosterUrl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir("/PopularMoviesDownloadedPosters", movie.getOriginalTitle() + ".jpg");

            manager.enqueue(request);

        }
        Uri moviePosterUri = Uri.parse(direct + File.separator + movie.getOriginalTitle() + ".jpg");
        finalMoviePosterUri = Uri.parse("file://" + moviePosterUri);
        return finalMoviePosterUri;
    }

    @Override
    public void insertFavoriteMovieIntoContentProvidersDatabase(Context context, Movie movie, Uri finalPosterUri, Uri finalBackgroundUri) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_URI, finalPosterUri.toString());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKGROUND_URI, finalBackgroundUri.toString());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movie.getPlotSynopsis());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getUserRating());
        contentValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());


        Uri uri = context.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            // Toast.makeText(context, uri.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Uri downloadBackgroundFile(String moviePosterUrl, Movie movie, Context context) {
        Uri finalMoviePosterUri;
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/PopularMoviesDownloadedPosters");

        if (!direct.exists()) {
            direct.mkdirs();
        }
        File testIfExists = new File(direct + File.separator + movie.getOriginalTitle() + "_background" + ".jpg");

        if (!testIfExists.exists()) {

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            //  Toast.makeText(context, "IMAGE DOWNLOADING", Toast.LENGTH_LONG).show();
            Uri downloadUri = Uri.parse(moviePosterUrl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir("/PopularMoviesDownloadedPosters", movie.getOriginalTitle() + "_background" + ".jpg");

            manager.enqueue(request);

        }
        Uri moviePosterUri = Uri.parse(direct + File.separator + movie.getOriginalTitle() + "_background" + ".jpg");
        finalMoviePosterUri = Uri.parse("file://" + moviePosterUri);
        return finalMoviePosterUri;
    }

    @Override
    public void prepareCollapsingToolbarLayout(Movie movie) {
        movieDetailView.setupCollapsingToolbarLayout(movie);
    }

    @Override
    public void getBackgroundMovieImage(Context context, Movie movie) {
        if (movie.getBackgroundUrl().startsWith("file://")) {

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(movie.getBackgroundUrl()));
                movieDetailView.displayMovieImageBackground(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            movieDetailView.displayMovieImageBackground(movie.getBackgroundUrl());
        }
    }

}
