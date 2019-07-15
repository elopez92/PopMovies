package manic.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String  APIKEY = "ac61e564ce0970984add21e6b38fa3ca";


    private final static String MOVIE_DB_URL = "http://api.themoviedb.org/3/";

    //private final static String PARAM_SORT = "sort";
    private final static String API_KEY = "api_key";

    public static URL buildMovieUrl(String string) {
        Uri builtUri = Uri.parse(MOVIE_DB_URL + "movie/" +  string).buildUpon()
                .appendQueryParameter(API_KEY, APIKEY)
                .build();

        return buildUrl(builtUri);
    }

    public static URL buildMovieFindByIdUrl(String id){
        Uri buildUri = Uri.parse(MOVIE_DB_URL + "movie/" + id).buildUpon()
                .appendQueryParameter(API_KEY, APIKEY)
                .build();

        return buildUrl(buildUri);
    }

    public static URL buildUrl(String id, String type){
        Uri builtUri = Uri.parse(MOVIE_DB_URL + "movie/" +  id +"/" + type).buildUpon()
                .appendQueryParameter(API_KEY, APIKEY)
                .build();

        return buildUrl(builtUri);
    }

    private static URL buildUrl(Uri uri){
        URL url= null;
        try{
            url = new URL(uri.toString());
        } catch(MalformedURLException mue){
            mue.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
