package sk.denis.davidek.popularmoviesstage3.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindString;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.MessageEvent;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, MovieDetailFragment.OnFragmentInteractionListener {

    private String TAG = "PERMISSIONS TAG";

    public static boolean isTwoPane;

    @BindString(R.string.movie_key)
    String selectedMovieKey;

    private Movie selectedMovie;


    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAllPermissionsGranted();
        ButterKnife.bind(this);
        if (findViewById(R.id.rl_recipe_step_instruction) != null) {
            isTwoPane = true;
            Log.e("TWO PANE ACTIVITY ", String.valueOf(isTwoPane));
            if (savedInstanceState != null && savedInstanceState.containsKey(selectedMovieKey) ) {

                selectedMovie = savedInstanceState.getParcelable(selectedMovieKey);
                MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
                movieDetailFragment.setSelectedMovie(selectedMovie);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_step_media_container, movieDetailFragment)
                        .commit();
            }

            floatingActionButton = findViewById(R.id.fab);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                }
            });
        }



    }


    public boolean isAllPermissionsGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.
                    permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted > 23");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]
                        {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            Log.v(TAG, "Permission is granted < 23");
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);

        } else {
            isAllPermissionsGranted();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(Movie movie) {
        if (isTwoPane) {

            selectedMovie = movie;

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setSelectedMovie(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_media_container, movieDetailFragment)
                    .commit();
        } else {

        }
    }

    @Override
    public void onClick(boolean value) {
        if (value) {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_white_36dp));

        } else {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_white_36dp));

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (selectedMovie != null)
        outState.putParcelable(selectedMovieKey, selectedMovie);
        super.onSaveInstanceState(outState);
    }


}
