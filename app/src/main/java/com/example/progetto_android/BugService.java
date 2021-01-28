package com.example.progetto_android;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.Timer;

public class BugService extends Service {

    private int mode;
    private int intervalBase;
    private static boolean tapped=false;

    public BugService() { }

    @Override
    public IBinder onBind(Intent intent) { return null; }

    public void onCreate() { }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mode = intent.getIntExtra("mode", 1);
        switch(mode){
            case 0:
                intervalBase = 1500;
                break;
            case 2:
                intervalBase = 500;
                break;
            case 1:
            default:
                intervalBase = 1000;
                break;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler(Looper.getMainLooper());

                while(TimerService.isRunning()){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            moveBug();
                        }
                    });
                    int t = new Random().nextInt(intervalBase) + 1000;
                    while(!tapped && t>0){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        t-=10;
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            disappearBug();
                        }
                    });
                    t = new Random().nextInt(500) + intervalBase;
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return Service.START_STICKY;
    }

    public void onDestroy(){
    }

    private void moveBug(){
        int left = new Random().nextInt(324) + 3;
        int top = new Random().nextInt(444) + 3;
        int right = 330 - left;
        int bottom = 450 - top;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(GameScreen.toPixels(left),GameScreen.toPixels(top),GameScreen.toPixels(right),GameScreen.toPixels(bottom));
        GameScreen.imgBug.setLayoutParams(layoutParams);
        int angle = new Random().nextInt(360);
        GameScreen.imgBug.setRotation(angle);
        GameScreen.imgBug.animate().alpha(1.0f).setDuration(500).start();
        GameScreen.imgBug.setEnabled(true);
    }

    private void disappearBug(){
        GameScreen.imgBug.setEnabled(false);
        GameScreen.imgBug.animate().alpha(0.0f).setDuration(500).start();
        tapped=false;
    }

    public static void imageTapped(){
        tapped=true;
    }
}