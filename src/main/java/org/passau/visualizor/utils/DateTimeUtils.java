package org.passau.visualizor.utils;

import org.apache.tomcat.jni.Local;

import java.text.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

public class DateTimeUtils {

    public static LocalDateTime convertUTC3toUTC0(LocalDateTime dateTime) {
        return LocalDateTime.ofEpochSecond(dateTime.toEpochSecond(ZoneOffset.ofHours(3)), 0, ZoneOffset.ofHours(0));
    }

    public static LocalDateTime convertRFC3339toUTC3(String dateTime) throws ParseException {
        return LocalDateTime.ofInstant(DateTimeUtils.parseRFC3339Date(dateTime).toInstant(), ZoneId.of("UTC+3"));
    }

    public static Date parseRFC3339Date(String dateString) throws java.text.ParseException, IndexOutOfBoundsException {
        Date d;

        //if there is no time zone, we don't need to do any special parsing.
        if (dateString.endsWith("Z")) {
            try {
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());//spec for RFC3339 with a 'Z'
                s.setTimeZone(TimeZone.getTimeZone("UTC"));
                d = s.parse(dateString);
            } catch (java.text.ParseException pe) {//try again with optional decimals
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault());//spec for RFC3339 with a 'Z' and fractional seconds
                s.setTimeZone(TimeZone.getTimeZone("UTC"));
                s.setLenient(true);
                d = s.parse(dateString);
            }
            return d;
        }

        //step one, split off the timezone.
        String firstPart;
        String secondPart;
        if (dateString.lastIndexOf('+') == -1) {
            firstPart = dateString.substring(0, dateString.lastIndexOf('-'));
            secondPart = dateString.substring(dateString.lastIndexOf('-'));
        } else {
            firstPart = dateString.substring(0, dateString.lastIndexOf('+'));
            secondPart = dateString.substring(dateString.lastIndexOf('+'));
        }

        //step two, remove the colon from the timezone offset
        secondPart = secondPart.substring(0, secondPart.indexOf(':')) + secondPart.substring(secondPart.indexOf(':') + 1);
        dateString = firstPart + secondPart;
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());//spec for RFC3339
        try {
            d = s.parse(dateString);
        } catch (java.text.ParseException pe) {//try again with optional decimals
            s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ", Locale.getDefault());//spec for RFC3339 (with fractional seconds)
            s.setLenient(true);
            d = s.parse(dateString);
        }
        return d;
    }

    public static void changeResultDateTimeToUTC3(List<?> prediction) {
                prediction.forEach(val -> {
                    if(!(val instanceof LinkedHashMap))
                        return;
                    Object trajectory = ((LinkedHashMap<?, ?>) val).get("trajectory");
                    if(!(trajectory instanceof ArrayList))
                        return;

                    ((ArrayList<?>) trajectory).forEach(trajectoryVal -> {
                        if(!(trajectoryVal instanceof LinkedHashMap))
                            return;
                        Object datetime = ((LinkedHashMap) trajectoryVal).get("datetime");
                        if(datetime == null)
                            return;
                        try {
                            LocalDateTime dateTimeUTC3 = DateTimeUtils.convertRFC3339toUTC3((String) datetime);
                            System.out.println(dateTimeUTC3);
                            ((LinkedHashMap) trajectoryVal).put("datetime", dateTimeUTC3.toString());
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                    });
                });
    }
}