package com.example.progetto_android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class DrawView extends View {

    Paint paint = new Paint();

    private void init(){
        paint.setColor(getResources().getColor(R.color.pink));
        paint.setStrokeWidth(10);
    }

    public DrawView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.draw(canvas);
        canvas.drawLine(GameScreen.toPixels(120), GameScreen.toPixels(72), GameScreen.toPixels(200), GameScreen.toPixels(200), paint);
    }

    public void draw(){
        invalidate();
        requestLayout();
    }
}
