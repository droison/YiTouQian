package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.IVBase;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class IV_Get implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;

	public IV_Get(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.rand_iv;

	}
	

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		
		HttpResponseEntity hre = HTTP.get(url);

		switch (hre.getHttpResponseCode()) {
		
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				System.out.println(json);
				IVBase ivBase = (IVBase) JsonUtil.Json2Object(json, IVBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, ivBase));
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("MsgUpload", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("MsgUpload", "" + hre.getHttpResponseCode());
			break;

		}
	}

}
