package com.liufeismart.editable;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

public class EditableImageButton extends ImageButton {


    public EditableImageButton(Context context, AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public EditableImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public EditableImageButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mOnStateChangedListener.onStateChange(mOnStateChangedListener.state);
                break;
        }
        return false;
    }
    
    
    private OnStateChangedListener mOnStateChangedListener;

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.mOnStateChangedListener = onStateChangedListener;
    }

    public static abstract class OnStateChangedListener {
        public OnStateChangedListener(int state) {
            this.state = state;
        }
        public int state;
        public void setState(int state) {
            this.state = state;
        }
        public abstract void onStateChange(int state);
    }
}
