package com.fuzple.headup;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.Calendar;

/**
 * Created by user on 2017-12-27.
 */

public class AlarmThread implements Runnable {

    Calendar calendar;
    AlarmManager am;
    PendingIntent pender;
    public AlarmThread(AlarmManager am, Calendar c, PendingIntent pender)
    {
        this.am = am;
        this.pender = pender;
        calendar = c;
    }

    @Override
    public void run() {
        try {
            long oneday = 24 * 60 * 60 * 1000;
            am.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pender);
            Thread.sleep(oneday);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
