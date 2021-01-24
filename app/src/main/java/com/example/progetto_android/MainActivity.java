package com.example.progetto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "lolcat";
    private TextView lblMainTitle;
    private Button btnStartGame;
    private Button btnExitApp;
    private int mode;
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblMainTitle = (TextView) findViewById(R.id.lblMainTitle);
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnExitApp = (Button) findViewById(R.id.btnExitApp);

        btnStartGame.setAlpha(0);
        btnStartGame.setEnabled(false);
        btnExitApp.setAlpha(0);
        btnExitApp.setEnabled(false);
        lblMainTitle.setText("");
        startupAnimation();

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseOptions();
            }
        });
        btnExitApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitApp();
            }
        });
    }

    private void startupAnimation(){    //scrive il titolo a mo' di "terminale", fade in dei bottoni
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String s = getResources().getString(R.string.app_name);
                    String aux = "";

                    for (int i=0; i<s.length(); i++){
                        aux += s.charAt(i);
                        String finalAux = aux;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lblMainTitle.setText(finalAux + "_");
                            }
                        });
                        Thread.sleep(125);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnStartGame.animate().alpha(1.0f).setDuration(1000).start();
                            btnExitApp.animate().alpha(1.0f).setDuration(1000).start();
                            btnStartGame.setEnabled(true);
                            btnExitApp.setEnabled(true);
                            try {
                                Thread.sleep(750);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            lblMainTitle.setText(s);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void chooseOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose your game mode:");
        builder.setCancelable(true);

        builder.setSingleChoiceItems(getResources().getStringArray(R.array.modes), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mode = which;
                dialog.dismiss();
                chooseTime();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void chooseTime(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose your game length:");
        builder.setCancelable(true);

        builder.setSingleChoiceItems(getResources().getStringArray(R.array.times), 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time = which;
                dialog.dismiss();
                Intent t = new Intent(MainActivity.this, GameScreen.class);
                t.putExtra("mode", mode);
                t.putExtra("time", time);
                startActivity(t);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void exitApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Quit");
        builder.setMessage("Are you sure you want to quit the game?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed(){
        exitApp();
    }
}