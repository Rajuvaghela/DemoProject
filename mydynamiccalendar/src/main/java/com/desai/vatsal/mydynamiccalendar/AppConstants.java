package com.desai.vatsal.mydynamiccalendar;

import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by vatsaldesai on 23-09-2016.
 */

public class AppConstants {

    public static ArrayList<EventModel> eventList = new ArrayList<>();
    public static ArrayList<String> holidayList = new ArrayList<>();

    public static Calendar main_calendar = Calendar.getInstance();

    public static SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
    public static SimpleDateFormat sdfHourMinute = new SimpleDateFormat("HH:mm");

    public static boolean isShowMonth = false;
    public static boolean isShowMonthWithBellowEvents = false;
    public static boolean isShowWeek = false;
    public static boolean isShowDay = false;
    public static boolean isAgenda = false;

    public static boolean isSaturdayOff = false;
    public static boolean isSundayOff = false;
    public static boolean isHolidayCellClickable = false;

    public static String strSaturdayOffBackgroundColor = "null";
    public static int saturdayOffBackgroundColor = -1;

    public static String strSaturdayOffTextColor = "null";
    public static int saturdayOffTextColor = -1;

    public static String strSundayOffBackgroundColor = "null";
    public static int sundayOffBackgroundColor = -1;

    public static String strSundayOffTextColor = "null";
    public static int sundayOffTextColor = -1;

    public static String strExtraDatesBackgroundColor = "#e9ebee";
    public static int extraDatesBackgroundColor = Color.parseColor("#e9ebee");

    public static String strExtraDatesTextColor = "#8a8a8b";
    public static int extraDatesTextColor =  Color.parseColor("#8a8a8b");

    public static String strDatesBackgroundColor = "null";
    public static int datesBackgroundColor =  -1;

    public static String strDatesTextColor = "#424242";
    public static int datesTextColor = Color.parseColor("#424242");

    public static String strCurrentDateBackgroundColor = "null";
    public static int currentDateBackgroundColor = -1;

    public static String strCurrentDateTextColor = "#E04637";
    public static int currentDateTextColor = Color.parseColor("#E04637");

    public static String strEventCellBackgroundColor = "null";
    public static int eventCellBackgroundColor = -1;

    public static String strEventCellTextColor = "null";
    public static int eventCellTextColor = -1;

    public static String strBelowMonthEventTextColor = "null";
    public static int belowMonthEventTextColor = -1;

    public static String strBelowMonthEventDividerColor = "null";
    public static int belowMonthEventDividerColor = -1;

    public static String strHolidayCellBackgroundColor = "null";
    public static int holidayCellBackgroundColor = -1;

    public static String strHolidayCellTextColor = "null";
    public static int holidayCellTextColor = -1;





}
