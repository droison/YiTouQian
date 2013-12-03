package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.EmailCheckBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.info.EmailCheckInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;
import com.yeetou.xinyongkaguanjia.util.UserInfoUtil;

public class EmailCheck implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String secret;
	private String email;
	private String c_email;
	private String raw_pwd;
	private String iv;

	public EmailCheck(Context context, Handler mHandler, String email, String secret, String c_email, String raw_pwd, String url, String iv) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = url;
		this.secret = secret;
		this.email = email;
		this.c_email = c_email;
		this.raw_pwd = raw_pwd;
		this.iv = iv;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		EmailCheckInfo eCheckInfo = new EmailCheckInfo();
		eCheckInfo.setVer("1.0");
		eCheckInfo.setEmail(email);
		eCheckInfo.setSecret(secret);
		eCheckInfo.setC_email(c_email);
		eCheckInfo.setRaw_pwd(raw_pwd);
		eCheckInfo.setIv(iv);
		eCheckInfo.setDevice(UserInfoUtil.device(context));
		
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, eCheckInfo);

		switch (hre.getHttpResponseCode()) {
		
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				EmailCheckBase  eCheckBase= (EmailCheckBase) JsonUtil.Json2Object(json, EmailCheckBase.class);
				Log.d("EmailCheck", eCheckBase.getCode()+" "+eCheckBase.getMsg());
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, eCheckBase));
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
