package com.yeetou.xinyongkaguanjia.http.service;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.service.DbAccountService;
import com.yeetou.xinyongkaguanjia.http.base.EmailScanBase;
import com.yeetou.xinyongkaguanjia.http.base.MsgUploadBase;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;
import com.yeetou.xinyongkaguanjia.util.CheckSMS;

public class AutoSyncData {
	private Context context;

	private DbAccountService dbAccountService;
	private DbAccount dbaccout;
	private String secret;
	private long scanSmsTime;
	private Handler mHandler;

	public AutoSyncData(Context context, Handler mHandler) {
		this.context = context;
		this.mHandler = mHandler;
		dbAccountService = new DbAccountService(context);
		dbaccout = dbAccountService.get();
		secret = dbaccout.getSecret();
		scanSmsTime = dbaccout.getMsgscan_at();
		new Thread(new CheckSMS(context, ScanSmsHandler, scanSmsTime)).start();
	}

	private Handler ScanSmsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case AppConstant.CHECK_MESSAGE_FINISH:
				List<MsgInfo> msgInfos = (List<MsgInfo>) msg.obj;
				if (msgInfos != null && msgInfos.size() != 0) {// 当查询的短信不为空的时候，保存并上传
					// 上传短信
					new Thread(new MsgUpload(context, UploadSmsHandler, msgInfos, secret, dbaccout.getEmail())).start();
				} else {
					new Thread(new EmailScanService(context, smailScanHandler, dbaccout.getEmail(), secret)).start();
				}
				break;
			default:
				new Thread(new EmailScanService(context, smailScanHandler, dbaccout.getEmail(), secret)).start();
				break;
			}
		}

	};

	private Handler UploadSmsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			MsgUploadBase msgUploadBase = (MsgUploadBase) msg.obj;

			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:

				secret = msgUploadBase.getSecret(); // 更新secret
				dbaccout.setMsgscan_at(System.currentTimeMillis());
				dbAccountService.saveOrUpdate(dbaccout);
				new Thread(new EmailScanService(context, smailScanHandler, dbaccout.getEmail(), secret)).start();
				break;
			default:
				new Thread(new EmailScanService(context, smailScanHandler, dbaccout.getEmail(), secret)).start();
				break;
			}
		}

	};
	
	private Handler smailScanHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			EmailScanBase eScanBase = (EmailScanBase) msg.obj;
			switch (msg.what) {
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				secret = eScanBase.getSecret(); // 更新secret
				dbAccountService.saveSecret(secret);
				break;
			default:
				new Thread(new SyncData(context, SyncDatahandler, dbaccout.getEmail(), secret, 0)).start();
				break;
			}
			new Thread(new SyncData(context, SyncDatahandler, dbaccout.getEmail(), secret, 0)).start();
		}
		

	};

	private Handler SyncDatahandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mHandler.sendEmptyMessage(AppConstant.HANDLER_SYN_FINISH);
			switch (msg.what) {
			default:
				mHandler.sendEmptyMessage(AppConstant.HANDLER_SYN_FINISH);
			case AppConstant.HANDLER_MESSAGE_NORMAL:
				Toast.makeText(context, "同步成功", Toast.LENGTH_SHORT).show();
				break;
			case AppConstant.HANDLER_MESSAGE_NONETWORK:
				Toast.makeText(context, "同步失败。请稍候重试", Toast.LENGTH_SHORT).show();
				break;
			case AppConstant.HANDLER_MESSAGE_TIMEOUT:
				Toast.makeText(context, "同步失败。请稍候重试", Toast.LENGTH_SHORT).show();
				break;
			case AppConstant.HANDLER_HTTPSTATUS_ERROR:
				Toast.makeText(context, "同步失败。请稍候重试", Toast.LENGTH_SHORT).show();
				break;
			}

		}

	};

}
