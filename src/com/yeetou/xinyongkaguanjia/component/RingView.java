package com.yeetou.xinyongkaguanjia.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

public class RingView extends View {

	private final Paint paint;
	private final Context context;

	public RingView(Context context) {

		// TODO Auto-generated constructor stub
		this(context, null);

	}

	public RingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.paint = new Paint();
		this.paint.setAntiAlias(true); // 消除锯齿
		this.paint.setStyle(Paint.Style.STROKE); // 绘制空心圆
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		canvas.drawColor(Color.TRANSPARENT);
		float[] fl = new float[] { 0, 200, 150, 200, 0, 250, 150, 200 };

		Paint paint = new Paint();
		
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(200, 300, 100, paint);
	}

	@Override
	public void setLayoutParams(LayoutParams params) {
		// TODO Auto-generated method stub
		super.setLayoutParams(params);
	}

	public static Bitmap getCir(){
		Canvas canvas = new Canvas();
		canvas.drawColor(Color.TRANSPARENT);
		float[] fl = new float[] { 0, 200, 150, 200, 0, 250, 150, 200 };

		Paint paint = new Paint();
		
		paint.setStyle(Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(200, 300, 100, paint);
		Bitmap bm = null;
		canvas.drawBitmap(bm, 0, 0, paint);
		return bm;
	}
	
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
