package com.example.myboardgames;

import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static Date getCurrentDate() {
        //Calendar cal = Calendar.getInstance();
        //return cal.getTime();
        //String str = DateTimeUtils.formatDate(new Date(), Locale.getDefault());
        //return DateTimeUtils.formatDate(str);
        return new Date();
        //return DateTimeUtils.formatDate((new Date()).getDate(), Locale.getDefault());
        /*return new Date(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));*/
    }

    public static int getPositionOfStr(String str, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str))
                return i;
        }
        return -1;
    }

    public static boolean validateName(String categoryName) {
        return !categoryName.isEmpty();
    }
}
