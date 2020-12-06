package com.app.booking.service.util;


import java.time.ZonedDateTime;

public class Date {


    public static boolean isValidDateRange(ZonedDateTime target, ZonedDateTime startDate, ZonedDateTime endDate) {
        boolean start = target.compareTo(startDate) >= 0;
        boolean end = target.compareTo(endDate) <= 0;
        boolean result = start && end;

        return result;
    }
}
