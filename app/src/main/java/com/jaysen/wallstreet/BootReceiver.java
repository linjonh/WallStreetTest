package com.jaysen.wallstreet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";

    public BootReceiver() {
    }

    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG,"BootReceiver onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.HOUR_OF_DAY, 11);
//            calendar.set(Calendar.MINUTE, 7);
            int ms = 2 * 1000;
//            PendingIntent pendingIntent = PendingIntent.getService(context, 0, new Intent(context, UpdateService.class), 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(MainActivity.UPDATE_ACTION), 0);
//            alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), ms, pendingIntent);
            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), ms, pendingIntent);
            Log.i(TAG,"BootReceiver onReceive1");
        }
    }
}
