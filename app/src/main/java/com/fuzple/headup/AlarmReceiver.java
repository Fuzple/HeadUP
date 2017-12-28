package com.fuzple.headup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import java.util.Calendar;

/**
 * Created by user on 2017-12-14.
 */

public class AlarmReceiver extends BroadcastReceiver{

    Vibrator vb;
    Calendar calendar;
    @Override
    public void onReceive(Context context, Intent intent) {

        calendar =Calendar.getInstance();

        for(int i=0;i<=MainActivity.adapter.getCount();i++)
        {
            Listview_item listview_item = (Listview_item)MainActivity.adapter.getItem(i);
            if(calendar.get(Calendar.HOUR_OF_DAY) == Integer.parseInt(listview_item.getTime()) && calendar.get(Calendar.MINUTE) == Integer.parseInt(listview_item.getDay()))
            {
                MainActivity.adapter.deleteItem(i);
                MainActivity.adapter.notifyDataSetChanged();
            }
        }
        vb = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        vb.vibrate(1000);


    }
}
