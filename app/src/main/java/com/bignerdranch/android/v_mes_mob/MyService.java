package com.bignerdranch.android.v_mes_mob;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private volatile boolean isStopped = false;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Service", "Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final int counter = intent.getIntExtra("Counter", 0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i < counter; i++){
                    if(isStopped){
                        stopSelf();
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d("Service", String.valueOf(i));
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("Service", "Almost destroy");
        isStopped = true;
        super.onDestroy();
    }

}
