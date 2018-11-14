# CalendarEvent
###　１. 权限
```
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
```
使用时还需动态申请

### 新建事件

以下是针对插入一个新的事件的一些规则：

1.  必须包含CALENDAR_ID和DTSTART字段

2.  必须包含EVENT_TIMEZONE字段。使用getAvailableIDs()方法获得系统已安装的时区ID列表。注意如果通过INSTERT类型Intent对象来插入事件，那么这个规则不适用，因为在INSERT对象的场景中会提供一个默认的时区；

3.  对于非重复发生的事件，必须包含DTEND字段；

4.  对重复发生的事件，必须包含一个附加了RRULE或RDATE字段的DURATIION字段。注意，如果通过INSERT类型的Intent对象来插入一个事件，这个规则不适用。因为在这个Intent对象的应用场景中，你能够把RRULE、DTSTART和DTEND字段联合在一起使用，并且Calendar应用程序能够自动的把它转换成一个持续的时间。


｜表名｜	说明
｜------|------
|calendars|	此表储存日历特定信息。 此表中的每一行都包含一个日历的详细信息，例如名称、颜色、同步信息等。
|events	| 此表储存事件特定信息。 此表中的每一行都包含一个事件的信息 — 例如事件标题、地点、开始时间、结束时间等。 事件可一次性发生，也可多次重复发生。参加者、提醒和扩展属性存储在单独的表内。它们各自具有一个 EVENT_ID，用于引用 Events 表中的 _ID。
|instances	| 此表储存每个事件实例的开始时间和结束时间。 此表中的每一行都表示一个事件实例。 对于一次性事件，实例与事件为 1:1 映射。对于重复事件，会自动生成多个行，分别对应多个事件实例。
|attendees	| 此表储存事件参加者（来宾）信息。 每一行都表示事件的一位来宾。 它指定来宾的类型以及事件的来宾出席响应。
|reminders	| 此表储存提醒/通知数据。 每一行都表示事件的一个提醒。一个事件可以有多个提醒。 每个事件的最大提醒数量在 MAX_REMINDERS 中指定，后者由拥有给定日历的同步适配器设置。 提醒以事件发生前的分钟数形式指定，其具有一个可决定用户提醒方式的方法。


### rule
根据以下规则，设置提醒

- 每周的提醒：
```
FREQ=WEEKLY;WKST=SU;BYDAY=MO
```
- 每月的提醒：
```
FREQ=MONTHLY;WKST=SU;BYMONTHDAY=4//?
```
- 每年的提醒：
```
FREQ=YEARLY;WKST=SU
```
- 每天的提醒
```
FREQ=DAILY;WKST=SU
```
- 每十天的提醒：
```
FREQ=DAILY;INTERVAL=10;WKST=SU
```
- 每8天提醒一次，截止到20180808
```
FREQ=DAILY;UNTIL=20180808T093000;INTERVAL=8;WKST=SU
```
- 每8天提醒一次，提醒9次
```
FREQ=DAILY;COUNT=9;INTERVAL=8;WKST=SU
```
