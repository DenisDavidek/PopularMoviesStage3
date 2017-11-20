package sk.denis.davidek.popularmoviesstage3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Trailer;

/**
 * Created by denis on 20.11.2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersViewHolder> {

    private ArrayList<Trailer> trailers;
    @Inject
    Context mContext;


    public TrailersAdapter(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        App.getAppComponent().inject(this);
    }

    public class TrailersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tv_trailer_name)
        TextView trailerNameTextView;

         /*   @BindView(R.id.trailer_divider)
        View dividerView;*/

        @BindView(R.id.iv_movie_poster)
        ImageView buttonPlay;

        public TrailersViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //itemClickListener.onClick(position);
        }
    }


    @Override
    public TrailersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;

        View movieView = inflater.inflate(R.layout.list_trailers_item, parent, shouldAttachToRoot);
        TrailersViewHolder viewHolder = new TrailersViewHolder(movieView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailersViewHolder holder, int position) {

        String trailerName = trailers.get(position).getName();
        holder.trailerNameTextView.setText(trailerName);

        String key = trailers.get(position).getKey();
String finalUrl = "http://img.youtube.com/vi/" + key + "/0.jpg";

        Glide.with(mContext).load(finalUrl).into(holder.buttonPlay);

        // hide divider when it is the last item.
  /*      if (trailers.size() - 1 == position) {
            holder.dividerView.setVisibility(View.INVISIBLE);
        }*/

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}