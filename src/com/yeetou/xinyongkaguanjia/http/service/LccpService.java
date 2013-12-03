package com.yeetou.xinyongkaguanjia.http.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.LccpBase;
import com.yeetou.xinyongkaguanjia.info.LccpInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class LccpService implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private List<String> banks = new ArrayList<String>();

	public LccpService(Context context, Handler mHandler,List<DbLcBank> lcBanks) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.lccp;
		for(DbLcBank temp:lcBanks){
			banks.add(temp.getName());
		}
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		LccpInfo lccp = new LccpInfo();
		lccp.setVer("1.0");
		lccp.setBanks(banks);
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, lccp);

		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				LccpBase  lccpBase= (LccpBase) JsonUtil.Json2Object(json, LccpBase.class);
				if(lccpBase.getCode()==101){
					mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, lccpBase));
				}else{
					mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
					Log.e("EmailScanService",lccpBase.getMsg());
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
