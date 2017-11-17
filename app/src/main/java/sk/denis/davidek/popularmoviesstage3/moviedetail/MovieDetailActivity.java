package sk.denis.davidek.popularmoviesstage3.moviedetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.Movie;
import sk.denis.davidek.popularmoviesstage3.R;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.backdrop_image)
    ImageView backdropImageView;

    @BindString(R.string.movie_key)
    String selectedMovieKey;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupToolbarBackIcon();

        ButterKnife.bind(this);
        setupCollapsingToolbarLayout();


    }

    private void setupToolbarBackIcon() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupCollapsingToolbarLayout() {
        collapsingToolbarLayout.setTitle(getMovieTitle());
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
        setTitle("");
        backdropImageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.crash_bandicoot_n_sane_trilogy_2));

    }

    private String getMovieTitle() {

        String returnedTitle;
        Intent intent = getIntent();
        if (intent.hasExtra(selectedMovieKey)) {
            movie = intent.getParcelableExtra(selectedMovieKey);
            returnedTitle = movie.getOriginalTitle();
        } else
            returnedTitle = "";

        return returnedTitle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
