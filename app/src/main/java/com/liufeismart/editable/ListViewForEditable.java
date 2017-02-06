package com.liufeismart.editable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewForEditable extends ListView {

	public static boolean canTouch = true;

	public ListViewForEditable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListViewForEditable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForEditable(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
		if (canTouch) {
			return dispatchTouchEvent;
		}
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean onTouchEvent = super.onTouchEvent(ev);
		if (canTouch) {
			return onTouchEvent;
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);

		if (canTouch) {
			return onInterceptTouchEvent;
		}
		return false;
	}

}
