package sk.denis.davidek.popularmoviesstage3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.main.MainPresenter;
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

    private Movie movie;
    private MovieDetailContract.Presenter movieDetailPresenter;

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ButterKnife.bind(this,fragmentView);
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
}
