package com.example.progetto_android;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.LinearLayout;

public class GameScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);

        LinearLayout layout = findViewById(R.id.layout_drawArea);
        MyView myView = new MyView(this);
        layout.addView(myView);
    }
}