package com.example.progetto_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Random;

public class GameScreen extends AppCompatActivity {

    private int mode;
    private int time;
    private static TextView lblCountdown;
    public static ImageView imgBug;
    private TextView lblScore;
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        Intent t = getIntent();
        mode = t.getIntExtra("mode", 1);
        switch(t.getIntExtra("time", 1)){
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
        score = 0;

        lblCountdown = (TextView) findViewById(R.id.lblCountdown);
        lblScore = (TextView) findViewById(R.id.lblScore);
        imgBug = (ImageView) findViewById(R.id.imgBug);
        imgBug.setEnabled(false);
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
        builder.setTitle("Inizia la partita!");
        builder.setMessage("Premi OK per iniziare il gioco.");
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

    }
}