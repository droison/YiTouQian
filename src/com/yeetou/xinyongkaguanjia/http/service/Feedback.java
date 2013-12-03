package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.FeedbackBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.info.FeedbackInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class Feedback implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String secret;
	private String email;
	private String content;

	public Feedback(Context context, Handler mHandler, String email, String secret, String content) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.feedback;
		this.secret = secret;
		this.email = email;
		this.content = content;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		FeedbackInfo feedbackInfo = new FeedbackInfo();
		feedbackInfo.setVer("1.0");
		feedbackInfo.setEmail(email);
		feedbackInfo.setSecret(secret);
		feedbackInfo.setContent(content);

		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, feedbackInfo);

		switch (hre.getHttpResponseCode()) {

		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				FeedbackBase feedbackBase = (FeedbackBase) JsonUtil.Json2Object(json, FeedbackBase.class);
				Log.d("Feedback", feedbackBase.getCode() + " " + feedbackBase.getMsg());
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, feedbackBase));
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				Log.e("Feedback", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("Feedback", "" + hre.getHttpResponseCode());
			break;

		}
	}
}
