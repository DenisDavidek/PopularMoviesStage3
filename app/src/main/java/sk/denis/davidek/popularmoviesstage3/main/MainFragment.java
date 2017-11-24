package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.adapters.MoviesAdapter;
import sk.denis.davidek.popularmoviesstage3.data.Constants;
import sk.denis.davidek.popularmoviesstage3.data.LoaderConstants;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailActivityPrava;
import sk.denis.davidek.popularmoviesstage3.utils.LayoutUtils;
import sk.denis.davidek.popularmoviesstage3.utils.NetworkUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements MainContract.View,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private OnFragmentInteractionListener mListener;

    private MainContract.Presenter mainPresenter;
    private MoviesAdapter moviesAdapter;

    @BindView(R.id.rv_movies)
    RecyclerView moviesRecyclerView;

    @BindView(R.id.tv_error_no_internet_connection)
    TextView errorInternetConnectionTextView;

    @BindView(R.id.tv_no_favorite_movies)
    TextView noFavoriteMoviesTextView;

    @BindView(R.id.progressBar)
    ProgressBar loadingProgressBar;

    @Inject
    SharedPreferences sharedPreferences;

    @Inject
    Context mContext;

    @BindString(R.string.pref_current_movies_filter)
    String moviesCurrentFilterKey;

    @BindString(R.string.movie_key)
    String selectedMovieKey;

    @BindString(R.string.state_main_activity_key)
    String mainActivityStateKey;

    private Parcelable state;
    private String MOVIES_CURRENT_FILTER;

    private GridLayoutManager layoutManager;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        this.setHasOptionsMenu(true);
        App.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, fragmentView);
        mainPresenter = new MainPresenter(this);

        MOVIES_CURRENT_FILTER = sharedPreferences.getString(moviesCurrentFilterKey, Constants.getMoviesTopRated());

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItemId = item.getItemId();
        switch (selectedItemId) {

            case R.id.action_show_popular_movies:
                Snackbar.make(moviesRecyclerView, getString(R.string.showing_popular_movies), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if (NetworkUtils.checkInternetConnection(mContext)) {
                    moviesRecyclerView.setAdapter(null);
                    getMoviesData(Constants.getMoviesPopular());
                    mainPresenter.setCurrentMovieFilterSetting(sharedPreferences, moviesCurrentFilterKey, Constants.getMoviesPopular());
                } else {
                    mainPresenter.prepareInternetErrorLoadingMessage();
                }
                break;

            case R.id.action_show_top_rated_movies:
                Snackbar.make(moviesRecyclerView, getString(R.string.showing_top_rated_movies), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                if (NetworkUtils.checkInternetConnection(mContext)) {
                    moviesRecyclerView.setAdapter(null);
                    getMoviesData(Constants.getMoviesTopRated());
                    mainPresenter.setCurrentMovieFilterSetting(sharedPreferences, moviesCurrentFilterKey, Constants.getMoviesTopRated());
                } else {
                    mainPresenter.prepareInternetErrorLoadingMessage();
                }
                break;

            case R.id.action_show_favorite_movies:
                Snackbar.make(moviesRecyclerView, getString(R.string.showing_favorite_movies), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                // if (NetworkUtils.checkInternetConnection(mContext)) {
                moviesRecyclerView.setAdapter(null);
                //  getMoviesData(Constants.getMoviesTopRated()); CURSOR SEM PRIDE
                getMoviesCursorLocalData();
                mainPresenter.setCurrentMovieFilterSetting(sharedPreferences, moviesCurrentFilterKey, Constants.getMoviesFavorites());
                //   } else {
                //    mainPresenter.prepareInternetErrorLoadingMessage();
                //   }
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void getMoviesData(String movieFilter) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(Constants.getQueryMovieFilter(), movieFilter);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        Loader<ArrayList<Movie>> getMoviesLoader = loaderManager.getLoader(LoaderConstants.getMovieLoader());

        if (getMoviesLoader == null) {
            loaderManager.initLoader(LoaderConstants.getMovieLoader(), argsBundle, this);
        } else
            loaderManager.restartLoader(LoaderConstants.getMovieLoader(), argsBundle, this);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainPresenter.start();
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        if (presenter != null)
            mainPresenter = presenter;
    }


    @Override
    public void prepareRecyclerView() {

        moviesRecyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getContext(), LayoutUtils.calculateNoOfColumns(getContext()));
        moviesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void showFavoriteMoviesData(ArrayList<Movie> movies) {
        moviesAdapter = new MoviesAdapter(mContext, movies, (MainPresenter) mainPresenter);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }


    @Override
    public void workWithInternetConnection(boolean hasInternetConnection) {
        if (hasInternetConnection) {

            if (MOVIES_CURRENT_FILTER.equals(Constants.getMoviesFavorites())) {

                getMoviesCursorLocalData();
            } else {
                getMoviesData(MOVIES_CURRENT_FILTER);
            }
        } else {

            if (MOVIES_CURRENT_FILTER.equals(Constants.getMoviesFavorites())) {
                getMoviesCursorLocalData();
            } else {

                mainPresenter.prepareInternetErrorLoadingMessage();
            }
        }
    }

    @Override
    public void showItemClickData(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivityPrava.class);
        intent.putExtra(selectedMovieKey, movie);
        startActivity(intent);
    }

    @Override
    public void showLoadingProgressBar(int load) {

        loadingProgressBar.setVisibility(load);

    }

    @Override
    public void hideLoadingProgressBar(int load) {
        loadingProgressBar.setVisibility(load);
    }

    @Override
    public void showInternetErrorLoadingMessage() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        errorInternetConnectionTextView.setVisibility(View.VISIBLE);
        if (noFavoriteMoviesTextView.getVisibility() == View.VISIBLE) {
            noFavoriteMoviesTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showMovieDataView() {

        moviesRecyclerView.setVisibility(View.VISIBLE);
        errorInternetConnectionTextView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoFavoriteMoviesMessage() {
        moviesRecyclerView.setVisibility(View.INVISIBLE);
        noFavoriteMoviesTextView.setVisibility(View.VISIBLE);
        if (errorInternetConnectionTextView.getVisibility() == View.VISIBLE) {
            errorInternetConnectionTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hideNoFavoriteMoviesMessage() {
        moviesRecyclerView.setVisibility(View.VISIBLE);
        noFavoriteMoviesTextView.setVisibility(View.INVISIBLE);

    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new MainLoader(mContext, args, this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        hideLoadingProgressBar(View.INVISIBLE);
        if (!data.isEmpty()) {
            mainPresenter.prepareMovieDataView();
            moviesAdapter = new MoviesAdapter(mContext, data, (MainPresenter) mainPresenter);
            moviesRecyclerView.setAdapter(moviesAdapter);
            layoutManager.onRestoreInstanceState(state);
        } else {
            mainPresenter.prepareInternetErrorLoadingMessage();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null)
            state = savedInstanceState.getParcelable(mainActivityStateKey);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }


    @Override
    public void onPause() {
        super.onPause();
        state = layoutManager.onSaveInstanceState();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        state = layoutManager.onSaveInstanceState();
        outState.putParcelable(mainActivityStateKey, state);
        super.onSaveInstanceState(outState);

    }

    public void getMoviesCursorLocalData() {

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<Cursor> getCusorLoader = loaderManager.getLoader(LoaderConstants.getMovieCursorGetLoader());

        if (getCusorLoader == null) {
            loaderManager.initLoader(LoaderConstants.getMovieCursorGetLoader(), null, new CallbackLocalQuery());
        } else
            loaderManager.restartLoader(LoaderConstants.getMovieCursorGetLoader(), null, new CallbackLocalQuery());

    }

    private Cursor mCursorLocalMovieData;

    private class CallbackLocalQuery implements LoaderManager.LoaderCallbacks<Cursor> {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new MoviesCursorLocalLoader(mContext);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mCursorLocalMovieData = data;
            getFavoriteMovies();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }

    public void getFavoriteMovies() {
        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        android.support.v4.content.Loader<ArrayList<Movie>> getFavoriteMoviesLoader = loaderManager.getLoader(LoaderConstants.getMoviesFavoritesLoader());

        if (getFavoriteMoviesLoader == null) {
            loaderManager.initLoader(LoaderConstants.getMoviesFavoritesLoader(), null, new CallbackFavoriteMovies(mCursorLocalMovieData, this));
        } else
            loaderManager.restartLoader(LoaderConstants.getMoviesFavoritesLoader(), null, new CallbackFavoriteMovies(mCursorLocalMovieData, this));
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
