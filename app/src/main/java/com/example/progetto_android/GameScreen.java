package com.example.progetto_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameScreen extends AppCompatActivity {

    private static int mode;
    private static int time;
    private static TextView lblCountdown;
    public static ImageView imgBug;
    private TextView lblScore;
    private static int score;
    private static String stats;
    public static AppCompatActivity activity;
    private static boolean stopped;
    public static MediaPlayer mediaPlayer = null;
    private static boolean audioOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent t = getIntent();
        mode = t.getIntExtra("mode", 1);
        time = t.getIntExtra("time", 1);
        score = 0;
        stats = "Mode:\n        " + getResources().getStringArray(R.array.modes)[mode] + "\n";
        stats += "Time:\n        " + getResources().getStringArray(R.array.times)[time] + "\n";
        activity = this;
        stopped = false;

        lblCountdown = (TextView) findViewById(R.id.lblCountdown);
        lblScore = (TextView) findViewById(R.id.lblScore);
        imgBug = (ImageView) findViewById(R.id.imgBug);
        imgBug.setEnabled(false);
        imgBug.setAlpha(0.0f);
        imgBug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BugService.imageTapped();
                score++;
                lblScore.setText("Score = " + score);
            }
        });
        setCountdownText(time);
        lblScore.setText("Score = 0");

        audioOn = MainActivity.sharedPref.getBoolean("audioOn", true);

        if(audioOn){
            mediaPlayer = MediaPlayer.create(this, R.raw.game_music);
            mediaPlayer.setLooping(true);
        }
        startGame();
    }


    public static void setCountdownText(int t){
        String aux = ((t/60)<10)?"0":"";
        aux += t/60 + ":";
        aux += ((t%60)<10)?"0":"";
        aux += t%60;
        lblCountdown.setText(aux);
    }

    private void startGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
        builder.setTitle("Game start!");
        builder.setMessage("Press OK to start the game.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent timer = new Intent(GameScreen.this, TimerService.class);
                timer.putExtra("time", time);
                Intent bug = new Intent(GameScreen.this, BugService.class);
                bug.putExtra("mode", mode);
                startService(timer);
                startService(bug);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void endGame(){
        if(audioOn){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        if(!stopped){
            stats += "Score:\n        " + score;
            Intent t = new Intent(GameScreen.activity, ResultScreen.class);
            t.putExtra("stats", stats);
            activity.startActivity(t);
        }
    }

    @Override
    public void onBackPressed(){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GameScreen.this);
        builder.setTitle("Stop");
        builder.setMessage("Are you sure you want to interrupt the game?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopped=true;
                stopService(new Intent(GameScreen.this, TimerService.class));
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    public static int toPixels(int dp){
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                activity.getResources().getDisplayMetrics()
        );
        return px;
    }
}