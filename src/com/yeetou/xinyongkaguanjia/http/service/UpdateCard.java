package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.UpdateBankBase;
import com.yeetou.xinyongkaguanjia.info.Card;
import com.yeetou.xinyongkaguanjia.info.ChangeCardInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class UpdateCard implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String secret;
	private Card card;
	private String email;

	public UpdateCard(Context context, Handler mHandler, String secret, String email, Card card) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.update;
		this.secret = secret;
		this.email = email;
		this.card = card;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		ChangeCardInfo changeCardInfo = new ChangeCardInfo();
		changeCardInfo.setVer("1.0");
		changeCardInfo.setEmail(email);
		changeCardInfo.setSecret(secret);
		changeCardInfo.setCard(card);

		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, changeCardInfo);

		switch (hre.getHttpResponseCode()) {

		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());

				UpdateBankBase updateBankBase = (UpdateBankBase) JsonUtil.Json2Object(json, UpdateBankBase.class);
				Log.d("UpdateCard", updateBankBase.getCode() + updateBankBase.getMsg());
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, updateBankBase));
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				Log.e("UpdateCard", e.toString());
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("UpdateCard", "" + hre.getHttpResponseCode());
			break;

		}
	}

}
