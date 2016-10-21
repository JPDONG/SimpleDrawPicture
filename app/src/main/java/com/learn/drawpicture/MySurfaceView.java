package com.learn.drawpicture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;


/**
 * Created by dongjiangpeng on 2016/10/20 0020.
 */

public class MySurfaceView extends SurfaceView {

    private final String TAG = "MySurfaceView";
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private float mCurrentSize = 10;
    private ArrayList<Action> mActions;
    private ActionType mType = ActionType.Path;
    private Action mCurrentAction;
    private int mCurrentColor = Color.BLACK;

    public enum ActionType {Point, Path}

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "MySurfaceView: MySurfaceView(Context context, AttributeSet attrs)");
        init();
    }

    public MySurfaceView(Context context) {
        super(context);
        Log.d(TAG, "MySurfaceView: MySurfaceView(Context context)");
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d(TAG, "MySurfaceView: MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr)");
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.d(TAG, "MySurfaceView: MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)");
        init();
    }

    private void init() {
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
        this.setFocusable(true);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(mCurrentSize);
    }

    SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.d(TAG, "surfaceCreated: ");
            Canvas mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            mActions = new ArrayList<Action>();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.d(TAG, "surfaceChanged: ");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "surfaceDestroyed: ");
        }
    };
    float mPreX = 0, mPreY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int mTouchAction = event.getAction();
        if (mTouchAction == MotionEvent.ACTION_CANCEL) {
            return false;
        }

        /*float mTouchX = event.getRawX();
        float mTouchY = event.getRawY();*/

        float mTouchX = event.getX();
        float mTouchY = event.getY();


        switch (mTouchAction) {
            case MotionEvent.ACTION_DOWN:
                setCurrentAction(mTouchX, mTouchY);
                mPreX = mTouchX;
                mPreY = mTouchY;
                break;
            case MotionEvent.ACTION_MOVE:

                Canvas mCanvas = mSurfaceHolder.lockCanvas();
                mCanvas.drawColor(Color.WHITE);
                for (Action action : mActions) {
                    action.draw(mCanvas);
                }
                //mCurrentAction.move(mTouchX,mTouchY);
               /*final float dx = Math.abs(mTouchX - mPreX);
                final float dy = Math.abs(mTouchY - mPreY);*/
                /**
                 * 曲线优化方案1：采用qualTo()
                 */
                /*float mControlX = (mPreX + mTouchX) / 2;
                float mControlY = (mPreY + mTouchY) / 2;
                mCurrentAction.move(mControlX, mControlY, mTouchX, mTouchY);
                mPreX = mTouchX;
                mPreY = mTouchY;*/

                /**
                 * 曲线优化方案2：采用getHistorySize()
                 */
                int mHistorySize = event.getHistorySize();
                Log.d(TAG, "onTouchEvent: " + mHistorySize);
                for(int i = 0;i < mHistorySize;i++){
                    float mHistoryX = event.getHistoricalX(i);
                    float mHistoryY = event.getHistoricalY(i);
                    mCurrentAction.move(mHistoryX,mHistoryY);
                }
                mCurrentAction.move(mTouchX,mTouchY);

                mCurrentAction.draw(mCanvas);
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
                break;
            case MotionEvent.ACTION_UP:
                mActions.add(mCurrentAction);
                mCurrentAction = null;
                break;
            default:
                break;
        }
        return true;
    }

    private void setCurrentAction(float mTouchX, float mTouchY) {
        switch (mType) {
            case Path:
                mCurrentAction = new MyPath(mTouchX, mTouchY, (int) mCurrentSize, mCurrentColor);
                break;
        }
    }

    public boolean back(){
        if(mActions != null && mActions.size() > 0){
            mActions.remove(mActions.size() - 1);
            Canvas mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.WHITE);
            for(Action action:mActions){
                action.draw(mCanvas);
            }
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            return true;
        }
        return false;
    }

    @Override
    public SurfaceHolder getHolder() {
        Log.d(TAG, "getHolder: ");
        return super.getHolder();
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow: ");
        super.onAttachedToWindow();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        Log.d(TAG, "onWindowVisibilityChanged: ");
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    public void setVisibility(int visibility) {
        Log.d(TAG, "setVisibility: ");
        super.setVisibility(visibility);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow: ");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure: ");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean gatherTransparentRegion(Region region) {
        Log.d(TAG, "gatherTransparentRegion: ");
        return super.gatherTransparentRegion(region);
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d(TAG, "draw: ");
        super.draw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d(TAG, "dispatchDraw: ");
        super.dispatchDraw(canvas);
    }

    @Override
    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        Log.d(TAG, "setZOrderMediaOverlay: ");
        super.setZOrderMediaOverlay(isMediaOverlay);
    }

    @Override
    public void setZOrderOnTop(boolean onTop) {
        Log.d(TAG, "setZOrderOnTop: ");
        super.setZOrderOnTop(onTop);
    }

    @Override
    public void setSecure(boolean isSecure) {
        Log.d(TAG, "setSecure: ");
        super.setSecure(isSecure);
    }


}
