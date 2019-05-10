package manic.com.popularmovies.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String  APIKEY = "";


    private final static String MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    //private final static String PARAM_SORT = "sort";
    private final static String API_KEY = "api_key";

    public static URL buildUrl(String sortBy) {
        Uri builtUri = Uri.parse(MOVIE_DB_URL + sortBy).buildUpon()
                .appendQueryParameter(API_KEY, APIKEY)
                .build();


        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
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
