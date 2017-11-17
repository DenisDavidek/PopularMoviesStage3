package sk.denis.davidek.popularmoviesstage3.moviedetail;

import sk.denis.davidek.popularmoviesstage3.Movie;

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

    }

    @Override
    public void distributeMovieDetails(Movie movie) {
        movieDetailView.displayMovieTitle(movie.getOriginalTitle());
    }
}
