package com.liufeismart.editable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liufeismart.editable.util.KeyBoardUtils;


public class EditableLayout extends LinearLayout {

    private Context context;

    public boolean firstLayout = true;
    public int initialWidth = 0;
    public int initialHeight = 0;
    public int l = -1, t = -1, r = -1, b = -1;
    private float distance;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.l = l;
        this.t = t;
        this.r = r;
        this.b = b;
        initCenterPoint(l, t, (r - l), (b - t));

    }

    private void initCenterPoint(int left, int top, int width, int height) {
        initialWidth = width;
        initialHeight = height;
        mCenterPoint.set(left + width / 2, top + height / 2);
        distance = (float) Math.sqrt(initialWidth * initialWidth
                + initialHeight * initialHeight) / 2;
    }

    private EditTextLayout layout_content;
    private EditText et_content;
    private EditableImageButton ibn_delete;
    private EditableImageButton ibn_drag;
    private RelativeLayout layout_line;

    // 中心点
    public PointF mCenterPoint = new PointF();

    public static final int STATE_INIT = 0;
    public static final int STATE_MOVE = 1;
    public static final int STATE_DRAG = 2;
    public static final int STATE_DELETE = 3;
    public static final int STATE_EDITABLE = 4;// 编辑文本
    public static final int STATE_INVALIDATE = 5;
    private int state = STATE_INIT;

    /**
     * 图片的最大缩放比例
     */
    public static final float MAX_SCALE = 10.0f;

    /**
     * 图片的最小缩放比例
     */
    public static final float MIN_SCALE = 0.65f;

    public EditText getEditText() {
        return et_content;
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public EditableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public EditableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EditableLayout(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        View root = LayoutInflater.from(context).inflate(
                R.layout.view_editablelayout, this);
        layout_content = (EditTextLayout) this
                .findViewById(R.id.layout_content);
        layout_content
                .setOnStateChangedListener(new EditTextLayout.OnStateChangedListener(
                        STATE_MOVE) {

                    @Override
                    public void onStateChange(int state) {
                        if (state == STATE_MOVE) {
                            EditableLayout.this.setStateInInit(state);
                        } else if (state == STATE_EDITABLE) {
                            EditableLayout.this.setState(state);
                        }
                    }
                });
        et_content = (EditText) root.findViewById(R.id.et_content);
        // et_content.setHorizontallyScrolling(false);
        ibn_delete = (EditableImageButton) root.findViewById(R.id.ibn_delete);
        ibn_delete
                .setOnStateChangedListener(new EditableImageButton.OnStateChangedListener(
                        STATE_DELETE) {

                    @Override
                    public void onStateChange(int state) {

                        EditableLayout.this.setState(state);
                    }
                });
        ibn_drag = (EditableImageButton) root.findViewById(R.id.ibn_drag);
        ibn_drag.setOnStateChangedListener(new EditableImageButton.OnStateChangedListener(
                STATE_DRAG) {

            @Override
            public void onStateChange(int state) {
                EditableLayout.this.setStateInInit(state);
            }
        });
        layout_line = (RelativeLayout) root.findViewById(R.id.layout_line);
    }

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (state != STATE_INIT) {
                    scaleTemp = -1f;
//                    degreeTemp = -1f;
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (this.getState() == STATE_DELETE) {
                                this.setState(STATE_DELETE);
                                ((ViewGroup) (this.getParent()))
                                        .removeView(this);
                                return true;
                            } else {
                                selecedListener.onSelected(this);
                                // this.setState(STATE_INIT);
                            }
                            break;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public void setStateInInit(int state) {
        if (this.state == STATE_INIT) {
            setState(state);
        }
    }

    @SuppressWarnings("deprecation")
    public void setState(int state) {

        this.state = state;
        switch (state) {
            case STATE_INVALIDATE:
                String string = et_content.getText().toString();
                if (string.length() > 0) {

                    et_content.setCursorVisible(false);
                    //
                    setEditable(false);
                    ibn_delete.setVisibility(View.GONE);
                    ibn_drag.setVisibility(View.GONE);
                    layout_line.setBackgroundDrawable(null);
                } else {
                    ViewGroup parent = (ViewGroup) this.getParent();
                    KeyBoardUtils.closeKeybord(et_content, context);
                    if (parent != null) {
                        parent.removeView(this);
                    }
                }
                break;
            case STATE_EDITABLE:
                ibn_delete.setVisibility(View.GONE);
                ibn_drag.setVisibility(View.GONE);
                setEditable(true);
                // et_content.setSelection(et_content.getText().toString().length());
                layout_line.setBackgroundDrawable(null);

                break;
            case STATE_INIT:
            case STATE_DRAG:
            case STATE_MOVE:
                setEditable(false);
                ibn_delete.setVisibility(View.VISIBLE);
                ibn_drag.setVisibility(View.VISIBLE);
                layout_line
                        .setBackgroundResource(R.drawable.rectangle_dotted_line);
                et_content.setCursorVisible(true);
                break;
            case STATE_DELETE:
                KeyBoardUtils.closeKeybord(et_content, context);
                break;
        }

    }

    private void setEditable(boolean editable) {
        et_content.setCursorVisible(editable);
        et_content.setFocusable(false);
        if (!editable) {// 隐藏键盘
            KeyBoardUtils.closeKeybord(et_content, context);
        } else {
            et_content.setFocusableInTouchMode(true);
            et_content.setFocusable(true);
            et_content.requestFocus();
            if (!KeyBoardUtils.showKeybord(context, et_content)) {
                KeyBoardUtils.openKeybord(et_content, context);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void initState() {
        if (this.state != STATE_EDITABLE) {
            this.state = STATE_INIT;
            rotationReset();
        }
    }

    public int getState() {
        return state;
    }


    private OnSelectedListenr selecedListener;

    public void setOnSelectedListenr(OnSelectedListenr selecedListener) {
        this.selecedListener = selecedListener;
    }

    public interface OnSelectedListenr {
        void onSelected(EditableLayout view);
    }

    private float scaleTemp = -1;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void scale(PointF mPreMovePointF, PointF mCurMovePointF) {
        // int width = this.getWidth();
        // int height = this.getHeight();
        // 图片某个点到图片中心的距离
        float bitmapToCenterDistance = EditableUtil.distance4PointF(
                mCenterPoint, mPreMovePointF);

        // 移动的点到图片中心的距离
        float moveToCenterDistance = EditableUtil.distance4PointF(mCenterPoint,
                mCurMovePointF);
        // 计算缩放比例
        float scale = (moveToCenterDistance - bitmapToCenterDistance)
                / (distance);
        scale = (moveToCenterDistance + ibn_drag.getWidth() * scale - bitmapToCenterDistance) / (distance);
        if (scaleTemp == -1f) {
            scaleTemp = this.getScaleX();
            scale = this.getScaleX() + scale;
        } else {
            scale = scaleTemp + scale;
        }
        // //缩放比例的界限判断
        if (scale <= MIN_SCALE) {
            scale = MIN_SCALE;
        } else if (scale >= MAX_SCALE) {
            scale = MAX_SCALE;
        }
        this.setScaleX(scale);
        this.setScaleY(scale);
        ibn_delete.setScaleX(1 / scale);
        ibn_delete.setScaleY(1 / scale);
        ibn_drag.setScaleX(1 / scale);
        ibn_drag.setScaleY(1 / scale);
        this.invalidate();
    }

//    private float degreeTemp = -1f;

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    private void rotate(PointF mPreMovePointF, PointF mCurMovePointF) {
//         角度
         double a = EditableUtil.distance4PointF(mCenterPoint,
         mPreMovePointF);
         double b = EditableUtil.distance4PointF(mPreMovePointF,
         mCurMovePointF);
         double c = EditableUtil.distance4PointF(mCenterPoint,
         mCurMovePointF);
        
         double cosb = (a * a + c * c - b * b) / (2 * a * c);
        
         if (cosb >= 1) {
         cosb = 1f;
         }
        
         double radian = Math.acos(cosb);
         float newDegree = (float) EditableUtil.radianToDegree(radian);
        
         // center -> proMove的向量， 我们使用PointF来实现
         PointF centerToProMove = new PointF(
         (mPreMovePointF.x - mCenterPoint.x),
         (mPreMovePointF.y - mCenterPoint.y));
        
         // center -> curMove 的向量
         PointF centerToCurMove = new PointF(
         (mCurMovePointF.x - mCenterPoint.x),
         (mCurMovePointF.y - mCenterPoint.y));
        
         // 向量叉乘结果, 如果结果为负数， 表示为逆时针， 结果为正数表示顺时针
         float result = centerToProMove.x * centerToCurMove.y
         - centerToProMove.y * centerToCurMove.x;
        
         if (result < 0) {
         newDegree = -newDegree;
         }
         float mDegree = getRotation();
         mDegree = mDegree + newDegree;
        
         this.setRotation(mDegree);

    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void scaleAndRotate(PointF mMoveDownPointF, PointF mPreMovePointF,
                               PointF mCurMovePointF) {

        rotate(mPreMovePointF, mCurMovePointF);
        scale(mMoveDownPointF, mCurMovePointF);

    }

    public void move(float distanceX, float distanceY) {
        this.mCenterPoint.set(mCenterPoint.x + distanceX, mCenterPoint.y
                + distanceY);
        boolean limit = limitScope();
        if (!limit) {
            l = (int) (this.mCenterPoint.x - this.getWidth() / 2);
            t = (int) (this.mCenterPoint.y - this.getHeight() / 2);
            b = t + this.getHeight();
            r = l + this.getWidth();
            this.layout(l, t, r, b);
        } else {
            this.mCenterPoint.set(mCenterPoint.x - distanceX, mCenterPoint.y
                    - distanceY);
        }
    }

    private boolean limitScope() {
        View parent = (View) this.getParent();
        if (mCenterPoint.x < 0 || mCenterPoint.x > parent.getWidth()
                || mCenterPoint.y < 0 || mCenterPoint.y > parent.getHeight()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // et_content.draw(canvas);
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public void rotationReset() {

        float mDegree = getRotation();
        final float degreePlus = mDegree % 90;

        if (0 <= (int) Math.rint(degreePlus)
                && (int) Math.rint(degreePlus) <= 6
                && (int) Math.rint(degreePlus) != 0) {
            EditableLayout.this.setRotation(mDegree - degreePlus);
        }

        if (85 <= (int) Math.rint(degreePlus)
                && (int) Math.rint(degreePlus) <= 90
                && (int) Math.rint(degreePlus) != 0) {
            EditableLayout.this.setRotation(mDegree + (90 - degreePlus));
        }
    }

}
