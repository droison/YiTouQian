package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbEmail;
import com.yeetou.xinyongkaguanjia.db.dao.DbEmailInterface;

public class DbEmailService implements DbEmailInterface {

	private DBHelper dbHelper;

	private DbEmailService() {

	}

	public DbEmailService(Context mContext) {
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}

	@Override
	public List<DbEmail> getAllEmail() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from email where state=0", new String[] {});
		List<DbEmail> emails = new ArrayList<DbEmail>();
		while (cursor.moveToNext()) {
			DbEmail email = new DbEmail();
			email.setId(cursor.getInt(0));
			email.setEmail(cursor.getString(1));
			email.setState(cursor.getInt(2));
			email.setCreated_at(cursor.getLong(3));
			email.setUpdated_at(cursor.getLong(4));
			emails.add(email);
		}
		cursor.close();
		
		return emails;
	}

	@Override
	public boolean save(DbEmail email) {

		if (isExsit(email.getEmail())) {
			return false;
		} else {
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("email", email.getEmail());
			values.put("created_at", System.currentTimeMillis());
			db.insert("email", null, values);

			
			return true;
		}

	}

	public void synSave(List<DbEmail> emails) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (DbEmail email : emails) {
			ContentValues values = new ContentValues();
			deleteOne(email.getEmail());
			values.put("_id", email.getId());
			values.put("email", email.getEmail());
			values.put("state", email.getState());
			values.put("created_at", email.getCreated_at());
			values.put("updated_at", email.getUpdated_at());
			db.insert("email", null, values);
		}
		
	}

	private boolean isExsit(String email) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from email where email=?", new String[] { email });
		if (cursor.moveToFirst()) {
			cursor.close();
			
			return true;
		} else {
			cursor.close();
			
			return false;
		}

	}

	public void deleteOne(String email) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from email where email=?", new String[] { email });
	}

	@Override
	public void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("msgs", null, null);
		
	}

}
