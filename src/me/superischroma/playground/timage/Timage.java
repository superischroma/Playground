package me.superischroma.playground.timage;

import java.util.Arrays;
import java.util.List;

public class Timage
{
    public static final List<String> MONTH_NAMES = Arrays.asList("New Awakening", "Early Spring",
            "Late Spring", "Early Summer", "Late Summer", "Early Autumn", "Late Autumn",
            "Early Winter", "Late Winter", "Oldened Resting");

    public static final long EPOCH = 1587340800000L; // epoch is April 20, 2020 at 12:00 AM GMT
    public static final long YEAR = 2419200000L; // 28 irl days
    public static final long MONTH = 201600000L; // ~2.3 irl days | 10 months in a year
    public static final long DAY = 6300000L; // ~1h 45m irl | 32 days in a month
    public static final long HOUR = 262500L; // ~4m 22s irl | 24 hours in a day
    public static final long MINUTE = 4375L; // 4s 375ms irl | 60 minutes in an hour

    public static long timestamp()
    {
        return System.currentTimeMillis() - EPOCH;
    }

    public static int getYear()
    {
        return (int) (timestamp() / YEAR);
    }

    public static int getMonth()
    {
        return (int) ((timestamp() / MONTH) % 10) + 1;
    }

    public static int getDay()
    {
        return (int) ((timestamp() / DAY) % 32) + 1;
    }

    public static int getHours()
    {
        return (int) ((timestamp() / HOUR) % 24);
    }

    public static int getMinutes()
    {
        return (int) ((timestamp() / MINUTE) % 60);
    }

    public static String getMonthName(int month)
    {
        month--;
        if (month < 0 || month > 9)
            return "Unknown Month";
        return MONTH_NAMES.get(month);
    }

    public static String getMonthName()
    {
        return getMonthName(getMonth());
    }
}
