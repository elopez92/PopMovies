package manic.com.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import manic.com.popularmovies.adapters.MovieAdapter;
import manic.com.popularmovies.adapters.SectionsPageAdapter;
import manic.com.popularmovies.database.AppDatabase;
import manic.com.popularmovies.database.AppExecutors;
import manic.com.popularmovies.fragments.ReviewsFragment;
import manic.com.popularmovies.fragments.TrailerFragment;
import manic.com.popularmovies.model.Favorite;
import manic.com.popularmovies.model.FavoriteViewModel;
import manic.com.popularmovies.model.FavoriteViewModelFactory;
import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.utils.ReleaseDateUtils;

public class MovieDetailsActivity extends AppCompatActivity implements TrailerFragment.OnFragmentInteractionListener,
        ReviewsFragment.OnFragmentInteractionListener {

    private TextView titleTV;
    private ImageView posterImgIV;
    private TextView releaseDateTV;
    private TextView plotTV;
    private TextView ratingTV;
    private Button favoriteButton;
    private ImageView favoriteStar;

    private Movie movie = null;
    private AppDatabase mDb;

    private ViewPager mViewPager;

    private LiveData<List<Favorite>> favorites;
    private Favorite favorite;
    private FavoriteViewModel viewModel;

    private final static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String imgSize = "w185";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        Intent intent = getIntent();

        if(intent != null && intent.hasExtra(getString(R.string.EXTRA_MOVIE_ID))) {
            movie = intent.getParcelableExtra(getString(R.string.EXTRA_MOVIE_ID));

            final MovieAdapter movieAdapter = new MovieAdapter(this, null);
            FavoriteViewModelFactory factory = new FavoriteViewModelFactory(mDb, movie.getId());
            viewModel = ViewModelProviders.of(this, factory).get(FavoriteViewModel.class);
            favorite = viewModel.getFavorite(movie.getId());
            favorites = viewModel.getFavoritesL();
            favorites.observe(this, new Observer<List<Favorite>>() {
                @Override
                public void onChanged(@Nullable List<Favorite> favorites) {
                    //viewModel.getFavorites().removeObservers(this);
                    favorite = viewModel.getFavorite(movie.getId());
                    if(favorite == null){
                        favoriteStar.setImageResource(R.drawable.non_favorite_24dp);
                        favoriteButton.setText(R.string.not_favorite);
                    } else {
                        favoriteStar.setImageResource(R.drawable.favorite_24dp);
                        favoriteButton.setText(R.string.favorite_remove);
                    }

                }
            });
            populateUI(movie);
        }
        else{
            finish();
        }

        setTitle(getString(R.string.detailTitle));
    }

    private void populateUI(Movie movie){
        String img = IMG_BASE_URL + imgSize + movie.getPosterPath();
        titleTV.setText(movie.getTitle());
        Picasso.get()
                .load(img)
                .into(posterImgIV);
        releaseDateTV.setText(ReleaseDateUtils.formatDate(movie.getReleaseDate()));
        plotTV.setText(movie.getOverview());
        ratingTV.setText(String.valueOf(movie.getVoteAverage()));
    }

    private void initViews(){
        titleTV = findViewById(R.id.title_tv);
        posterImgIV = findViewById(R.id.img_poster);
        releaseDateTV = findViewById(R.id.release_date_tv);
        plotTV = findViewById(R.id.summary_tv);
        ratingTV = findViewById(R.id.average_tv);

        mViewPager = findViewById(R.id.container);
        favoriteButton = findViewById(R.id.favorite_button);
        favoriteButton.setOnClickListener(favoriteClick);
        favoriteStar = findViewById(R.id.favorite_iv);

        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TrailerFragment(), getString(R.string.trailer_tab));
        adapter.addFragment(new ReviewsFragment(), getString(R.string.review_tab));
        viewPager.setAdapter(adapter);
    }

    public Movie getMovie(){
        return movie;
    }

    @Override
    public void onFragmentCreated(TrailerFragment fragment) {
        TrailerFragment.newInstance(movie);
        fragment.setMovie(movie);
    }

    @Override
    public void onFragmentCreated(ReviewsFragment fragment) {
        ReviewsFragment.newInstance(movie);
        fragment.setMovie(movie);
    }

    private View.OnClickListener favoriteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(favorite==null) {
                int id = movie.getId();
                String title = movie.getTitle();
                String releaseDate = movie.getReleaseDate();
                String moviePoster = movie.getPosterPath();
                String voteAverage = String.valueOf(movie.getVoteAverage());
                String plot = movie.getOverview();
                final Favorite favorite = new Favorite(id, title, releaseDate, moviePoster, voteAverage, plot);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mDb.favoriteDao().insertFavorie(favorite);
                    }
                });


            }else {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        Favorite favorite = viewModel.getFavorite(movie.getId());
                        mDb.favoriteDao().deleteFavorite(favorite);
                    }
                });
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        favorites.removeObservers(this);
    }
}
