package com.jhj.calendarevent.model;

import android.support.annotation.NonNull;

public class CalendarAccount {

    /**
     * 日历名称
     */
    private String calendarName;
    /**
     * 日历账号，为邮箱格式
     */
    private String calendarAccountName;
    /**
     * 账户类型，com.android.exchange
     */
    private String calendarAccountType;

    public CalendarAccount(@NonNull String calendarName, @NonNull String calendarAccountName, @NonNull String calendarAccountType) {
        this.calendarName = calendarName;
        this.calendarAccountName = calendarAccountName;
        this.calendarAccountType = calendarAccountType;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public String getCalendarAccountName() {
        return calendarAccountName;
    }

    public String getCalendarAccountType() {
        return calendarAccountType;
    }


}
