package com.yeetou.xinyongkaguanjia.http.service;

import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.http.base.CreditPayoffBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.info.CreditPayoffInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class CreditPayoffService implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String secret;
	private String email;
	private Integer billid;
	private DbAccountService dbAccountService;
	private DbAccount dbAccount;

	public CreditPayoffService(Context context, Handler mHandler,Integer billid) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.creditPayoff;
		dbAccountService = new DbAccountService(context);
		dbAccount = dbAccountService.get();
		this.secret = dbAccount.getSecret();
		this.email = dbAccount.getEmail();
		this.billid = billid;
	}

	@Override
	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		CreditPayoffInfo cpi = new CreditPayoffInfo();
		cpi.setEmail(email);
		cpi.setSecret(secret);
		cpi.setBill_id(billid);
		cpi.setVer("1.0");
		
		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, cpi);

		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				CreditPayoffBase  cpob= (CreditPayoffBase) JsonUtil.Json2Object(json, CreditPayoffBase.class);
				if(cpob.getCode()==101){
					mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, cpob));
				}else{
					mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
					Log.e("CreditPayoffService",cpob.getMsg());
				}
			} catch (TimeoutException e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_TIMEOUT);
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("CreditPayoffService", e.toString());
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.e("CreditPayoffService=", ""+hre.getHttpResponseCode());
			break;

		}
	}
}
