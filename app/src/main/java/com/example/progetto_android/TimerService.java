package com.example.progetto_android;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class TimerService extends Service {
    private static int time;
    private static boolean isRunning=false;

    public TimerService() { }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    public void onCreate() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        time = intent.getIntExtra("time", 60);
        isRunning=true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());

                do{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(isRunning){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                GameScreen.setCountdownText(time);
                            }
                        });
                        time--;
                    }
                } while(isRunning && time>0);
                stopSelf();
            }
        }).start();
        return Service.START_STICKY;
    }

    public void onDestroy() {
        isRunning=false;

    }

    public static boolean isRunning(){
        return isRunning;
    }
}