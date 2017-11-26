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
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import sk.denis.davidek.popularmoviesstage3.MessageEvent;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailFragment;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener, MovieDetailFragment.OnFragmentInteractionListener {

    private String TAG = "PERMISSIONS TAG";

    public static boolean isTwoPane;

    private Movie selectedMovie;
    private LinearLayout linearLayout;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isAllPermissionsGranted();

        if (findViewById(R.id.rl_recipe_step_instruction) != null) {
            isTwoPane = true;
            Log.e("TWO PANE ACTIVITY ", String.valueOf(isTwoPane));
            if (savedInstanceState != null && savedInstanceState.containsKey("test") && selectedMovie != null) {

                selectedMovie = savedInstanceState.getParcelable("test");
                Toast.makeText(getApplicationContext(),"testSavedInstanceStatePodmienka",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(),"Wow",Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new MessageEvent("Hello everyone!"));
                }
            });
        }


/*        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            //code for portrait mode
            linearLayout = findViewById(R.id.ll_main_fragment);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,0,

                    1.0f
            );
            linearLayout.setLayoutParams(param);
            linearLayout.requestLayout();
        } else {
            //code for landscape mode
            linearLayout = findViewById(R.id.ll_main_fragment);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                 0,    LinearLayout.LayoutParams.MATCH_PARENT,

                    1.0f
            );
            linearLayout.setLayoutParams(param);
            linearLayout.requestLayout();

        }*/

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
            Toast.makeText(getApplicationContext(),"It is two pane UI " , Toast.LENGTH_SHORT).show();
            selectedMovie = movie;

/*
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                //code for portrait mode
                linearLayout = findViewById(R.id.ll_main_fragment);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,0,

                        0.5f
                );
                linearLayout.setLayoutParams(param);
                linearLayout.requestLayout();
            } else {
                //code for landscape mode
                linearLayout = findViewById(R.id.ll_main_fragment);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        0,    LinearLayout.LayoutParams.MATCH_PARENT,

                        0.5f
                );
                linearLayout.setLayoutParams(param);
                linearLayout.requestLayout();

            }
*/

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setSelectedMovie(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_media_container, movieDetailFragment)
                    .commit();
        } else{

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
        outState.putParcelable("test", selectedMovie);
        super.onSaveInstanceState(outState);
    }


}
