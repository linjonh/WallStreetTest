package com.jaysen.wallstreet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);
            int oneMinutes = 60 * 1000;
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, UpdateService.class), 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), oneMinutes, pendingIntent);
        }
    }
}
