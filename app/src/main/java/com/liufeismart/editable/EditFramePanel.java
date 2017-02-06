package com.liufeismart.editable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.liufeismart.editable.util.DensityUtils;
import com.liufeismart.editable.util.KeyBoardUtils;
import com.liufeismart.editable.util.TextUtil;


public class EditFramePanel extends RelativeLayout {

    private EditableLayout mCurrentEdit;
    private PointF mPreMovePointF = new PointF();
    private PointF mPreMovePointFRaw = new PointF();
    private PointF mCurMovePointF = new PointF();
    private PointF mCurMovePointFRaw = new PointF();
    private Context context;

    public EditFramePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context2) {
        this.context = context2;
        detector = new GestureDetector(new SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {

                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                mCurMovePointFRaw.set(e2.getRawX(), e2.getRawY());
                mCurMovePointF.set(e2.getX(), e2.getY());
                if (mCurrentEdit != null) {
                    switch (mCurrentEdit.getState()) {
                        case EditableLayout.STATE_MOVE:
                            mCurrentEdit.move(mCurMovePointFRaw.x
                                    - mPreMovePointFRaw.x, mCurMovePointFRaw.y
                                    - mPreMovePointFRaw.y);
                            break;
                        case EditableLayout.STATE_DRAG:
                            mCurrentEdit.scaleAndRotate(new PointF(e1.getX(),
                                    e1.getY()), mPreMovePointF, mCurMovePointF);
                            break;
                    }
                    mPreMovePointF.set(mCurMovePointF);
                    mPreMovePointFRaw.set(mCurMovePointFRaw);
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                mPreMovePointFRaw.set(e.getRawX(), e.getRawY());
                mPreMovePointF.set(e.getX(), e.getY());
                return true;
            }
        });

    }

    public EditFramePanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditFramePanel(Context context) {
        super(context);
        init(context);
    }

    public void addView(final Context context) {
        final EditableLayout editable = new EditableLayout(context);
        editable.setOnSelectedListenr(new EditableLayout.OnSelectedListenr() {

            @Override
            public void onSelected(EditableLayout editable) {
                if (mCurrentEdit == null
                        || mCurrentEdit.getState() != EditableLayout.STATE_EDITABLE) {
                    setCurrentEdit(editable);
                } else {
                    setCurrentEdit(null);
                }

            }

        });
        editable.initialHeight = DensityUtils.dp2px(context, 50 + 30);
        editable.initialWidth = DensityUtils.dp2px(context, 50 + 30);
        editable.getEditText().addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (mCurrentEdit != null) {
                    int afterLen = TextUtil.getTextWidth(s.toString(),
                            mCurrentEdit.getEditText(), context);

                    android.view.ViewGroup.LayoutParams layoutParams = mCurrentEdit
                            .getEditText().getLayoutParams();
                    layoutParams.width = (int) (afterLen + mCurrentEdit.getEditText().getTextSize() + mCurrentEdit.getEditText().getPaddingLeft() * 2); //(afterLen - beforeLen);
                    requestLayout();

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editable.setState(EditableLayout.STATE_EDITABLE);

        this.addView(editable);
        setCurrentEdit(editable);
    }

    private GestureDetector detector;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentEdit != null) {
            detector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (mCurrentEdit != null) {
                        if (mCurrentEdit.getState() == EditableLayout.STATE_INIT
                                || mCurrentEdit.getState() == EditableLayout.STATE_EDITABLE) {

                            setCurrentEdit(null);
                        } else {
                            mCurrentEdit.initState();

                        }

                    }
                    break;

            }
            ListViewForEditable.canTouch = false;
            return true;
        }
        ListViewForEditable.canTouch = true;
        return super.onTouchEvent(event);
    }


    public void setCurrentEdit(EditableLayout editable) {

        if (mCurrentEdit != null && mCurrentEdit != editable) {
            mCurrentEdit.setState(EditableLayout.STATE_INVALIDATE);
            if (editable != null
                    && editable.getState() == EditableLayout.STATE_EDITABLE) {
                KeyBoardUtils.openKeybord(editable.getEditText(), context);
            }
        }
//        if(editable!=null) {
//
//            this.removeView(editable);
//            this.addView(editable, getChildCount());
//        }
        mCurrentEdit = editable;

    }

    public void setCurrentEdit() {

        if (mCurrentEdit.getState() == EditableLayout.STATE_EDITABLE) {
            mCurrentEdit.setState(EditableLayout.STATE_INVALIDATE);
            mCurrentEdit = null;
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // super.onLayout(changed, l, t, r, b);
        // 动态获取子View实例
        for (int i = 0, size = getChildCount(); i < size; i++) {
            EditableLayout child = (EditableLayout) getChildAt(i);
            if (child.getVisibility() != GONE) {

                if (child.l != -1) {
                    int oldWidth = child.r - child.l;
                    int oldHeight = child.b - child.t;
                    child.layout(child.l, child.t,
                            child.l + child.getMeasuredWidth(),
                            child.t + child.getMeasuredHeight());
                    child.l -= (child.initialWidth - oldWidth) / 2;
                    child.t -= (child.initialHeight - oldHeight) / 2;
                    child.layout(child.l, child.t,
                            child.l + child.getMeasuredWidth(),
                            child.t + child.getMeasuredHeight());
                } else {
                    int initalX = ((r - l) - child.initialWidth) / 2;
                    int initalY = b - child.initialHeight
                            - DensityUtils.dp2px(context, 12);
                    child.layout(initalX, initalY,
                            initalX + child.getMeasuredWidth(),
                            initalY + child.getMeasuredHeight());
                }
            }
        }
    }


}
