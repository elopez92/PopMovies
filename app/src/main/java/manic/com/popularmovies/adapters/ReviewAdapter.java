package manic.com.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import manic.com.popularmovies.R;
import manic.com.popularmovies.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review> reviews;
    private final ReviewAdapterOnClickHandler mHandler;

    public ReviewAdapter(Context context, ReviewAdapter.ReviewAdapterOnClickHandler clickHandler){
        mHandler = clickHandler;
    }

    public interface ReviewAdapterOnClickHandler{
        void onClick(Review review);
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder viewHolder, int i) {
        if(reviews!=null) {
            viewHolder.reviewTitle.setText(reviews.get(i).getAuthor());
            viewHolder.reviewContent.setText(reviews.get(i).getContent());
        }
    }

    @Override
    public int getItemCount() {
        if(reviews == null){
            return 0;
        }else
            return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView reviewTitle;
        TextView reviewContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewTitle = itemView.findViewById(R.id.review_title_tv);
            reviewContent = itemView.findViewById(R.id.review_description_tv);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mHandler.onClick(reviews.get(adapterPosition));
        }
    }

    public void setReviews(List<Review> reviews){
        this.reviews = reviews;
        notifyDataSetChanged();
    }
}
