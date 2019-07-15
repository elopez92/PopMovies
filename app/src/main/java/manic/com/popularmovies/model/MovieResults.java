package manic.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResults implements Parcelable {
    @SerializedName("results")
    @Expose
    private List<Movie> movies = null;
    @SerializedName("total_results")
    @Expose
    private Integer totalMovies;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    public final static Parcelable.Creator<MovieResults> CREATOR = new Creator<MovieResults>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MovieResults createFromParcel(Parcel in) {
            return new MovieResults(in);
        }

        public MovieResults[] newArray(int size) {
            return (new MovieResults[size]);
        }

    }
            ;

    protected MovieResults(Parcel in) {
        in.readList(this.movies, (manic.com.popularmovies.model.Movie.class.getClassLoader()));
        this.totalMovies = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.totalPages = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public MovieResults() {
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setResults(List<Movie> results) {
        this.movies = results;
    }

    public Integer getTotalResults() {
        return totalMovies;
    }

    public void setTotalResults(Integer totalMovies) {
        this.totalMovies = totalMovies;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(movies);
        dest.writeValue(totalMovies);
        dest.writeValue(totalPages);
    }

    public int describeContents() {
        return 0;
    }

}
