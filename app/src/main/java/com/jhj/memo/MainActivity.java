package com.jhj.memo;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jhj.calendarevent.CalendarAccount;
import com.jhj.calendarevent.CalendarEvent;
import com.jhj.calendarevent.ScheduleEventBean;
import com.jhj.permissionscheck.OnPermissionsListener;
import com.jhj.permissionscheck.PermissionsRequest;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert();
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

    }

    private void insert() {
        CalendarAccount account = new CalendarAccount().setCalendarName("calendarName")
                .setCalendarAccountName("calendarAccountName")
                .setCalendarAccountType("calendarAccountType");
        CalendarEvent.init(account);
        ScheduleEventBean bean = new ScheduleEventBean();
        bean.setTitle("titel");
        bean.setDescription("description");
        bean.setLocation("location");
        bean.setStartTime(System.currentTimeMillis() + 500000);
        bean.setEndTime(System.currentTimeMillis() + 500000);
        CalendarEvent.insert(this, "displayName", bean);
    }
}
