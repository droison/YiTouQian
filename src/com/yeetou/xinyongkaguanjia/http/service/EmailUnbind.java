package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.BasicBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.info.EmailUnbindInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class EmailUnbind implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String email;
	private String secret;
	private String c_email;

	public EmailUnbind(Context context, Handler mHandler, String email, String secret, String c_email) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.unbind;
		this.email = email;
		this.secret = secret;
		this.c_email = c_email;

	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		EmailUnbindInfo emailUnbindInfo = new EmailUnbindInfo();
		emailUnbindInfo.setVer("1.0");
		emailUnbindInfo.setEmail(email);
		emailUnbindInfo.setSecret(secret);
		emailUnbindInfo.setC_email(c_email);

		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, emailUnbindInfo);

		switch (hre.getHttpResponseCode()) {

		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				BasicBase basicBase = (BasicBase) JsonUtil.Json2Object(json, BasicBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, basicBase));
				Log.d("Emailunbind", basicBase.getCode() + "msg:" + basicBase.getMsg());
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("Emailunbind", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.e("Emailunbind", "" + hre.getHttpResponseCode());
			break;

		}
	}

}
