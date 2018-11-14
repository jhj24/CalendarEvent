package com.jhj.calendarevent;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;

import com.jhj.calendarevent.model.CalendarAccount;
import com.jhj.calendarevent.model.Config;
import com.jhj.calendarevent.model.ScheduleEventBean;

import java.util.TimeZone;

public class CalendarEvent {


    private static CalendarAccount account;
    private static boolean isCalenderAlarm = true;
    private static int aheadOfTime;

    public static void init(CalendarAccount account) {
        CalendarEvent.account = account;
    }

    public static void setCalenderAlarm(boolean isCalenderAlarm) {
        CalendarEvent.isCalenderAlarm = isCalenderAlarm;
    }

    public static void setCalenderAlarmAheadTime(int aheadOfTime) {
        CalendarEvent.aheadOfTime = aheadOfTime;
    }

    public static boolean insert(Context mContext, String displayName, ScheduleEventBean schedule) {
        if (account == null) {
            throw new NullPointerException("CalendarEvent must be init()");
        }

        try {
            Uri calendarEvent = addCalendarEvent(mContext, displayName, schedule);
            Uri alarmCalendarEvent = addCalendarAlarm(mContext, calendarEvent);
            return alarmCalendarEvent != null;
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.Events.TITLE, schedule.getTitle())
                    .putExtra(CalendarContract.Events.DESCRIPTION, schedule.getDescription())
                    .putExtra(CalendarContract.Events.EVENT_LOCATION, schedule.getLocation())
                    .putExtra(CalendarContract.Events.DTSTART, schedule.getStartTime())
                    .putExtra(CalendarContract.Events.DTEND, schedule.getEndTime())
                    .putExtra(CalendarContract.Reminders.MINUTES, aheadOfTime);
            mContext.startActivity(intent);
        }
        return false;
    }

    public static int update(Context mContext, String title, long startMills, ScheduleEventBean schedule) {
        int id = search(mContext, title, startMills);
        if (id == -1) {
            return -1;
        }
        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id);
        ContentValues up = updateCalendarEvent(schedule);
        return mContext.getContentResolver().update(updateUri, up, null, null);
    }

    /**
     * 根据id删除
     *
     * @param mContext Context
     * @param title    titel
     */
    public static int delete(Context mContext, String title, long startMills) {
        int id = search(mContext, title, startMills);
        if (id == -1) {
            return -1;
        }
        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(Config.CALENDAR_EVENT_URL), id);
        return mContext.getContentResolver().delete(deleteUri, null, null);
    }

    /**
     * 根据设置的title和开始事件来查找id
     *
     * @param mContext         Context
     * @param searchTitle      标题
     * @param searchStartMills 　开始事件
     * @return　事件id
     */
    private static int search(Context mContext, String searchTitle, long searchStartMills) {
        Cursor eventCursor = mContext.getContentResolver().query(Uri.parse(Config.CALENDAR_EVENT_URL), null, null, null, null);
        try {
            if (eventCursor == null)//查询返回空值
                return -1;
            if (eventCursor.getCount() > 0) {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext()) {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                    long eventStartTime = eventCursor.getLong(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                    if (!TextUtils.isEmpty(searchTitle) && searchTitle.equals(eventTitle) && searchStartMills == eventStartTime) {
                        return eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));
                    }
                }
            }
        } finally {
            if (eventCursor != null) {
                eventCursor.close();
            }
        }
        return -1;
    }


    /**
     * 添加闹铃
     *
     * @param mContext mContext
     * @param event    Uri
     */
    private static Uri addCalendarAlarm(Context mContext, Uri event) {
        //  事件提醒的设定
        ContentValues contentValues = new ContentValues();
        //  事件的ID
        contentValues.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(event));
        //  准时提醒    提前0分钟提醒
        contentValues.put(CalendarContract.Reminders.MINUTES, aheadOfTime);
        contentValues.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        return mContext.getContentResolver().insert(Uri.parse(Config.CALENDAR_REMIND_URL), contentValues);
    }

    /**
     * 创建日历事件
     *
     * @param mContext mContext
     * @param schedule ScheduleEventBean
     * @return
     */
    private static Uri addCalendarEvent(Context mContext, String displayName, ScheduleEventBean schedule) {
        int account = checkAndAddCalendarAccount(mContext, displayName);
        if (account < 0)
            return null;
        if (schedule.getTitle() == null) {
            throw new NullPointerException("title field must not be null when used in class ScheduleEventBean");
        }
        if (schedule.getStartTime() == 0) {

        }

        ContentValues contentValues = new ContentValues();
        // 事件的日历_ID。
        contentValues.put(CalendarContract.Events.CALENDAR_ID, account);
        //  事件标题
        contentValues.put(CalendarContract.Events.TITLE, schedule.getTitle());
        //  事件发生的地点
        contentValues.put(CalendarContract.Events.EVENT_LOCATION, schedule.getLocation());
        //  事件描述
        contentValues.put(CalendarContract.Events.DESCRIPTION, schedule.getDescription());
        //  事件开始时间
        contentValues.put(CalendarContract.Events.DTSTART, schedule.getStartTime());
        //  事件结束时间
        contentValues.put(CalendarContract.Events.DTEND, schedule.getEndTime());
        //  设置有闹钟提醒
        if (isCalenderAlarm) {
            contentValues.put(CalendarContract.Events.HAS_ALARM, 1);
        }
        //  设置提示规则
        if (schedule.getRule() != null) {
            contentValues.put(CalendarContract.Events.RRULE, schedule.getRule());
        }
        //  事件时区
        contentValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        //  返回事件

        return mContext.getContentResolver().insert(Uri.parse(Config.CALENDAR_EVENT_URL), contentValues);

    }

    /**
     * 修改日历事件
     *
     * @param schedule ScheduleEventBean
     * @return ContentValues
     */
    private static ContentValues updateCalendarEvent(ScheduleEventBean schedule) {
        ContentValues contentValues = new ContentValues();
        if (schedule == null) {
            return contentValues;
        }

        //  事件标题
        if (schedule.getTitle() != null)
            contentValues.put(CalendarContract.Events.TITLE, schedule.getTitle());
        //  事件发生的地点
        if (schedule.getLocation() != null)
            contentValues.put(CalendarContract.Events.EVENT_LOCATION, schedule.getLocation());
        //  事件描述
        if (schedule.getDescription() != null)
            contentValues.put(CalendarContract.Events.DESCRIPTION, schedule.getDescription());
        //  事件开始时间
        if (schedule.getStartTime() != 0)
            contentValues.put(CalendarContract.Events.DTSTART, schedule.getStartTime());
        //  事件结束时间
        if (schedule.getEndTime() != 0)
            contentValues.put(CalendarContract.Events.DTEND, schedule.getEndTime());
        //  设置有闹钟提醒
        if (isCalenderAlarm)
            contentValues.put(CalendarContract.Events.HAS_ALARM, 1);
        //  设置提示规则
        if (schedule.getRule() != null)
            contentValues.put(CalendarContract.Events.RRULE, schedule.getRule());
        return contentValues;

    }

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     *
     * @param mContext    Context
     * @param displayName String
     * @return 账户
     */
    private static int checkAndAddCalendarAccount(Context mContext, String displayName) {
        int oldId = checkCalendarAccount(mContext, displayName);
        if (oldId >= 0) {
            return oldId;
        } else {
            long addId = addCalendarAccount(mContext, displayName);
            if (addId >= 0) {
                return checkCalendarAccount(mContext, displayName);
            } else {
                return -1;
            }
        }
    }

    /**
     * 检查是否有现有存在的账户。存在则返回账户id，否则返回-1
     *
     * @param mContext    Context
     * @param displayName String
     * @return 账户
     */
    private static int checkCalendarAccount(Context mContext, String displayName) {
        if (account.getCalendarAccountName() == null) {
            throw new NullPointerException("The calendarAccountName field of the CalendarAccount method must be initialized");
        }
        Cursor userCursor = mContext.getContentResolver().query(Uri.parse(Config.CALENDAR_URL), null, null, null, null);
        try {
            if (userCursor == null)//查询返回空值
                return -1;
            int count = userCursor.getCount();
            if (count > 0) {//存在现有账户，取第一个账户的id返回
                for (userCursor.moveToFirst(); !userCursor.isAfterLast(); userCursor.moveToNext()) {

                    String displayNameDB = userCursor.getString(userCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME));
                    String accountName = userCursor.getString(userCursor.getColumnIndex("ACCOUNT_NAME"));
                    if (accountName.equals(account.getCalendarAccountName()) && displayName.equals(displayNameDB)) {
                        return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
                    }
                }
                return -1;
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

    /**
     * 添加新的账户，账户创建成功则返回账户id，否则返回-1
     *
     * @param mContext    Context
     * @param displayName String
     * @return 账户号
     */
    private static long addCalendarAccount(Context mContext, String displayName) {

        if (account.getCalendarName() == null) {
            throw new NullPointerException("The calendarName field of the CalendarAccount method must be initialized");
        }
        if (account.getCalendarAccountName() == null) {
            throw new NullPointerException("The calendarAccountName field of the CalendarAccount method must be initialized");
        }
        if (account.getCalendarAccountType() == null) {
            throw new NullPointerException("The calendarAccountType field of the CalendarAccount method must be initialized");
        }


        ContentValues contentValues = new ContentValues();
        //  日历名称
        contentValues.put(CalendarContract.Calendars.NAME, account.getCalendarName());
        //  日历账号，为邮箱格式
        contentValues.put(CalendarContract.Calendars.ACCOUNT_NAME, account.getCalendarAccountName());
        //  账户类型，com.android.exchange
        contentValues.put(CalendarContract.Calendars.ACCOUNT_TYPE, account.getCalendarAccountType());
        //  展示给用户的日历名称
        contentValues.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, displayName);
        //  它是一个表示被选中日历是否要被展示的值。
        //  0值表示关联这个日历的事件不应该展示出来。
        //  而1值则表示关联这个日历的事件应该被展示出来。
        //  这个值会影响CalendarContract.instances表中的生成行。
        contentValues.put(CalendarContract.Calendars.VISIBLE, 1);
        //  账户标记颜色
        contentValues.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        //  账户级别
        contentValues.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        //  它是一个表示日历是否应该被同步和是否应该把它的事件保存到设备上的值。
        //  0值表示不要同步这个日历或者不要把它的事件存储到设备上。
        //  1值则表示要同步这个日历的事件并把它的事件储存到设备上。
        contentValues.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        //  时区
        contentValues.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, TimeZone.getDefault().getID());
        //  账户拥有者
        contentValues.put(CalendarContract.Calendars.OWNER_ACCOUNT, account.getCalendarAccountName());
        contentValues.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(Config.CALENDAR_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, account.getCalendarAccountName())
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, account.getCalendarAccountType())
                .build();

        Uri result = mContext.getContentResolver().insert(calendarUri, contentValues);
        return result == null ? -1 : ContentUris.parseId(result);
    }
}