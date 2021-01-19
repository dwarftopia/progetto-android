package com.example.progetto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "lolcat";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView lblMainTitle = (TextView) findViewById(R.id.lblMainTitle);
        Button btnStartGame = (Button) findViewById(R.id.btnStartGame);
        Button btnExitApp = (Button) findViewById(R.id.btnExitApp);

        btnStartGame.setAlpha(0);
        btnStartGame.setEnabled(false);
        btnExitApp.setAlpha(0);
        btnExitApp.setEnabled(false);

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
                        Thread.sleep(150);
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
}