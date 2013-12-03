package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.dao.DbSMSInterface;
import com.yeetou.xinyongkaguanjia.info.MsgInfo;

public class DbSMSService implements DbSMSInterface {

	private DBHelper dbHelper;
	private DbSMSService(){
		
	}
	public DbSMSService(Context mContext){
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}
	
	@Override
	public List<MsgInfo> getAllSMS() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from msgs", new String[]{});
		List<MsgInfo> msgs=  new ArrayList<MsgInfo>();
		while(cursor.moveToNext()){
			MsgInfo msg = new MsgInfo();
			msg.setRaw_id(cursor.getString(1));
			msg.setAddress(cursor.getString(2));
			msg.setReceived_tm(cursor.getLong(3));
			msg.setBody(cursor.getString(4));
			msgs.add(msg);
		}
		cursor.close();
		
		return msgs;
	}

	@Override
	public void save(List<MsgInfo> msgs) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		for (MsgInfo msg : msgs) {
			values.put("raw_id", msg.getRaw_id());
			values.put("address", msg.getAddress());
			values.put("received_tm", msg.getReceived_tm());
			values.put("body", msg.getBody());
			
			db.insert("msgs", null, values);
		}
		

	}
	@Override
	public void deleteAll() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("msgs", null, null);
		
	}
	
	

}
