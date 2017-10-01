package familytracker.snm.com.familytracker.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kumanjit on 10/1/2017.
 */

/*
* class used To generate Time in correct Format
* */

public class TimestampUtils{
    public static String getISO8601StringForCurrentDate(){
        Date now=new Date();
        return getISO8601StringForDate(now);
    }

    public static String getISO8601StringForDate(Date date){
        DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"+"+05:30", Locale.UK);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

/**
 * Private constructor: class cannot be instantiated
 */
    private TimestampUtils(){}

}