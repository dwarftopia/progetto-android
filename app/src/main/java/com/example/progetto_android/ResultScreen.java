package com.example.progetto_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ResultScreen extends AppCompatActivity {

    private ConstraintLayout layout_bitmapArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_screen);

        Intent t = getIntent();

        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnReturn = (Button) findViewById(R.id.btnReturn);
        layout_bitmapArea = (ConstraintLayout) findViewById(R.id.layout_bitmapArea);
        TextView lblResult = (TextView) findViewById(R.id.lblResult);

        lblResult.setText(t.getStringExtra("stats"));
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.permissionDenied){
                    if(ContextCompat.checkSelfPermission(ResultScreen.this, MainActivity.myPermission)!= PackageManager.PERMISSION_GRANTED){
                        requestPermission();
                    }
                    else {
                        saveResult();
                        finish();
                        GameScreen.activity.finish();
                    }
                } else {
                    Toast.makeText(ResultScreen.this, "It is not possible to save the result to internal storage without the requested permission.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                GameScreen.activity.finish();
            }
        });
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(ResultScreen.this, MainActivity.myPermission)){   //se true, l'utente ha rifiutato il permesso in passato
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ResultScreen.this);
            builder.setTitle("Storage access");
            builder.setMessage("In order to save game results, the internal storage permission is required.");
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(ResultScreen.this, new String[]{MainActivity.myPermission}, MainActivity.MY_PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
                }
            });
            android.app.AlertDialog alert = builder.create();
            alert.show();
        } else {
            if(!MainActivity.permissionDenied) {  //se il flag è false vuol dire che il permesso non è mai stato chiesto
                ActivityCompat.requestPermissions(ResultScreen.this, new String[]{MainActivity.myPermission}, MainActivity.MY_PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MainActivity.MY_PERMISSIONS_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    MainActivity.permissionDenied=true;
                    return;
                }
        }
    }

    private void saveResult(){
        String fileName = Environment.getExternalStorageDirectory().getPath()
                + "/"
                + GameScreen.activity.getResources().getString(R.string.app_name);
        File f = new File(fileName);
        if(!f.exists()) //se la cartella non esiste la creo
            f.mkdirs();
        fileName += "/game-"
                + Calendar.getInstance().get(Calendar.YEAR)
                + (Calendar.getInstance().get(Calendar.MONTH) + 1)
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                + "-"
                + Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                + Calendar.getInstance().get(Calendar.MINUTE)
                + Calendar.getInstance().get(Calendar.SECOND)
                + ".png";
        Bitmap bitmap = Bitmap.createBitmap(layout_bitmapArea.getWidth(), layout_bitmapArea.getHeight(), Bitmap.Config.ARGB_8888);

        try {
            FileOutputStream out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
        GameScreen.activity.finish();
    }
}