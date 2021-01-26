package com.example.progetto_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GameScreen extends AppCompatActivity {

    private static int mode;
    private static int time;
    private static TextView lblCountdown;
    public static ImageView imgBug;
    private TextView lblScore;
    private static int score;
    private static String stats;
    private static AppCompatActivity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent t = getIntent();
        mode = t.getIntExtra("mode", 1);
        time = t.getIntExtra("time", 1);
        score = 0;
        stats = "Mode: " + getResources().getStringArray(R.array.modes)[mode] + "\n";
        stats += "Time: " + getResources().getStringArray(R.array.times)[time] + "\n";
        activity = this;

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
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent timer = new Intent(GameScreen.this, TimerService.class);
                timer.putExtra("time", time);
                Intent bug = new Intent(GameScreen.this, BugService.class);
                bug.putExtra("mode", mode);
                startService(timer);
                startService(bug);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void endGame(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Game end!");
        stats += "Score: " + score;
        String message = stats + "\n\n";
        if(ContextCompat.checkSelfPermission(activity, MainActivity.myPermission)!=PackageManager.PERMISSION_GRANTED){
            message += "It is not possible to save the result to internal storage without the requested permission.";
            builder.setNeutralButton("Return to menu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        } else {
            message += "Do you want to save your result to your phone before returning to the main menu?";
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveScore();
                    dialog.dismiss();
                    activity.finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.finish();
                }
            });
        }
        builder.setMessage(message);

        AlertDialog alert = builder.create();
        alert.show();
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

    private static void saveScore(){
        String fileName = Environment.getExternalStorageDirectory().getPath().toString()
                + "/"
                + activity.getResources().getString(R.string.app_name);
        File f = new File(fileName);
        if(!f.exists()) //se la cartella non esiste la creo
            f.mkdirs();
        fileName += "/game-"
                + Calendar.getInstance().get(Calendar.YEAR)
                + Calendar.getInstance().get(Calendar.MONTH)
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                + "-"
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                + Calendar.getInstance().get(Calendar.MINUTE)
                + Calendar.getInstance().get(Calendar.SECOND)
                //+ ".png";
                + ".txt";
        //Bitmap bitmap = null;

        try {
            FileOutputStream out = new FileOutputStream(fileName);
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}