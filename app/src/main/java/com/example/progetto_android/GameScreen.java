package com.example.progetto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameScreen extends AppCompatActivity {

    private int mode;
    private int time;
    private static TextView lblCountdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent t = getIntent();
        mode = t.getIntExtra("mode", 1);
        time = t.getIntExtra("time", 1);

        Intent intent = new Intent(GameScreen.this, TimerService.class);

        switch (time){
            case 0:
                intent.putExtra("time", 30);
                break;
            case 1:
                intent.putExtra("time", 60);
                break;
            case 2:
                intent.putExtra("time", 180);
                break;
        }
        startService(intent);

        lblCountdown = (TextView) findViewById(R.id.lblCountdown);
        ImageView imgBug1 = (ImageView) findViewById(R.id.imgBug1);
        setRandomPos(imgBug1);
    }

    private int toPixels(int dp){
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
        return px;
    }

    private void setRandomPos(ImageView img){
        int left = new Random().nextInt(324) + 3;
        int top = new Random().nextInt(494) + 3;
        int right = 380 - left - 50;
        int bottom = 500 - top - 50;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(toPixels(left),toPixels(top),toPixels(right),toPixels(bottom));
        img.setLayoutParams(layoutParams);
    }

    public static void setCountdownText(int t){
        String aux = ((t/60)<10)?"0":"";
        aux += t/60 + ":";
        aux += ((t%60)<10)?"0":"";
        aux += t%60;
        lblCountdown.setText(aux);
    }

    public static void endGame(){

    }
}