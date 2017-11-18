package sk.denis.davidek.popularmoviesstage3.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import sk.denis.davidek.popularmoviesstage3.App;
import sk.denis.davidek.popularmoviesstage3.R;
import sk.denis.davidek.popularmoviesstage3.data.Review;

/**
 * Created by denis on 18.11.2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    private ArrayList<Review> reviews;
    @Inject
    Context mContext;

    public ReviewsAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
        App.getAppComponent().inject(this);
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author)
        TextView reviewAuthorTextView;
        @BindView(R.id.tv_review)
        TextView reviewTextView;
        @BindView(R.id.review_divider)
        View dividerView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToRoot = false;

        View movieView = inflater.inflate(R.layout.list_reviews_item, parent, shouldAttachToRoot);
        ReviewsViewHolder viewHolder = new ReviewsViewHolder(movieView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {

        String reviewAuthor = reviews.get(position).getAuthor();
        holder.reviewAuthorTextView.setText(reviewAuthor);

        String reviewText = reviews.get(position).getContent();
        holder.reviewTextView.setText(reviewText);

        if (reviews.size() - 1 == position) {
            holder.dividerView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}