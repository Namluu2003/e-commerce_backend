package com.poly.app.infrastructure.util;

import com.poly.app.infrastructure.exception.ApiException;
import com.poly.app.infrastructure.exception.ErrorCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    public static Long getCurrentTimeNow() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String converDateString(Long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(new Date(dateLong));
    }

    public static String converDateTimeString(Long dateLong) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        return sdf.format(new Date(dateLong));
    }

    public static Long parseDateLong(String dateString)  {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
            Date date = sdf.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        }catch (Exception e) {
            System.out.println("lấy thời gian lỗi nè");
            throw new ApiException(ErrorCode.THOI_GIAN_INVALID);
        }
    }

    public static Long parseDateTimeLong(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        Date date = sdf.parse(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(converDateTimeString(1693475100000L));
        System.out.println(parseDateTimeLong("31-08-2023 16:45:00"));
    }
}
