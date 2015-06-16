package com.key.doltool.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
/**
 * 单位转换工具类
 * @author key
 * @version 1.0
 */
public class DensityUtil {
	/**
	 * dip转换pixel
	 * @param context
	 * @param dpValue
	 * @return pixel
	 */
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }
	/**
	 * pixel转换dip
	 * @param context
	 * @param pxValue
	 * @return dip
	 */
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
    //获得宽
	public static int getScreenWidth(Activity context) {
		 DisplayMetrics displaymetrics = new DisplayMetrics();
		 context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.widthPixels;
	}
	//获得高
	public static int getScreenHeight(Activity context) {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		return displaymetrics.heightPixels;
	}
}