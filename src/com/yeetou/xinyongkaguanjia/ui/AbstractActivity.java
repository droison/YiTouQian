package com.yeetou.xinyongkaguanjia.ui;

import java.io.File;

import com.baidu.mobstat.StatService;
import com.yeetou.xinyongkaguanjia.constants.AppConstant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * @author chaisong
 */
public abstract class AbstractActivity extends Activity {

	private ProgressDialog progressDialog;
	
	public final String TAG = this.getClass().getName();

	private boolean destroyed = false;

	// ***************************************
	// Activity methods
	// ***************************************
	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
	}

	public void onResume() {

		super.onResume();
		StatService.onResume(this);
	}

	public void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		File baseDir_temp = new File(AppConstant.BASE_DIR_PATH);
		File baseDir_cache = new File(AppConstant.BASE_DIR_CACHE);
		if (!baseDir_cache.isDirectory())
			baseDir_cache.mkdirs();
		if (!baseDir_temp.isDirectory())
			baseDir_temp.mkdir();
	}

	public void showLoadingProgressDialog() {
		this.showProgressDialog("Loading. Please wait...");
	}

	public void showProgressDialog(CharSequence message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setIndeterminate(true);
		}

		progressDialog.setMessage(message);
		progressDialog.show();
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:

					return true;
				}
				return false;
			}
		});
	}

	public void dismissProgressDialog() {
		if (progressDialog != null && !destroyed) {
			progressDialog.dismiss();
		}
	}

	public void displayResponse(String result) {
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}

/*	public void showNoNetWork() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.offline, (ViewGroup) findViewById(R.id.toast_layout_root));
		Toast toast = new Toast(this);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setView(view);
		toast.show();
	}*/
	public interface OnCallback{
		public void Positiveback(String voicePath);
		public void Negativeback(String voicePath);
	}
	
	
}
