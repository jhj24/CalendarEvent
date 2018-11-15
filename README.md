# CalendarEvent
### １. 权限
```
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.WRITE_CALENDAR" />
```
使用时还需动态申请

### 2.方法
- 可以在Application中初始化账户信息
```
init(CalendarAccount account)
```
- 设置提示在事件开始前几分钟
```
setCalenderAlarmAheadTime(int aheadOfTime)
```
- 日历事件增、删、改、查

```
/**
* 日历事件创建
*
* @param mContext    mContext
* @param displayName 显示在日历上账户的名称
* @param schedule    日历事件详情
* @return　boolean 事件是否创建成功
*/
public static boolean insert(Context mContext, String displayName, ScheduleEventBean schedule)

/**
* 日历事件修改
*
* @param mContext         Context
* @param searchTitle      要修改日历事件的标题
* @param searchStartMills 　要修改日历事件的开始时间
* @param schedule         　日历事件详情
* @return boolean 事件是否修改成功
*/
public static boolean update(Context mContext, String searchTitle, long searchStartMills, ScheduleEventBean schedule)

/**
* 根据id删除
*
* @param mContext         Context
* @param searchTitle      要删除日历事件的标题
* @param searchStartMills 　要删除日历事件的开始时间
* @return boolean 事件是否删除成功
*/
public static boolean delete(Context mContext, String searchTitle, long searchStartMills) 

/**
* 根据设置的title和开始事件来查找id
*
* @param mContext         Context
* @param searchTitle      要搜索日历事件的标题
* @param searchStartMills 　要搜索日历事件的开始时间
* @return　事件id
*/
public static int search(Context mContext, String searchTitle, long searchStartMills)

```

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
