package sk.denis.davidek.popularmoviesstage3.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import sk.denis.davidek.popularmoviesstage3.Movie;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.adapters.MoviesAdapter;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailActivity;
import sk.denis.davidek.popularmoviesstage3.utils.LayoutUtils;


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

    private static final String QUERY_MOVIE_FILTER = "movie_filter";
    private int MOVIE_GET_LOADER = 22;
    private boolean isConnectedToInternet;

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
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, fragmentView);
        mainPresenter = new MainPresenter(this);

        return fragmentView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private static final String MOVIES_POPULAR = "/movie/popular";
    private static final String MOVIES_TOP_RATED = "/movie/top_rated";
    private static final String MOVIES_FAVORITES = "movie_favorites";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItemId = item.getItemId();
        switch (selectedItemId) {

            case R.id.action_show_popular_movies:
                Snackbar.make(moviesRecyclerView, getString(R.string.showing_popular_movies), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (isConnectedToInternet) {
                    isConnectedToInternet = false;
                    moviesRecyclerView.setAdapter(null);
                    getMoviesData(MOVIES_POPULAR);
                    mainPresenter.setCurrentMovieFilterSetting(sharedPreferences, moviesCurrentFilterKey, MOVIES_POPULAR);
                } else {
                    mainPresenter.prepareErrorLoadingMessage();
                }

        }


        return super.onOptionsItemSelected(item);
    }

    private void getMoviesData(String movieFilter) {

        Bundle argsBundle = new Bundle();
        argsBundle.putString(QUERY_MOVIE_FILTER, movieFilter);

        android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
        Loader<ArrayList<Movie>> getMoviesLoader = loaderManager.getLoader(MOVIE_GET_LOADER);

        if (getMoviesLoader == null) {
            loaderManager.initLoader(MOVIE_GET_LOADER, argsBundle, this);
        } else
            loaderManager.restartLoader(MOVIE_GET_LOADER, argsBundle, this);

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
        //TODO CHECK IF PRESENER JE NULOVY
        mainPresenter = presenter;
    }


    @Override
    public void prepareRecyclerView() {

        moviesRecyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), LayoutUtils.calculateNoOfColumns(getContext()));
        moviesRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void setMovieFilter() {

    }

    @Override
    public void workWithInternetConnection(boolean hasInternetConnection) {
        if (hasInternetConnection) {
            isConnectedToInternet = true;
            getMoviesData(MOVIES_POPULAR);
        } else {
            isConnectedToInternet = false;
            mainPresenter.prepareErrorLoadingMessage();
        }
    }

    @Override
    public void showItemClickData(Movie movie) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(selectedMovieKey, movie);
        startActivity(intent);
    }

    @Override
    public void showLoadingProgressBar(int load) {

        loadingProgressBar.setVisibility(load);

    }

    @Override
    public void showErrorLoadingMessage() {
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
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new MainLoader(getContext(), args, this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        loadingProgressBar.setVisibility(View.INVISIBLE);
        if (!data.isEmpty()) {
            mainPresenter.prepareMovieDataView();
            moviesAdapter = new MoviesAdapter(mContext, data, (MainPresenter) mainPresenter);
            moviesRecyclerView.setAdapter(moviesAdapter);
        } else {
            mainPresenter.prepareErrorLoadingMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

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
