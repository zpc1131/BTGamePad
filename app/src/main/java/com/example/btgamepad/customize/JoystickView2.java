package com.example.btgamepad.customize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.btgamepad.Config;
import com.example.btgamepad.R;

public class JoystickView2 extends View {
    public float currentX ;
    public float currentY ;
    public float initX ;
    public float initY ;
    public float currentX2 ;
    public float currentY2 ;
    float lastX;
    float lastY;
    float lastVibX;
    float lastVibY;
    public int viewSize;
    private float viewSizeW;
    private float viewSizeH;
    private float vibratorRanger;
    public int pointSize;
    private float maxRanger=150;
    public float maxRanger2 = 150;
    public Context context;
    public Bitmap map;
    private Bitmap mDirectionBmp;
    public static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public OnJoystickListener mListener;
    public JoystickView2(Context context) {
        super(context);
        this.context = context;
    }

    public JoystickView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public JoystickView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }
    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = (float)newWidth / (float)width;
        float scaleHeight = (float)newHeight / (float)height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }


    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        this.viewSize = this.getMeasuredWidth();
        int var10000 = this.viewSize;
        this.pointSize = var10000 / 3;
        viewSizeW=this.getMeasuredWidth();
        viewSizeH=this.getMeasuredHeight();
        vibratorRanger=viewSizeH/6;
        if (maxRanger>viewSizeH/4)
        {
            maxRanger=viewSizeH/4;
        }
        if (maxRanger2>viewSizeH/3)
        {
            maxRanger2=viewSizeH/3;
        }

        this.init();
    }
    public void init() {
        currentX = currentX2=initX=lastX=viewSizeW/2;
        currentY= currentY2 =initY=lastY=viewSizeH/2;
        int var1 = this.pointSize;
        Bitmap var10000 = BitmapFactory.decodeResource(this.context.getResources(), R.mipmap.joystick_round);
        Bitmap mDirectionBmp1 = BitmapFactory.decodeResource(this.context.getResources(), R.mipmap.joystick_down);
        this.mDirectionBmp = this.zoomImg(mDirectionBmp1, viewSize, viewSize);
        this.map = this.zoomImg(var10000, var1, var1);

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        paint.setDither(true);
        canvas.drawBitmap(this.map, currentX2 - this.pointSize / 2, currentY2 - this.pointSize / 2, paint);
        if (currentX != initX && currentY != initY) {
            // 画方向指示箭头
            float rotationDegree = (float) RoundCalculator.calTwoPointAngleDegree(initX, initY,
                    currentX, currentY);
            Matrix matrix = new Matrix();
            int offsetX = mDirectionBmp.getWidth() / 2;
            int offsetY = mDirectionBmp.getHeight() / 2;
            matrix.postTranslate(-offsetX, -offsetY);
            matrix.postRotate(270 - rotationDegree);
            matrix.postTranslate(initX - mDirectionBmp.getWidth()/2 + offsetX, initY - mDirectionBmp.getHeight()/2 + offsetY);
            canvas.drawBitmap(mDirectionBmp, matrix, null);
        }

    }

    public void setOnJoystickListener(OnJoystickListener listener) {
        this.mListener = listener;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(Config.s_keyboard){
            return false;
        }
        if (event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE){

            /*if (event.getAction()==MotionEvent.ACTION_DOWN) {
                initX2 = event.getX();
                initY2 = event.getY();
            }*/
            this.currentX = event.getX();
            this.currentY = event.getY();

            this.currentX2 = event.getX();
            this.currentY2 = event.getY();

            float tr = (float) RoundCalculator.calTwoPointDistant(initX, initY, event.getX(), event.getY());
            if (tr>maxRanger)
            {
                float dotCenterOnShow[] = RoundCalculator.calPointLocationByAngle(
                        initX, initY, event.getX(), event.getY(), maxRanger);
                this.currentX = dotCenterOnShow[0];
                this.currentY = dotCenterOnShow[1];
                Log.e("currentXY1","...x="+currentX2+",y="+currentY2+",maxRanger2="+maxRanger2);
            }


            float tr2 = (float) RoundCalculator.calTwoPointDistant(initX, initY, event.getX(), event.getY());
            if (tr2>maxRanger2)
            {
                float dotCenterOnShow[] = RoundCalculator.calPointLocationByAngle(
                        initX, initY, event.getX(), event.getY(), maxRanger2);
                this.currentX2 = dotCenterOnShow[0];
                this.currentY2 = dotCenterOnShow[1];

            }

        }else {
            this.currentX =initX;
            this.currentY =initY;

            this.currentX2 = initX;
            this.currentY2 = initY;
        }



        if (mListener!=null){
            //if (Math.abs(currentX-lastX)>3||Math.abs(currentY-lastY)>3){

            mListener.onPosition((currentX-initX)/(float)maxRanger,(currentY-initY)/(float)maxRanger);

            lastX=currentX;
            lastY=currentY;
            //  }
            if (Math.abs(currentX-lastVibX)>vibratorRanger||Math.abs(currentY-lastVibY)>vibratorRanger){//用来震动
                this.mListener.onVibrator();
                lastVibX=currentX;
                lastVibY=currentY;
            }

        }
        this.invalidate();
        return true;
    }

    public interface OnJoystickListener {
        void onPosition(float var1, float var2);

        void onVibrator();
    }

}
