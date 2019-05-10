package manic.com.popularmovies.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReleaseDateUtils {

    public static String formatDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date newDate = new Date();
        try {
            newDate = format.parse(date);
            format = new SimpleDateFormat("yyyy");

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(newDate);
    }
}
