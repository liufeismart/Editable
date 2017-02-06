package com.liufeismart.editable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class EditTextLayout extends RelativeLayout {

    public EditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public EditTextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public EditTextLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    private int count;
    private long firClick, secClick;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                EditableLayout layout = (EditableLayout) (this.getParent()
                        .getParent());
                if (layout.getState() == EditableLayout.STATE_EDITABLE) {
                    mOnStateChangedListener
                            .onStateChange(EditableLayout.STATE_EDITABLE);
                    return super.onInterceptTouchEvent(event);
                } else {

                    if (layout.getState() != EditableLayout.STATE_INVALIDATE) {
                        if (firClick != 0
                                && System.currentTimeMillis() - firClick > 300) {
                            count = 0;
                        }
                        count++;
                        if (count == 1) {
                            firClick = System.currentTimeMillis();
                        } else if (count == 2) {
                            secClick = System.currentTimeMillis();
                            if (secClick - firClick < 300) {
                                // 双击事件
                                mOnStateChangedListener
                                        .onStateChange(EditableLayout.STATE_EDITABLE);
                                count = 0;
                                firClick = 0;
                                secClick = 0;
                                return super.onInterceptTouchEvent(event);
                            }

                            count = 0;
                            firClick = 0;
                            secClick = 0;
                        }
                    } else {
                        count = 0;
                    }
                    if (layout.getState() != EditableLayout.STATE_DRAG
                            && layout.getState() != EditableLayout.STATE_DELETE) {
                        layout.setState(EditableLayout.STATE_INIT);
                        mOnStateChangedListener
                                .onStateChange(mOnStateChangedListener.state);
                    }

                }
                break;
//            case MotionEvent.ACTION_MOVE:
//                mOnStateChangedListener
//                .onStateChange(mOnStateChangedListener.state);
//                count = 0;
            default:
                return super.onInterceptTouchEvent(event);
        }
        return true;
    }

    private OnStateChangedListener mOnStateChangedListener;

    public void setOnStateChangedListener(
            OnStateChangedListener onStateChangedListener) {
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
