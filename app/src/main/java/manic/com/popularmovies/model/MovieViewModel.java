package manic.com.popularmovies.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import manic.com.popularmovies.adapters.MovieAdapter;
import manic.com.popularmovies.database.AppDatabase;
import manic.com.popularmovies.database.AppExecutors;
import manic.com.popularmovies.utils.JsonUtils;
import manic.com.popularmovies.utils.NetworkUtils;

public class MovieViewModel extends ViewModel {

    public MutableLiveData<List<Movie>> movies;

    protected MovieViewModel(final MovieAdapter mAdapter, final String sortBy) {
        ExecutorService executor = AppExecutors.getInstance().execService();
        movies = new MutableLiveData<>();
        final List<Movie>[] movieList = new List[]{new ArrayList<>()};
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL trailerRequestUrl = NetworkUtils.buildMovieUrl(sortBy);
                        String jsonTrailerResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                        movieList[0] = JsonUtils.parseMovieJson(jsonTrailerResponse);
                        movies.postValue(movieList[0]);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            });
        executor.shutdown();
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch(InterruptedException ie){
            ie.printStackTrace();
        }
        mAdapter.setMovieData(movieList[0]);
    }


    public void setMovies(MutableLiveData<List<Movie>> movies){
        this.movies =  movies;
    }

    public MutableLiveData<List<Movie>> getMovies(){
        return movies;
    }
}
