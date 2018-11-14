package com.jhj.calendarevent.model;

public class ScheduleEventBean {

    /**
     * 事件标题
     */
    private String title;
    /**
     * 事件描述
     */
    private String description;
    /**
     * 事件发生地点
     */
    private String location;
    /**
     * 开始时间
     */
    private long startTime;
    /**
     * 结束时间
     */
    private long endTime;

    private String rule;


    public String getTitle() {
        return title;
    }

    public ScheduleEventBean setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ScheduleEventBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ScheduleEventBean setLocation(String location) {
        this.location = location;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public ScheduleEventBean setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public long getEndTime() {
        return endTime;
    }

    public ScheduleEventBean setEndTime(long endTime) {
        this.endTime = endTime;
        return this;
    }

    public String getRule() {
        return rule;
    }

    public ScheduleEventBean setRule(String rule) {
        this.rule = rule;
        return this;
    }
}
