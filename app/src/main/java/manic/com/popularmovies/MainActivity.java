package manic.com.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import manic.com.popularmovies.database.AppDatabase;
import manic.com.popularmovies.database.AppExecutors;
import manic.com.popularmovies.model.Favorite;
import manic.com.popularmovies.model.FavoriteViewModel;
import manic.com.popularmovies.model.FavoriteViewModelFactory;
import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.model.MovieViewModel;
import manic.com.popularmovies.model.MovieViewModelFactory;
import manic.com.popularmovies.utils.JsonUtils;
import manic.com.popularmovies.adapters.MovieAdapter;
import manic.com.popularmovies.utils.NetworkUtils;
import manic.com.popularmovies.utils.Utils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private final String SORT_BY_POPULAR = "popular";
    private final String SORT_BY_TOPRATED = "top_rated";
    private final String SORT_BY_FAVORITE = "favorite";

    private String sortBy = SORT_BY_POPULAR;

    private final int NUM_COLUMNS = 2;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private MutableLiveData<List<Movie>> movies;
    private LiveData<List<Favorite>> favorites;
    private ExecutorService executor;

    AppDatabase mDb;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mDb = AppDatabase.getInstance(mContext);
        executor = AppExecutors.getInstance().execService();

        mRecyclerView = findViewById(R.id.rv);
        mLoadingIndicator = findViewById(R.id.pb);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        int width = size.x;
        int numColumns = width / 600;

        //AutoFitGridLayoutManager layoutManager = new AutoFitGridLayoutManager(this, )
        GridLayoutManager layoutManager = new GridLayoutManager(this, numColumns);
        mRecyclerView.setLayoutManager(layoutManager);

        setupSharedPreferences();
        setupMovieViewMode();

        //retrieveMovies();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(mContext, MovieDetailsActivity.class);
        intent.putExtra(getString(R.string.EXTRA_MOVIE_ID), movie);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String string = sharedPreferences.getString(key, getString(R.string.pref_sort_by_default));
            switch (sortBy){
                case SORT_BY_POPULAR:
                    setTitle(getString(R.string.pop));
                    break;
                case SORT_BY_TOPRATED:
                    setTitle(getString(R.string.top_rated));
            }
            loadMovieData(string);

    }


    private void loadMovieData(final String sortBy){
        showMovieDataView();
        if(!this.sortBy.equals(sortBy))
            this.sortBy = sortBy;
            if(!sortBy.equals(getString(R.string.pref_sort_by_favorite)))
                getMovies(sortBy);

    }

    private void showMovieDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();
        SharedPreferences.Editor editor;
        editor  = PreferenceManager.getDefaultSharedPreferences(this).edit();
        switch (menuItemSelected){
            case R.id.sort_by_popular:
                editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_popular_value));
                editor.apply();
                break;
            case R.id.sort_by_top:
                editor.putString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_top_value));
                editor.apply();
                break;
            case R.id.favorites:
                setTitle(getString(R.string.favorite_label));
                setupFavoriteViewModel();
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Favorite> favorites = AppDatabase.getInstance(mContext).favoriteDao().loadFavorites();
                        getFavorites(favorites);
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFavoriteViewModel() {
        FavoriteViewModelFactory factory = new FavoriteViewModelFactory(mDb, 0);
        final FavoriteViewModel viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel.class);
        favorites = viewModel.getFavoritesL();
        favorites.observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable List<Favorite> favorites) {
                List<Movie> movies = Utils.favoritesToMovies(favorites);
                mMovieAdapter.setMovieData(movies);
            }
        });
    }

    private void setupMovieViewMode(){
        if(sortBy.equals(getString(R.string.pref_sort_by_favorite))) {
            sortBy = SORT_BY_POPULAR;
            setTitle(getString(R.string.favorite_label));
        }
        MovieViewModelFactory factory = new MovieViewModelFactory(mMovieAdapter, sortBy);
        MovieViewModel viewModel = ViewModelProviders.of(this, factory).get(MovieViewModel.class);
        movies = viewModel.getMovies();
        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                switch (sortBy){
                    case SORT_BY_POPULAR:
                        setTitle(getString(R.string.pref_sort_by_popular_label));
                        break;
                    case SORT_BY_TOPRATED:
                        setTitle(getString(R.string.pref_sort_by_top_label));
                        break;
                    case SORT_BY_FAVORITE:
                        setTitle(getString(R.string.pref_sort_by_favorite));
                        break;
                }
                mMovieAdapter.setMovieData(movies);
            }
        });
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.sortBy = sharedPref.getString(getString(R.string.pref_sort_by_key), getString(R.string.pref_sort_by_default));

        //loadMovieData(sortBy);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    private void getMovies(final String sortBy){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                URL movieRequestUrl = NetworkUtils.buildMovieUrl(sortBy);
                try{
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                    List<Movie> list = JsonUtils.parseMovieJson(jsonMovieResponse);
                    movies.postValue(null);
                    movies.postValue(list);
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        });
    }

    private void getFavorites(final List<Favorite> favoriteslist){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Movie> mList = new ArrayList<>();
                for(Favorite favorite : favoriteslist) {
                    int id = favorite.getId();
                    URL movieRequestUrl = NetworkUtils.buildMovieFindByIdUrl(String.valueOf(id));
                    String jsonMovieResponse;
                    try {
                        jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                        Movie movie = JsonUtils.parseMovieByIdJson(jsonMovieResponse);
                        mList.add(movie);
                        movies.postValue(mList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
