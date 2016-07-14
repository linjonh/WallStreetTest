package com.jaysen.wallstreet;

import android.app.AlarmManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.jaysen.wallstreet.util.Message;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Message>> {

    private static final String TAG = "MainActivity";
    private RecyclerView         mRecyclerView;
    private StockRecyclerAdapter mAdapter;
    private UpdateReceiver       mUpdateReceiver;
    private AlarmManager         mAlarmManager;
    private PendingIntent        mPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        mAdapter = new StockRecyclerAdapter();
        mRecyclerView
                .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
//        getLoaderManager().initLoader(0, null, this);
        new StockAsyncTask(this, mAdapter).execute();
        setAlarm();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getLoaderManager().destroyLoader(0);
//        getLoaderManager().restartLoader(0, null, this);

//        Intent intent = new Intent(this, UpdateService.class);
//        bindService(intent, mConnection, 0);
        if (mUpdateReceiver == null) {
            mUpdateReceiver = new UpdateReceiver();
        }
        registerReceiver(mUpdateReceiver, new IntentFilter(UPDATE_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(mConnection);
//        if (mIRemoteService != null) {
//            try {
//                mIRemoteService.getData();
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//        }

        unregisterReceiver(mUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAlarmManager.cancel();
    }

    @Override
    public Loader<ArrayList<Message>> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case 0:
                return new StockAsyncLoader(this);
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Message>> loader, ArrayList<Message> data) {
        mAdapter.setmDatasets(data);
        Log.i(TAG, "onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Message>> loader) {
        mAdapter.setmDatasets(null);
        Log.i(TAG, "onLoaderReset");
    }

//    IStockAidlInterface mIRemoteService;
//    private ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            mIRemoteService = IStockAidlInterface.Stub.asInterface(service);
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            Log.e(TAG, "Service has unexpectedly disconnected");
//            mIRemoteService = null;
//        }
//    };


//    JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//
//    private void startJobScheduler() {
//        JobInfo jobInfo = new JobInfo.Builder(0, new ComponentName(this, UpdateService.class))
//                .setPeriodic(60 * 1000)
//                .build();
//        jobScheduler.schedule(jobInfo);
//    }

    public static final String UPDATE_ACTION = MainActivity.class.getPackage() + "_UPDATE_ACTION";

    class UpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "UpdateReceiver onReceive");
            if (intent.getAction().equals(UPDATE_ACTION)) {
                new StockAsyncTask(context, mAdapter).execute();
            }
        }
    }

    private void setAlarm() {
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
//            calendar.set(Calendar.HOUR_OF_DAY, 9);
//            calendar.set(Calendar.MINUTE, 0);
        int ms = 60 * 1000;
        mPendingIntent = PendingIntent
                .getBroadcast(this, 0, new Intent(MainActivity.UPDATE_ACTION), 0);
        mAlarmManager
                .setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), ms, mPendingIntent);
        Log.i(TAG, "setAlarm");
    }
}
