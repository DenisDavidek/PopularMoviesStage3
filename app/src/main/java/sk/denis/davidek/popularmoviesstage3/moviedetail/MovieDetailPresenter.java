package sk.denis.davidek.popularmoviesstage3.moviedetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sk.denis.davidek.popularmoviesstage3.data.Movie;

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
    public void prepareNoReviewsDataView() {
        movieDetailView.hideReviewsDataView();
    }
}
