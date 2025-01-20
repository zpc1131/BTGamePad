package com.example.btgamepad.customize;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.example.btgamepad.Config;

public class MoveButton extends RelativeLayout {


    private OnClickCallBackListener mListener;
    private float mOriginalX;
    private float mOriginalY;
    private float mOriginalRawX;
    private float mOriginalRawY;
    public float touchInitX;
    public float touchInitY;
    boolean isMove = false;
    public float moveX;
    public float moveY;

    public MoveButton(Context context) {
        super(context);
    }

    public MoveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Config.s_keyboard) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    moveX = 0;
                    moveY = 0;
                    mOriginalX = getX();
                    mOriginalY = getY();
                    mOriginalRawX = event.getRawX();
                    mOriginalRawY = event.getRawY();

                    touchInitX = event.getX();
                    touchInitY = event.getY();
                    isMove = false;

                    if (mListener != null) {
                        mListener.onActionDown(event);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (Math.abs(event.getX() - touchInitX) > 10 || Math.abs(event.getY() - touchInitY) > 10) {
                        isMove = true;
                        moveX = mOriginalX + event.getRawX() - mOriginalRawX;
                        setX(moveX);
                        moveY = mOriginalY + event.getRawY() - mOriginalRawY;
                        setY(mOriginalY + event.getRawY() - mOriginalRawY);
                        if (mListener != null){
                            mListener.onActionMove(moveX, moveY);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    if (!isMove) {

                    }
                    break;
            }
            return true;
        }
        return false;
    }

    public void setOnClickCallBackListener(OnClickCallBackListener listener) {
        this.mListener = listener;
    }


    public interface OnClickCallBackListener {
        void onActionDown(MotionEvent event);

        //void onActionUp();

        void onActionMove(float moveX, float moveY);
    }

    public void setPosition(float x, float y) {
        this.setX(x);
        this.setY(y);
    }


}
