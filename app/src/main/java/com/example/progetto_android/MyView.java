package com.example.progetto_android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

public class MyView extends View {
    private Paint paint;

    public MyView(Context context) {
        super(context);

        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.pink));
        paint.setStrokeWidth(20);
        paint.setAntiAlias(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawLine(50,50,100,250,paint);
    }
}
