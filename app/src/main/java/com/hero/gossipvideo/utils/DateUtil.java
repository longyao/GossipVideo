package com.hero.gossipvideo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by longtc on 15-4-8.
 */
public class DateUtil {

    public enum Format {
        YMD("yyyy-MM-dd"),
        YMD_HH_MM("yyyy-MM-dd HH:mm"),
        YMD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
        YMD_HH("HH:mm");

        private String fStr;

        private Format(String str) {
            fStr = str;
        }

        public String strOf() {
            return fStr;
        }
    }

    public static String format(long time, Format format) {
        SimpleDateFormat df = new SimpleDateFormat(format.strOf());
        return df.format(time);
    }

    public static String format(Date date, Format format) {
        return format(date.getTime(), format);
    }

    public static String format(long time) {
        return format(time, Format.YMD_HH_MM_SS);
    }

    public static String format(Date date) {
        return format(date, Format.YMD_HH_MM_SS);
    }

    public static Date parse2Date(String time, Format format) {
        SimpleDateFormat df = new SimpleDateFormat(format.strOf());
        try {
            Date d =  df.parse(time);
            return d;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long parse2Long(String time, Format format) {
        Date d = parse2Date(time, format);
        if (d != null) {
            return d.getTime();
        }

        return 0;
    }
}
