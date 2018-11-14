package com.jhj.calendarevent;

public class CalendarAccount {

    private String calendarName;
    private String calendarAccountName;
    private String calendarAccountType;

    public String getCalendarName() {
        return calendarName;
    }

    public CalendarAccount setCalendarName(String calendarName) {
        this.calendarName = calendarName;
        return this;
    }

    public String getCalendarAccountName() {
        return calendarAccountName;
    }

    public CalendarAccount setCalendarAccountName(String calendarAccountName) {
        this.calendarAccountName = calendarAccountName;
        return this;
    }

    public String getCalendarAccountType() {
        return calendarAccountType;
    }

    public CalendarAccount setCalendarAccountType(String calendarAccountType) {
        this.calendarAccountType = calendarAccountType;
        return this;
    }
}
