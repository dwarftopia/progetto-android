package com.example.progetto_android;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsHelp extends AppCompatActivity {

    private Switch swcAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_help);

        swcAudio = (Switch) findViewById(R.id.swcAudio);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        swcAudio.setChecked(sharedPref.getBoolean("audioOn", true));

        swcAudio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("audioOn", isChecked);
                editor.apply();
            }
        });
    }
}