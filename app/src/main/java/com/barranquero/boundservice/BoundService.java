package com.barranquero.boundservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Chronometer;

/**
 * Created by usuario on 17/02/17
 * BoundService
 */
public class BoundService extends Service {
    private static final String TAG = "BoundService";
    private IBinder mBinder;
    private Chronometer mChrono;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mBinder = new TheBinder();
        mChrono = new Chronometer(this);
        mChrono.setBase(SystemClock.elapsedRealtime());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        mChrono.start();
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        mChrono.stop();
        return super.onUnbind(intent);
    }

    public String getTimeStamp() {
        long elapsedMillis = SystemClock.elapsedRealtime() - mChrono.getBase();
        /*SimpleDateFormat dateFormat = new SimpleDateFormat("hh:MM:ss");

        return dateFormat.format(new Date(elapsedMillis));*/
        int hours = (int) (elapsedMillis / 3600000);
        int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
        int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
        int millis = (int) (elapsedMillis - hours * 3600000 - minutes * 60000 - seconds * 1000);

        return hours + ":" + minutes + ":" + seconds + ":" + millis;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    public class TheBinder extends Binder {

        BoundService getService() {
            return BoundService.this;
        }
    }
}
