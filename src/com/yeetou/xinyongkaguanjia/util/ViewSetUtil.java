package com.yeetou.xinyongkaguanjia.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ViewSetUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	//scalew为 高/宽
	public static int getWidthPx(Context context,float scale ){
		 DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 
		 return (int)scale*dm.widthPixels;
	}
}
