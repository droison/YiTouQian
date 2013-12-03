package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.EmailScanBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.info.EmailScanInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class EmailScanService implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String secret;
	private String email;

	public EmailScanService(Context context, Handler mHandler, String email, String secret) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.emails_scan;
		this.secret = secret;
		this.email = email;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		EmailScanInfo emailScan = new EmailScanInfo();
		emailScan.setEmail(email);
		emailScan.setSecret(secret);
		emailScan.setVer("1.0");
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, emailScan);

		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				EmailScanBase  emaiScanBase= (EmailScanBase) JsonUtil.Json2Object(json, EmailScanBase.class);
				if(emaiScanBase.getCode()==101){
					mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, emaiScanBase));
				}else{
					mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
					Log.e("EmailScanService",emaiScanBase.getMsg());
				}
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("EmailScanService", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.e("EmailScanService:code=", ""+hre.getHttpResponseCode());
			break;

		}
	}
}
