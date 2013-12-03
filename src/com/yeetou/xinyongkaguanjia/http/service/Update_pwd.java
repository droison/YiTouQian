package com.yeetou.xinyongkaguanjia.http.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.Update_pwdBase;
import com.yeetou.xinyongkaguanjia.info.update_pwdInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class Update_pwd implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String old_pwd;
	private String new_pwd;
	private String secret;
	private String email;
	private String iv;

	public Update_pwd(Context context, Handler mHandler, String old_pwd, String new_pwd, String secret, String email, String iv) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.update_pwd;
		this.old_pwd = old_pwd;
		this.new_pwd = new_pwd;
		this.secret = secret;
		this.email = email;
		this.iv = iv;
	}

	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		update_pwdInfo uInfo = new update_pwdInfo();
		uInfo.setVer("1.0");
		uInfo.setEmail(email);
		uInfo.setSecret(secret);
		uInfo.setOld_pwd(old_pwd);
		uInfo.setNew_pwd(new_pwd);
		uInfo.setIv(iv);

		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, uInfo);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());

				Update_pwdBase uBase = (Update_pwdBase) JsonUtil.Json2Object(json, Update_pwdBase.class);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, uBase));
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("StringGet", "200", e);
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("StringGet", "" + hre.getHttpResponseCode());
			break;
		}
	}
}
