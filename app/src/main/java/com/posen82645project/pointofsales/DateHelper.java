package com.posen82645project.pointofsales;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    public static long convertDateStringToTimestamp(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Change the format according to your date string
            Date date = dateFormat.parse(dateString);
            return date != null ? date.getTime() : 0;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static long calculateDaysDifference(long date1, long date2) {
        long difference = date2 - date1;
        return difference / (1000 * 60 * 60 * 24); // Convert milliseconds to days
    }
}
