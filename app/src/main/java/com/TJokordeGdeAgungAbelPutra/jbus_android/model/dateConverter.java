package com.TJokordeGdeAgungAbelPutra.jbus_android.model;

import android.os.Build;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateConverter {
    public static String convertDateString(String inputDate) {
        DateTimeFormatter inputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        }
        DateTimeFormatter outputFormatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a");
        }

        LocalDateTime dateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(inputDate, inputFormatter);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime.format(outputFormatter);
        }
        return inputDate;
    }
}
