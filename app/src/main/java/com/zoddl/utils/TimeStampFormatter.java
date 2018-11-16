package com.zoddl.utils;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author abhishek.tiwari on 7/9/15.
 */
public class TimeStampFormatter {


    public static final String DATE_FORMAT_1 = "dd/MM/yyyy";
    public static final String DATE_FORMAT_2 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    public static final String DATE_FORMAT_3 = "dd-MM-yyyy HH:mm";
    public static final String DATE_FORMAT_4 = "HH:mm EEEE";
    public static final String DATE_FORMAT_5 = " MMM yyyy, HH:mm";
    /**
     *
     *
     yyyy-MM-dd 1969-12-31
     yyyy-MM-dd 1970-01-01
     yyyy-MM-dd HH:mm 1969-12-31 16:00
     yyyy-MM-dd HH:mm 1970-01-01 00:00
     yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
     *
     */
    public static String getValueFromTS(String timeStamp, String dateFormat) {
        try {
            if (TextUtils.isEmpty(timeStamp)){
                return "";
            }
            if (timeStamp.length()==10) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp)*1000)));
            } else if (timeStamp.length()==13) {
                DateFormat formatter1 = new SimpleDateFormat(dateFormat, Locale.getDefault());
                return formatter1.format(new Date((Long.valueOf(timeStamp))));
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static long getTimeStamp(String date, String dateFormat) {
        try{
            if (!TextUtils.isEmpty(date)){
                DateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
                Date newDate = (Date)formatter.parse(date);
                LogUtils.infoLog("TimeStampFormatter", "_____dateFormat_____" + dateFormat);
                LogUtils.infoLog("TimeStampFormatter", "_____getTimeStamp_____" + newDate.getTime());
                return newDate.getTime();
            }else {
                return 0;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static String changeDateTimeFormat(String date, String oldFormat, String newFormat) {
        if (TextUtils.isEmpty(date))
            date = "0";
        SimpleDateFormat inputFormat = new SimpleDateFormat(oldFormat, Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat(newFormat, Locale.getDefault());
        Date newDate;
        String str;
        try {
            newDate = inputFormat.parse(date);
            str = outputFormat.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return str;
    }

    /**
     * getDateSuffix
     * @param day day
     * @return DateSuffix
     */
    public static String getDateSuffix( int day) {
        switch (day) {
            case 1: case 21: case 31:
                return (day+"st");

            case 2: case 22:
                return (day+"nd");

            case 3: case 23:
                return (day+"rd");

            default:
                return (day+"th");
        }
    }

    /**
     * getCommentTime
     * @param currentDate currentDate
     * @param startDate startDate
     * @return comment time
     */
    public static String getCommentTime(String currentDate, String startDate){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_1, Locale.getDefault());
        try {
            Date date1 = simpleDateFormat.parse(getValueFromTS(currentDate, DATE_FORMAT_1));
            Date date2 = simpleDateFormat.parse(getValueFromTS(startDate, DATE_FORMAT_1));
            return printDifference(date1, date2, startDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400

    /**
     * printDifference
     * @param startDate startDate
     * @param endDate endDate
     * @param date date
     * @return String difference
     */
    public static String printDifference(Date startDate, Date endDate, String date){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        LogUtils.infoLog("TimeStampFormatter", "startDate : " + startDate);
        LogUtils.infoLog("TimeStampFormatter", "endDate   : "+ endDate);
        LogUtils.infoLog("TimeStampFormatter", "different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        if (elapsedDays <0 && elapsedSeconds<60){
            return String.valueOf(elapsedSeconds)+"secs ago";
        }else if (elapsedDays <0 && elapsedSeconds>60 && elapsedMinutes<60){
            return String.valueOf(elapsedMinutes)+"mins ago";
        }else if (elapsedDays<0 && elapsedMinutes>60 && elapsedHours<24){
            return String.valueOf(elapsedHours)+"hrs ago";
        }else {
            return (getValueFromTS(date, DATE_FORMAT_1));
        }
    }


}
