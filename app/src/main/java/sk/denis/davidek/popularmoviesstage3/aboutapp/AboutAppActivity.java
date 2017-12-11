package sk.denis.davidek.popularmoviesstage3.aboutapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.R;

public class AboutAppActivity extends AppCompatActivity implements AboutAppContract.View {

    private AboutAppContract.Presenter aboutAppPresenter;

    @BindView(R.id.iv_tmdb_logo)
    ImageView tmdbLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        aboutAppPresenter = new AboutAppPresenter(this);
        ButterKnife.bind(this);
        tmdbLogoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutAppPresenter.loadUrl();
            }
        });
    }

    @Override
    public void setPresenter(AboutAppContract.Presenter presenter) {
        aboutAppPresenter = presenter;
    }

    @Override
    public void openTMDbWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
