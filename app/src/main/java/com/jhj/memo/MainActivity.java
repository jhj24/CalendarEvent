package com.jhj.memo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jhj.calendarevent.CalendarEvent;
import com.jhj.calendarevent.model.CalendarAccount;
import com.jhj.calendarevent.model.ScheduleEventBean;
import com.jhj.permissionscheck.OnPermissionsListener;
import com.jhj.permissionscheck.PermissionsRequest;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    long startMills = 1542184881123L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PermissionsRequest.Builder(MainActivity.this)
                        .requestPermissions(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
                        .callback(new OnPermissionsListener() {
                            @Override
                            public void onPermissions(List<String> list, List<String> list1) {
                                if (list.size() == 0) {
                                    insert();
                                }
                            }
                        })
                        .build();
            }
        });

        findViewById(R.id.btn_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PermissionsRequest.Builder(MainActivity.this)
                        .requestPermissions(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
                        .callback(new OnPermissionsListener() {
                            @Override
                            public void onPermissions(List<String> list, List<String> list1) {
                                if (list.size() == 0) {
                                    update();
                                }
                            }
                        })
                        .build();
            }
        });


        findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PermissionsRequest.Builder(MainActivity.this)
                        .requestPermissions(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)
                        .callback(new OnPermissionsListener() {
                            @Override
                            public void onPermissions(List<String> list, List<String> list1) {
                                if (list.size() == 0) {
                                    boolean isSuccess = CalendarEvent.delete(MainActivity.this, "title1", startMills);
                                    if (!isSuccess) {
                                        //删除失败
                                    }
                                }
                            }
                        })
                        .build();
            }
        });

    }

    private void update() {
        ScheduleEventBean bean = new ScheduleEventBean();
        bean.setTitle("title1");
        boolean isSuccess = CalendarEvent.update(this, "title", startMills, bean);
        if (!isSuccess) {
            //修改失败
        }
    }

    private void insert() {
        CalendarAccount account = new CalendarAccount("calendarName", "calendarAccountName", "calendarAccountType");
        CalendarEvent.init(account);
        ScheduleEventBean bean = new ScheduleEventBean();
        bean.setTitle("title")
                .setDescription("description")
                .setLocation("location")
                .setStartTime(startMills)
                .setEndTime(System.currentTimeMillis() + 500000);
        boolean isSuccess = CalendarEvent.insert(this, "displayName", bean);
        if (isSuccess) {
            //添加成功
        }
    }
}
