package com.example.progetto_android;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class TimerService extends Service {
    private static int time;
    private static boolean isRunning=false;

    public TimerService() { }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    public void onCreate() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        switch(intent.getIntExtra("time", 1)){
            case 0:
                time=30;
                break;
            case 1:
                time=60;
                break;
            case 2:
                time=180;
                break;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());

                isRunning=true;
                if(MainActivity.sharedPref.getBoolean("audioOn", true)){
                    GameScreen.mediaPlayer.start();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.setCountdownText(time);
                    }
                });

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
        BugService.setRunning(false);
        GameScreen.endGame();
    }

    public static boolean isRunning(){
        return isRunning;
    }
}