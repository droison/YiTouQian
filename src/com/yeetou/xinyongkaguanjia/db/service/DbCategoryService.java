package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.dao.DbCategoryInterface;

public class DbCategoryService implements DbCategoryInterface {

	private DBHelper dbHelper;
	private DbCategoryService(){
		
	}
	public DbCategoryService(Context mContext){
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}
	
	@Override
	public List<String> getAllCategory() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name from category", new String[]{});
		List<String> categorys = new ArrayList<String>();
		while(cursor.moveToNext()){
			categorys.add(cursor.getString(0));
		}
		
		cursor.close();
		return categorys;
	}

	@Override
	public void save(List<String> categorys) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for(String category:categorys){
			ContentValues values = new ContentValues();
			values.put("name", category);
			db.insert("category", null, values);
		}
		
	}

}
