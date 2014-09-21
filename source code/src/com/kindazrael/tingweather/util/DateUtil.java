package com.kindazrael.tingweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtil {

    private static final String LOG_TAG_STRING = DateUtil.class.getName();
    private static final String SMART_API_DATE_FORMATE_STRING = "yyyyMMddHHmm";
    private static final SimpleDateFormat SMART_API_DATE_FORMAT = new SimpleDateFormat(SMART_API_DATE_FORMATE_STRING,
            Locale.CHINA);
    private static final Calendar CALENDAR = Calendar.getInstance();
    
    public static String getSmartAPICurrentDateString() {
        return getSmartAPIDateString(new Date());
    }
    
    public static String getSmartAPIDateString(Date date) {
        return SMART_API_DATE_FORMAT.format(date);
    }

    public static Date getDateFromSmartAPIString(String string) {
        try {
            return SMART_API_DATE_FORMAT.parse(string);
        } catch (ParseException e) {
            LogUtil.e(LOG_TAG_STRING, "Error when parese smart weather string" + string + "to date", e);
            return new Date();
        }
    }

    public static Date addDays(Date date, int days) {
        CALENDAR.setTime(date);
        CALENDAR.add(Calendar.DATE, days);

        return CALENDAR.getTime();
    }

    public static boolean isNight() {
        Date currentTime = new Date();
        CALENDAR.setTime(currentTime);
        int hour = CALENDAR.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 19) {
            return false;
        } else {
            return true;
        }
    }
}
