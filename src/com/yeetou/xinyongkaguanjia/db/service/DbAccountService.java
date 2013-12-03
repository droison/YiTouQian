package com.yeetou.xinyongkaguanjia.db.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbAccount;
import com.yeetou.xinyongkaguanjia.db.dao.DbAccountInterface;

public class DbAccountService implements DbAccountInterface {

	private DBHelper dbHelper;
	private DbAccountService(){
		
	}
	public DbAccountService(Context mContext){
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}
	
	
	//没有则返回null
	@Override
	public DbAccount get() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from account where _id=201310", new String[]{});
		DbAccount account = null;
		if(cursor.moveToFirst()){
			account = new DbAccount();
			account.setId(cursor.getInt(0));
			account.setPhone(cursor.getString(1));
			account.setEmail(cursor.getString(2));
			account.setSecret(cursor.getString(3));
			account.setToken(cursor.getString(4));
			account.setMsgscan_at(cursor.getLong(5));
			account.setSyn_at(cursor.getLong(6));
			account.setIv(cursor.getString(7));
		}
		cursor.close();
		
		return account;
	}

	@Override
	public boolean saveOrUpdate(DbAccount account) {
		if(account.check()){
			delete();
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("_id", 201310);
			values.put("phone", account.getPhone());
			values.put("email", account.getEmail());
			values.put("secret", account.getSecret());
			values.put("token", account.getToken());
			values.put("msgscan_at", account.getMsgscan_at());
			values.put("syn_at", account.getSyn_at());
			values.put("iv", account.getIv());
			db.insert("account", null, values);
			
			return true;
		}else{
			return false;
		}
		
	}

	
	public void saveSecret(String secret){
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("secret", secret);
			db.update("account", values, "_id=?", new String[]{"201310"});
	}
	@Override
	public void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("account", null, null);
	}

}
