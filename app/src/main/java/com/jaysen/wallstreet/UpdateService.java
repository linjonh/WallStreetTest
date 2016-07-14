package com.jaysen.wallstreet;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by lin on 2016/7/13.
 */
public class UpdateService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO load data
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }


    IStockAidlInterface.Stub mIBinder =new IStockAidlInterface.Stub() {
        @Override
        public List<Message> getData() throws RemoteException {
            return null;
        }
    };
}
