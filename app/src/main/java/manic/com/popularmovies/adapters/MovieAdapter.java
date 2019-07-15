package manic.com.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import manic.com.popularmovies.R;
import manic.com.popularmovies.model.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> implements Filterable {

    private Context mContext;
    private List<Movie> movies;
    private List<Movie> masterMovieList;
    private final static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String imgSize = "w185";

    private MovieFilter movieFilter;

    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(Context context, MovieAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;

        getFilter();
    }

    @Override
    public Filter getFilter() {
        if(movieFilter == null){
            movieFilter = new MovieFilter();
        }

        return movieFilter;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recyclerview_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String img = IMG_BASE_URL + imgSize + movies.get(position).getPosterPath();
        Picasso.get()
                .load(img)
                .fit()
                .into(holder.imageView);
    }

    @Override
    public long getItemId(int position) {
        if(movies == null)
            return 0;
        else
            return movies.size();
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else
            return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(movies.get(adapterPosition));
        }
    }

    public void setMovieData(List<Movie> movieData){
        movies = null;
        movies =  movieData;
        masterMovieList = movieData;
        notifyDataSetChanged();
    }

    public List<Movie> getMovieData(){
        return movies;
    }

    public class MovieFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if(constraint!=null && constraint.length()>0) {
                List<Movie> tempList = new ArrayList<>();

                for(Movie movie : masterMovieList){
                    if(movie.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())){
                        tempList.add(movie);
                    }
                }
                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else{
                filterResults.count = masterMovieList.size();
                filterResults.values = masterMovieList;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            movies = (List<Movie>) results.values;
            notifyDataSetChanged();
        }
    }
}
