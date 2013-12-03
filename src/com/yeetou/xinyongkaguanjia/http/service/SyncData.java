package com.yeetou.xinyongkaguanjia.http.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;
import com.yeetou.xinyongkaguanjia.db.base.DbCardToBills;
import com.yeetou.xinyongkaguanjia.db.base.DbEmail;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.db.service.DbBankCardService;
import com.yeetou.xinyongkaguanjia.db.service.DbBillsService;
import com.yeetou.xinyongkaguanjia.db.service.DbCardToBillService;
import com.yeetou.xinyongkaguanjia.db.service.DbEmailService;
import com.yeetou.xinyongkaguanjia.db.service.DbStreamService;
import com.yeetou.xinyongkaguanjia.http.base.DataSynBase;
import com.yeetou.xinyongkaguanjia.http.base.FdSynAtBase;
import com.yeetou.xinyongkaguanjia.http.base.FeedbackBase;
import com.yeetou.xinyongkaguanjia.http.base.HttpResponseEntity;
import com.yeetou.xinyongkaguanjia.http.base.StreamBase;
import com.yeetou.xinyongkaguanjia.http.base.FdSynAtBase.Id_list;
import com.yeetou.xinyongkaguanjia.info.SyncDataInfo;
import com.yeetou.xinyongkaguanjia.util.CheckNetwork;
import com.yeetou.xinyongkaguanjia.util.JsonUtil;
import com.yeetou.xinyongkaguanjia.util.StringUtil;

public class SyncData implements Runnable {
	private Context context;
	private Handler mHandler;
	private String url;
	private String email;
	private String secret;
	private int flag;

	private DbBankCardService dbBankCardService;
	private DbBillsService dbBillsService;
	private DbStreamService dbStreamService;
	private DbCardToBillService dbCardToBillService;
	private DbEmailService dbEmailService;
	private DbAccountService dbAccountService;

	public SyncData(Context context, Handler mHandler, String email, String secret, int flag) {
		this.context = context;
		this.mHandler = mHandler;
		this.url = AppConstant.HTTPURL.sysc_web;
		this.email = email;
		this.secret = secret;
		this.flag = flag;
	}

	public void run() {

		if (!CheckNetwork.Isavilable(context)) {
			mHandler.sendEmptyMessage(AppConstant.HANDLER_MESSAGE_NONETWORK);
			return;
		}

		SyncDataInfo syncDataInfo = new SyncDataInfo();
		syncDataInfo.setVer("1.0");
		syncDataInfo.setEmail(email);
		syncDataInfo.setSecret(secret);
		syncDataInfo.setForce_flag(flag);

		HttpResponseEntity hre = HTTP.postByHttpUrlConnection(url, syncDataInfo);
		switch (hre.getHttpResponseCode()) {
		case 200:
			try {
				String json = StringUtil.byte2String(hre.getB());
				DataSynBase dsb = (DataSynBase) JsonUtil.Json2Object(json, DataSynBase.class);
				secret = dsb.getSecret();
				if (dsb.getBook_data() != null) {
					List<DbEmail> card_emails = dsb.getBook_data().getCard_emails();
					List<DbBankCard> card_bank_cards = dsb.getBook_data().getCard_bank_cards();
					List<StreamBase> card_streams = dsb.getBook_data().getCard_streams();
					List<DbCardToBills> card_card_bills = dsb.getBook_data().getCard_card_bills();
					List<DbCardBills> card_bills = dsb.getBook_data().getCard_bills();

					FdSynAtBase feed = new FdSynAtBase();
					feed.setEmail(email);
					feed.setSecret(secret);
					feed.setVer("1.0");
					Id_list list = feed.new Id_list();
					List<Integer> l1 = new ArrayList<Integer>();
					List<Integer> l2 = new ArrayList<Integer>();
					List<Integer> l3 = new ArrayList<Integer>();
					List<Integer> l4 = new ArrayList<Integer>();
					List<Integer> l5 = new ArrayList<Integer>();

					if (card_emails != null && card_emails.size() != 0) {
						dbEmailService = new DbEmailService(context);
						dbEmailService.synSave(card_emails);
						for (DbEmail temp : card_emails) {
							l1.add(temp.getId());
						}
					}
					list.setCard_emails(l1);
					if (card_bank_cards != null && card_bank_cards.size() != 0) {
						dbBankCardService = new DbBankCardService(context);
						dbBankCardService.synSave(card_bank_cards);
						for (DbBankCard temp : card_bank_cards) {
							l2.add(temp.getId());
						}
					}
					list.setCard_bank_cards(l2);
					if (card_card_bills != null && card_card_bills.size() != 0) {
						dbCardToBillService = new DbCardToBillService(context);
						dbCardToBillService.synSave(card_card_bills);
						for (DbCardToBills temp : card_card_bills) {
							l3.add(temp.getId());
						}
					}
					list.setCard_card_bills(l3);
					if (card_bills != null && card_bills.size() != 0) {
						dbBillsService = new DbBillsService(context);
						dbBillsService.synSave(card_bills);
						for (DbCardBills temp : card_bills) {
							l4.add(temp.getId());
						}
					}
					list.setCard_bills(l4);
					if (card_streams != null && card_streams.size() != 0) {
						dbStreamService = new DbStreamService(context);
						dbStreamService.synSaveStreamBase(card_streams);
						for (StreamBase temp : card_streams) {
							l5.add(temp.getId());
						}
					}
					list.setCard_streams(l5);
					feed.setId_list(list);
					
					url = AppConstant.HTTPURL.sysc_at;
					hre = HTTP.postByHttpUrlConnection(url, feed);
					if(hre.getHttpResponseCode()==200){
						json = StringUtil.byte2String(hre.getB());
						FeedbackBase fbb =  (FeedbackBase) JsonUtil.Json2Object(json, FeedbackBase.class);
						secret = fbb.getSecret();
						if(fbb.getCode()==101){
							dbAccountService = new DbAccountService(context);
							DbAccount account = dbAccountService.get();
							account.setSecret(secret);
							dbAccountService.saveOrUpdate(account);
							
							mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, secret));
						}else{
							dbAccountService = new DbAccountService(context);
							DbAccount account = dbAccountService.get();
							account.setSecret(secret);
							dbAccountService.saveOrUpdate(account);
							mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
							Log.e("SynData feed", fbb.getMsg());
						}
					}else{
						dbAccountService = new DbAccountService(context);
						DbAccount account = dbAccountService.get();
						account.setSecret(secret);
						dbAccountService.saveOrUpdate(account);
						mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
					}
				}else{
					dbAccountService = new DbAccountService(context);
					DbAccount account = dbAccountService.get();
					account.setSecret(secret);
					dbAccountService.saveOrUpdate(account);
					mHandler.sendMessage(mHandler.obtainMessage(AppConstant.HANDLER_MESSAGE_NORMAL, secret));
				}			
			} catch (Exception e) {
				mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
				Log.e("SynData", "200", e);
			}
			break;
		default:
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
			Log.d("SynData", "" + hre.getHttpResponseCode());
			break;
		}
	}
}
