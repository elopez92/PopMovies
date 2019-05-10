package manic.com.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.utils.ReleaseDateUtils;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;

    private TextView titleTV;
    private ImageView posterImgIV;
    private TextView releaseDateTV;
    private TextView plotTV;
    private TextView ratingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        titleTV = findViewById(R.id.title_tv);
        posterImgIV = findViewById(R.id.img_poster);
        releaseDateTV = findViewById(R.id.release_date_tv);
        plotTV = findViewById(R.id.summary_tv);
        ratingTV = findViewById(R.id.average_tv);

        if(getIntent().hasExtra("movie")) {
            movie = getIntent().getExtras().getParcelable("movie");
        }

        if(movie!=null) {
            String img = movie.getMoviePoster();
            titleTV.setText(movie.getTitle());
            Picasso.get()
                    .load(img)
                    .into(posterImgIV);
            releaseDateTV.setText(ReleaseDateUtils.formatDate(movie.getReleaseDate()));
            plotTV.setText(movie.getPlot());
            ratingTV.setText(movie.getVoteAverage());
        }
        setTitle(getString(R.string.detailTitle));
    }
}
