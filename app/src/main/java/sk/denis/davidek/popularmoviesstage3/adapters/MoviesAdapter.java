package sk.denis.davidek.popularmoviesstage3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Movie;
import sk.denis.davidek.popularmoviesstage3.main.MainPresenter;

/**
 * Created by denis on 15.11.2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private ArrayList<Movie> movies;
    private MainPresenter presenter;
    private Context mContext;


    public MoviesAdapter(Context context, ArrayList<Movie> movies, MainPresenter presenter) {
        this.mContext = context;
        this.movies = movies;
        this.presenter = presenter;
    }

    public class MoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster)
        ImageView moviePosterImageView;

        public MoviesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            if (presenter != null) {
                presenter.onItemInteraction(movies.get(clickedPosition));
            }
        }
    }


    @Override
    public MoviesAdapter.MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;

        View movieView = inflater.inflate(R.layout.list_movies_item, parent, shouldAttachToRoot);

        return new MoviesViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(MoviesAdapter.MoviesViewHolder holder, int position) {

        String movieImageUrl = movies.get(position).getPosterUrl();
        Picasso.with(mContext)
                .load(movieImageUrl)
                .error(R.drawable.movie_error)
                .into(holder.moviePosterImageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
