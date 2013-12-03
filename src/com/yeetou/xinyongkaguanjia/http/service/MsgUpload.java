package com.yeetou.xinyongkaguanjia.http.service;

import java.util.List;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.MsgUploadBase;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;
import com.yeetou.xinyongkaguanjia.info.MsgUploadInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;
import com.yeetou.xinyongkaguanjia.util.UserInfoUtil;

public class MsgUpload implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private List<MsgInfo> msgs;
	private String secret;
	private String email;

	public MsgUpload(Context context, Handler mHandler, List<MsgInfo> msgs, String secret, String email) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.msgUpload;
		this.msgs = msgs;
		this.secret = secret;
		this.email = email;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}
		
		MsgUploadInfo msgInfo = new MsgUploadInfo();
		msgInfo.setVer("1.0");
		msgInfo.setEmail(email);
		msgInfo.setMessages(msgs);
		msgInfo.setSecret(secret);
		msgInfo.setDevice(UserInfoUtil.device(context));
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, msgInfo);

		switch (hre.getHttpResponseCode()) {
		
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				Log.e("MsgUpload", json.toString());
				MsgUploadBase  msgUploadBase= (MsgUploadBase) JsonUtil.jsonMsgUploadBase(json);
				mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, msgUploadBase));
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				Log.e("MsgUpload", e.toString());
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("MsgUpload", "" + hre.getHttpResponseCode());
			break;

		}
	}
	
}
