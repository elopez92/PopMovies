package manic.com.popularmovies.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Movie implements Parcelable {

    @PrimaryKey
    private int id;
    private String title;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "movie_poster")
    private String moviePoster;
    @ColumnInfo(name = "vote_average")
    private String voteAverage;
    private String plot;

    @Ignore
    private boolean isFavorite = false;

    private final static String IMG_BASE_URL = "http://image.tmdb.org/t/p/";
    private final static String imgSize = "w185";

    @Ignore
    public Movie(String title, String releaseDate, String moviePoster, String voteAverage, String plot){
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAverage = voteAverage;
        this.plot = plot;
    }

    public Movie(int id, String title, String releaseDate, String moviePoster, String voteAverage, String plot){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        if(!moviePoster.equals(""))
        this.moviePoster = moviePoster;
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
        this.moviePoster = moviePoster;
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

    public boolean isFavorite(){
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
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
