package manic.com.popularmovies.utils;

import manic.com.popularmovies.model.Movie;
import manic.com.popularmovies.model.MovieResults;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie")
    Call<Movie> getMovieById(@Query("id") String id, @Query("api_key") String api_key);
}
