package com.yeetou.xinyongkaguanjia.ui;

import com.yeetou.xinyongkaguanjia.R;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class LoadActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageView bg = new ImageView(this);
		bg.setImageResource(R.drawable.main_loading);
		bg.setScaleType(ScaleType.FIT_XY);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置成全屏模式
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		setContentView(bg);
		Handler mHandler = new Handler();
		mHandler.postDelayed(myThread, 2000);

	}

	Thread myThread = new Thread() {
		public void run() {
			Intent toLoad = new Intent(LoadActivity.this, LoginActivity.class);
			startActivity(toLoad);
			finish();
		};
	};
}
