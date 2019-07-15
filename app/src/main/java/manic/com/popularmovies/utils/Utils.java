package manic.com.popularmovies.utils;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import manic.com.popularmovies.model.Favorite;
import manic.com.popularmovies.model.Movie;

public class Utils {

    public static DividerItemDecoration getDividerItemDecoration(Context context, RecyclerView.LayoutManager mLayoutManager){
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                ((LinearLayoutManager) mLayoutManager).getOrientation());
        return dividerItemDecoration;
    }

    public static List<Movie> favoritesToMovies(List<Favorite> favorites){
        List<Movie> movies = new ArrayList<>();
        for(Favorite favorite : favorites){
            int id = favorite.getId();
            String title = favorite.getTitle();
            String releaseDate = favorite.getReleaseDate();
            String moviePoster = favorite.getMoviePoster();
            String plot = favorite.getPlot();
            String voterAverage = favorite.getVoteAverage();

            Movie movie = new Movie(id, title, releaseDate, moviePoster, Double.parseDouble(voterAverage), plot);
            movies.add(movie);
        }
        return movies;
    }

}
