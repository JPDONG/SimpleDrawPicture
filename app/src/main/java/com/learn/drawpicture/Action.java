package com.learn.drawpicture;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by dongjiangpeng on 2016/10/19 0019.
 */

public abstract class Action {
    public int mColor;

    Action() {
        mColor = Color.BLACK;
    }

    Action(int color) {
        this.mColor = color;
    }

    public abstract void draw(Canvas canvas);

    public abstract void move(float x, float y);

    public abstract void move(float x0, float y0, float x1, float y1);
}

class MyPath extends Action{

    Path mPath;
    int mSize;

    MyPath(){
        mPath = new Path();
        mSize = 1;
    }

    MyPath(float x, float y, int size, int color){
        super(color);
        this.mSize = size;
        mPath = new Path();
        mPath.moveTo(x,y);
        mPath.lineTo(x,y);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSize);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public void move(float x, float y) {
        mPath.lineTo(x,y);
    }

    @Override
    public void move(float x0, float y0,float x1,float y1) {
        mPath.quadTo(x0,y0,x1,y1);
    }
}
