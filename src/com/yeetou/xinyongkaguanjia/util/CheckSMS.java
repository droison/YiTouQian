package com.yeetou.xinyongkaguanjia.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yeetou.xinyongkaguanjia.constants.AppConstant;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.service.DbSMSService;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

/**
 * 

 * @author Ls 功能： 输入：时间戳、上下文环境。输出：msgInfos .按小数点后两位匹配
 * 
 */
public class CheckSMS implements Runnable{
	final String SMS_URI_ALL = "content://sms/";
	private MsgInfo msgInfo;
	private Handler mHandler = null;
	private Long timestamp;
	private Context context;
	private List<MsgInfo> msgInfos;
	private DbSMSService dbSMSService;
	private Pattern p;
	
	public CheckSMS(Context context, Handler mHandler, Long timestamp){
		this.context = context;
		this.mHandler = mHandler;
		this.timestamp = timestamp;
		msgInfos = new ArrayList<MsgInfo>();
		dbSMSService = new DbSMSService(context);
		p = Pattern.compile("\\s*|\\t|\\r|\\n");
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Uri uri = Uri.parse(SMS_URI_ALL);
			String[] projection = new String[] { "_id", "address", "person","body", "date", "type" };
			String selection = "date>? and type=?";
			String[] selectionArgs = new String[] { String.valueOf(timestamp),"1" };
			Cursor cur = context.getContentResolver().query(uri, projection,selection, selectionArgs, "date desc"); // 获取手机内部短信

			if (cur.moveToFirst()) {
				int index_Id = cur.getColumnIndex("_id");
				int index_Address = cur.getColumnIndex("address");
				int index_Body = cur.getColumnIndex("body");
				int index_Date = cur.getColumnIndex("date");

				do {
					msgInfo = new MsgInfo();
					msgInfo.setRaw_id(cur.getString(index_Id));
					msgInfo.setAddress(cur.getString(index_Address));
					Matcher m = p.matcher(cur.getString(index_Body));
					msgInfo.setBody(m.replaceAll(""));
					msgInfo.setReceived_tm(cur.getLong(index_Date));
					
					if (FindNumber.MsgInfoFilter((msgInfo))){
						msgInfos.add(msgInfo);
						//mHandler.sendMessage(mHandler.obtainMessage(AppConstant.CHECK_MESSAGE_INFO, msgInfo));
					}

				} while (cur.moveToNext());

				if (!cur.isClosed()) {
					cur.close();
					cur = null;
				}
					
			}
			if(msgInfos.size()!=0){
				dbSMSService.save(msgInfos);
			}
			mHandler.sendMessage(mHandler.obtainMessage(AppConstant.CHECK_MESSAGE_FINISH, msgInfos));//查询完成，返回
		} catch (SQLiteException ex) {
			Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
			mHandler.sendEmptyMessage(AppConstant.HANDLER_HTTPSTATUS_ERROR);
		}
	}



}
