package com.example.myboardgames.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Utils {

    public static Date getCurrentDate() {
        return new Date();
    }

    public static LocalDateTime convertToLocalDateTimeViaInstant(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String convertDateToLocalString(Date date) {
        LocalDateTime dateL = convertToLocalDateTimeViaInstant(date);
        String[] arr = String.valueOf(dateL).split("T");
        String[] day = arr[0].split("-");
        String[] time = arr[1].split(":");
        return time[0] + ":" + time[1] + " " + day[2] + "." + day[1] + "." + day[0];
    }

    public static int getPositionOfStr(String str, String[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str))
                return i;
        }
        return -1;
    }

    public static boolean validateName(String name) {
        return !name.isEmpty();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
