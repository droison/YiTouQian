package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.UserLoginBase;
import com.yeetou.xinyongkaguanjia.info.UserLoginInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;
import com.yeetou.xinyongkaguanjia.util.UserInfoUtil;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class UserLogin implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String phone;

	public UserLogin(Context context, Handler mHandler,String phone) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.register;
		this.phone = phone;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}
		
		UserLoginInfo user = UserInfoUtil.info(context,phone);
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, user);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				UserLoginBase userLoginBase = (UserLoginBase) JsonUtil.Json2Object(json, UserLoginBase.class);
				System.out.println(userLoginBase.getExisting());
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, userLoginBase));
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				Log.e("UserLogin", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("UserLogin", "" + hre.getHttpResponseCode());
			break;

		}
	}
}
