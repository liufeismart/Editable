package com.liufeismart.editable;

import android.annotation.SuppressLint;
import android.graphics.PointF;
import android.util.FloatMath;

public class EditableUtil {
    public static double angleBetweenPoints(float x1, float y1, float x2,
            float y2) {
        if ((x1 == x2) && (y1 == y2))
            return 0.0D;
        float y = y2 - y1;
        float x = x2 - x1;
        double gradiant = Math.atan2(Math.abs(y), Math.abs(x))* 57.295779513082323D;
        if(y<0 && x>0) {
            return gradiant;
        }
        else if(y>0 && x>0) {
            return 360f-gradiant;
        }
        else if(y<0 && x<0) {
            return 180f - gradiant;
        }
        else if(y>0 && x<0) {
            return 180f + gradiant;
        }
        return -1f;
    }

    public static double angleBetweenPoints(PointF p1 , PointF p2) {
          return angleBetweenPoints(p1.x, p1.y, p2.x, p2.y);
        
    }
    
    
    public static double angleBetweenHW(int width, int height) {
        return Math.atan2(Math.abs(width), Math.abs(height))* 57.295779513082323D;
    }

    public static float calculateScale(float x1, float y1, float x2, float y2) {
        float x =  (x2 - x1)/1000f;
        float y =  (y2 - y1)/1000f;
        return x<y?x:y;
    }
    
    
    @SuppressLint("FloatMath")
	public static float distance4PointF(PointF pf1, PointF pf2) {
        float disX = pf2.x - pf1.x;
        float disY = pf2.y - pf1.y;
        return (float)Math.sqrt(disX * disX + disY * disY);
    }
    

    /**
     * 弧度换算成角度
     * 
     * @return
     */
    public static double radianToDegree(double radian) {
        return radian * 180 / Math.PI;
    }
}
