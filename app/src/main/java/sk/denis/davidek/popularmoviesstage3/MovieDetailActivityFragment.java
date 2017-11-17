package sk.denis.davidek.popularmoviesstage3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailContract;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailPresenter;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements MovieDetailContract.View {

    @BindString(R.string.movie_key)
    String selectedMovieKey;

    @BindView(R.id.tv_movie_title)
    TextView movieTitleTextView;
/*
    @BindView(R.id.tv_no_favorite_movies)
    TextView movieTitleTextaView;*/

    @BindView(R.id.tv_movie_user_rating)
    TextView movieUserRatingTextView;

    @BindView(R.id.tv_movie_release_date)
    TextView movieReleaseDateTextView;

    @BindView(R.id.iv_movie_poster)
    ImageView moviePosterImageView;

    private Movie movie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    @Inject
    Context context;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this, fragmentView);
        App.getAppComponent().inject(this);
        movieDetailPresenter = new MovieDetailPresenter(this);
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(selectedMovieKey)) {
            movie = intent.getParcelableExtra(selectedMovieKey);
            movieDetailPresenter.distributeMovieDetails(movie);


        }
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        movieDetailPresenter.start();
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        movieDetailPresenter = presenter;

    }

    @Override
    public void displayMovieTitle(String movieTitle) {
        movieTitleTextView.setText(movieTitle);
    }

    @Override
    public void displayMoviePlotSynopsis(String plotSynopsis) {

    }

    @Override
    public void displayUserRating(double userRating) {
        movieUserRatingTextView.setText(String.valueOf(userRating));
    }

    @Override
    public void displayReleaseDate(String releaseDate) {
        movieReleaseDateTextView.setText(releaseDate);
    }

    @Override
    public void displayMoviePoster(String posterUrl) {
        Picasso.with(context).load(posterUrl).into(moviePosterImageView);
    }
}
