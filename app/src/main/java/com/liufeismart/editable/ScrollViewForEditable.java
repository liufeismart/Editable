package com.liufeismart.editable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewForEditable extends ScrollView {

	public ScrollViewForEditable(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ScrollViewForEditable(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewForEditable(Context context) {
		super(context);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean dispatchTouchEvent = super.dispatchTouchEvent(ev);
		if (ListViewForEditable.canTouch) {
			return dispatchTouchEvent;
		}
		return true;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean onTouchEvent = super.onTouchEvent(ev);
		if (ListViewForEditable.canTouch) {
			return onTouchEvent;
		}
		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean onInterceptTouchEvent = super.onInterceptTouchEvent(ev);

		if (ListViewForEditable.canTouch) {
			return onInterceptTouchEvent;
		}
		return false;
	}

}
