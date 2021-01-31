package com.example.progetto_android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "lolcat";
    private TextView lblMainTitle;
    private Button btnStartGame;
    private Button btnExitApp;
    private Button btnSettings;
    private int mode;
    private int time;

    public static final int MY_PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE=1;
    public static String myPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static boolean permissionDenied=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lblMainTitle = (TextView) findViewById(R.id.lblMainTitle);
        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnExitApp = (Button) findViewById(R.id.btnExitApp);
        btnSettings = (Button) findViewById(R.id.btnSettings);

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
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsHelp.class));
            }
        });
    }

    private void startupAnimation(){    //scrive il titolo a mo' di "terminale", fade in dei bottoni
        btnStartGame.setAlpha(0);
        btnStartGame.setEnabled(false);
        btnExitApp.setAlpha(0);
        btnExitApp.setEnabled(false);
        btnSettings.setAlpha(0);
        btnSettings.setEnabled(false);
        lblMainTitle.setText("_");

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
                        int n = new Random().nextInt(26) + 100;
                        Thread.sleep(n);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnStartGame.animate().alpha(1.0f).setDuration(750).start();
                            btnExitApp.animate().alpha(1.0f).setDuration(750).start();
                            btnSettings.animate().alpha(1.0f).setDuration(750).start();
                            btnStartGame.setEnabled(true);
                            btnExitApp.setEnabled(true);
                            btnSettings.setEnabled(true);
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
        builder.setMessage("Are you sure you want to quit the app?");
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