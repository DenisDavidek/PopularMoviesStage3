package sk.denis.davidek.popularmoviesstage3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;
import sk.denis.davidek.popularmoviesstage3.moviedetail.MovieDetailContract;

/**
 * Created by denis on 20.11.2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private ArrayList<Trailer> trailers;
    @Inject
    Context mContext;

    private final MovieDetailContract.Presenter presenter;


    public TrailersAdapter(ArrayList<Trailer> trailers, MovieDetailContract.Presenter presenter) {
        this.trailers = trailers;
        this.presenter = presenter;
        App.getAppComponent().inject(this);
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_trailer_name)
        TextView trailerNameTextView;


        @BindView(R.id.iv_movie_poster)
        ImageView buttonPlay;

        public TrailersViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            presenter.prepareYoutubeMovieTrailer(trailers.get(position).getKey());
        }
    }


    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;

        View movieView = inflater.inflate(R.layout.list_trailers_item, parent, shouldAttachToRoot);

        return new TrailersViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {

        String trailerName = trailers.get(position).getName();
        holder.trailerNameTextView.setText(trailerName);

        String key = trailers.get(position).getKey();
        String finalUrl = "http://img.youtube.com/vi/" + key + "/0.jpg";

        Picasso.with(mContext)
                .load(finalUrl)
                .error(R.drawable.movie_error)
                .into(holder.buttonPlay);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}