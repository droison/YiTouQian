package com.yeetou.xinyongkaguanjia.http.service;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.service.DbBankService;
import com.yeetou.xinyongkaguanjia.http.base.BankSynBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class BankSynHttp implements Runnable{
	private Context context;
	private Handler mHandler;
	private String url;
	private DbBankService dbBankService;

	public BankSynHttp(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.banklist;
	}

	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		HttpResponseEntity hre = HTTP.get(url);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				BankSynBase bsb = (BankSynBase) JsonUtil.Json2Object(json, BankSynBase.class);
				if(bsb.getData()!=null&&bsb.getData().size()!=0){
					dbBankService = new DbBankService(context);
					dbBankService.synSave(bsb.getData());
				}
				mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NORMAL);
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
