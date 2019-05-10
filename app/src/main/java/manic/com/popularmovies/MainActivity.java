package manic.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.utils.JsonUtils;
import manic.com.popularmovies.utils.MovieAdapter;
import manic.com.popularmovies.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private final static String SORT_BY_POPULAR = "popular";
    private final static String SORT_BY_TOPRATED = "top_rated";

    private String sortBy;

    private final int NUM_COLUMNS = 2;

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        mRecyclerView = findViewById(R.id.rv);
        mLoadingIndicator = findViewById(R.id.pb);

        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, NUM_COLUMNS);
        mRecyclerView.setLayoutManager(layoutManager);

        loadMovieData(SORT_BY_POPULAR);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(mContext, MovieDetailsActivity.class);
        intent.putExtra("movie", movie);
        startActivity(intent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            URL movieRequestUrl = NetworkUtils.buildUrl(strings[0]);

            try{
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                List<Movie> movies = JsonUtils.parseMovieJson(jsonMovieResponse);
                return movies;
            }catch (IOException ioe){
                ioe.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null){
                showMovieDataView();
                switch (sortBy){
                    case SORT_BY_POPULAR:
                        setTitle(getString(R.string.pop));
                        break;
                    case SORT_BY_TOPRATED:
                        setTitle(getString(R.string.top_rated));
                }
                mMovieAdapter.setMovieData(movieData);
            }
        }
    }

    private void loadMovieData(String sortBy){
        showMovieDataView();
        this.sortBy = sortBy;
        new FetchMovieTask().execute(sortBy);
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
        switch (menuItemSelected){
            case R.id.sortbypop:
                loadMovieData(SORT_BY_POPULAR);
                break;
            case R.id.sortybyrating:
                loadMovieData(SORT_BY_TOPRATED);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
