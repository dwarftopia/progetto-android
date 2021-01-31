package com.example.progetto_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        Switch swcAudio = (Switch) findViewById(R.id.swcAudio);
        Button btnBack = (Button) findViewById(R.id.btnBack);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        swcAudio.setChecked(sharedPref.getBoolean("audioOn", true));

        swcAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                editor.putBoolean("audioOn", isChecked);
                editor.apply();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}