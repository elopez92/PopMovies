package manic.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String releaseDate;
    private String moviePoster;
    private String voteAverage;
    private String plot;


    private final static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private String imgSize = "w185";

    public Movie(int id, String title, String releaseDate, String moviePoster, String voteAverage, String plot){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = IMG_BASE_URL + imgSize + moviePoster;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        voteAverage = in.readString();
        plot = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = IMG_BASE_URL + imgSize + moviePoster;
    }

    public String getVoteAverage() {
        return voteAverage+"/10";
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getTitle());
        dest.writeString(releaseDate);
        dest.writeString(moviePoster);
        dest.writeString(voteAverage);
        dest.writeString(plot);
    }
}
