package manic.com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import manic.com.popularmovies.R;
import manic.com.popularmovies.model.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private List<Trailer> trailers;

    private final TrailerAdapterOnClickHandler mHandler;

    public interface TrailerAdapterOnClickHandler{
        void onClick(Trailer trailer);
    }

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler){
        mContext = context;
        mHandler = clickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindView(i);
    }

    @Override
    public long getItemId(int position) {
        if(trailers == null)
            return 0;
        else
            return trailers.size();
    }

    @Override
    public int getItemCount() {
        if(trailers == null)
            return 0;
        else
            return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView title;
        RelativeLayout parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            title = itemView.findViewById(R.id.trailer_title_tv);
            imageView = itemView.findViewById(R.id.trailer_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            if(trailers!=null)
                mHandler.onClick(trailers.get(adapterPosition));
        }

        public void bindView(int position){
            if(trailers != null) {
                String img = mContext.getString(R.string.youtube_img_url, trailers.get(position).getKey());
                title.setText(trailers.get(position).getName());
                if(imageView!=null)
                Picasso.get()
                        .load(mContext.getString(R.string.youtube_img_url, trailers.get(position).getKey()))
                        .fit()
                        .into(imageView);
            }
        }

    }

    public void setTrailerData(List<Trailer> trailerData){
        trailers = trailerData;
        notifyDataSetChanged();
    }
}
