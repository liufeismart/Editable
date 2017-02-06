package com.liufeismart.editable.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

public class DensityUtils {
	private DensityUtils() {
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static int dp2px(Context context, float dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, context.getResources().getDisplayMetrics());
	}

	public static int sp2px(Context context, float spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, context.getResources().getDisplayMetrics());
	}

	public static float px2dp(Context context, float pxVal) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxVal / scale);
	}

	public static float px2sp(Context context, float pxVal) {
		return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
	}

	public static int Newpx2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 描述：获取屏幕的宽度
	 * 
	 * @param context
	 * @return
	 */
	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowWidth(Context context) {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Point screenPoint = new Point();
		display.getSize(screenPoint);
		return screenPoint.x;
	}

	@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int getWindowHeight(Context context) {
		WindowManager windowManager = ((Activity) context).getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Point screenPoint = new Point();
		display.getSize(screenPoint);
		return screenPoint.y;
	}

	public static int getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return (int) dm.density;
	}

	public static int getDensityDpi(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return (int) dm.densityDpi;
	}
}
