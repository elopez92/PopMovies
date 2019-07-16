package manic.com.popularmovies.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import manic.com.popularmovies.MainActivity;
import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.model.MovieResults;
import manic.com.popularmovies.model.Review;
import manic.com.popularmovies.model.Trailer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsonUtils {

    private final static String MOVIE_DB_URL = "http://api.themoviedb.org/3/";
    private static final String  APIKEY = "";

    //JSON Strings
    private static final String RESULTS = "results";
    private static final String TITLE = "original_title";
    private static final String PLOT = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String RATING = "vote_average";
    private static final String POSTER = "poster_path";


    public static List<Movie> parseMovieJson(String json){
        List<Movie> mList = new ArrayList<>();
        try{
            JSONObject movies = new JSONObject(json);
            JSONArray movieInfo =  movies.optJSONArray(RESULTS);

            if(movieInfo.length()>0){
                for(int i = 0; i < movieInfo.length(); i++) {
                    JSONObject movieObj = movieInfo.getJSONObject(i);
                    int id = movieObj.optInt("id");
                    String title = movieObj.optString(TITLE);
                    String releaseDate = movieObj.optString(RELEASE_DATE);
                    String moviePoster = movieObj.optString(POSTER);
                    String voteAverage = movieObj.optString(RATING);
                    String plot = movieObj.optString(PLOT);

                    Movie movie = new Movie(id, title, releaseDate, moviePoster, Double.parseDouble(voteAverage), plot);
                    mList.add(movie);
                }
            }

        }catch (JSONException je){
            je.printStackTrace();
        }
        return mList;
    }

    public static Movie parseMovieByIdJson(String json){
        Movie mMovie = null;
        try{
            JSONObject movie = new JSONObject(json);
            int id = movie.optInt("id");
            String original_title = movie.optString(TITLE);
            String releaseDate = movie.optString(RELEASE_DATE);
            String moviePoster = movie.optString(POSTER);
            String voteAverage = movie.optString(RATING);
            String plot = movie.optString(PLOT);

            mMovie = new Movie(id, original_title, releaseDate, moviePoster, Double.parseDouble(voteAverage), plot);


        }catch (JSONException je){
            je.printStackTrace();
        }
        return mMovie;
    }


    public static void getJsonResponse(Retrofit retrofit, String search, final MainActivity mainActivity){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIE_DB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        MovieService service = retrofit.create(MovieService.class);
        Call<MovieResults> call = service.getPopularMovies(APIKEY);

        switch (search){
            case "top_rated":
                call = service.getTopRatedMovies(APIKEY);
                break;
            case "popular":
                call = service.getPopularMovies(APIKEY);
                break;
        }
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                mainActivity.setMovies(response.body().getMovies());
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {

            }
        });
    }

    public static void getMovieByIdResponse(String id, MainActivity mainActivity){

    }

    public static List<Trailer> parseTrailerJson(String json){
        List<Trailer> trailerList = new ArrayList<>();
        try{
            JSONObject trailers = new JSONObject(json);
            JSONArray trailerInfo = trailers.optJSONArray(RESULTS);

            if(trailerInfo.length() > 0) {
                for(int i = 0; i < trailerInfo.length(); i++){
                    JSONObject trailerObj = trailerInfo.getJSONObject(i);
                    int id = trailerObj.optInt("id");
                    String key = trailerObj.optString("key");
                    String name = trailerObj.optString("name");

                    Trailer trailer = new Trailer(key, name);
                    trailerList.add(trailer);
                }
            }


        }catch (JSONException je){
            je.printStackTrace();
        }
        return trailerList;
    }

    public static List<Review> parseReviewJson(String json) {
        List<Review> reviewList = new ArrayList<>();
        try{
            JSONObject reviews = new JSONObject(json);
            JSONArray reviewInfo = reviews.optJSONArray(RESULTS);

            if(reviewInfo.length() > 0) {
                for(int i = 0; i < reviewInfo.length(); i++){
                    JSONObject reviewObj = reviewInfo.getJSONObject(i);
                    int id = reviewObj.optInt("id");
                    String key = reviewObj.optString("author");
                    String name = reviewObj.optString("content");

                    Review review = new Review(id, key, name);
                    reviewList.add(review);
                }
            }


        }catch (JSONException je){
            je.printStackTrace();
        }
        return reviewList;
    }
}