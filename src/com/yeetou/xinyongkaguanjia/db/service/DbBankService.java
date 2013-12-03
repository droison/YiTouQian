package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.dao.DbBankInterface;

public class DbBankService implements DbBankInterface {


	private DBHelper dbHelper;
	private DbBankService(){
		
	}
	public DbBankService(Context mContext){
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}
	
	
	@Override
	public List<DbBank> getAllBank() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_banks", new String[]{});
		List<DbBank> banks = new ArrayList<DbBank>();
		while(cursor.moveToNext()){
			DbBank bank = new DbBank();
			bank.setId(cursor.getInt(0));
			bank.setName(cursor.getString(1));
			bank.setSpell_name(cursor.getString(2));
			bank.setLogo(cursor.getString(3));
			bank.setPhone(cursor.getString(4));
			bank.setCc_yd(cursor.getString(5));
			bank.setCc_lt(cursor.getString(6));
			bank.setHotline(cursor.getString(7));
			bank.setManual(cursor.getString(8));
			bank.setLoss(cursor.getString(9));
			bank.setQuery_bill(cursor.getString(10));
			bank.setQuery_limit(cursor.getString(11));
			bank.setQuery_credit(cursor.getString(12));
			banks.add(bank);
		}
		
		cursor.close();
		return banks;
	}
	
	

	@Override
	public void synSave(List<DbBank> banks) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		deleteAll();
		for(DbBank bank:banks){
			ContentValues values = new ContentValues();
			values.put("_id", bank.getId());
			values.put("name", bank.getName());
			values.put("spell_name", bank.getSpell_name());
			values.put("logo", bank.getLogo());
			values.put("phone", bank.getPhone());
			values.put("cc_yd", bank.getCc_yd());
			values.put("cc_lt", bank.getCc_lt());
			values.put("hotline", bank.getHotline());
			values.put("manual", bank.getManual());
			values.put("loss", bank.getLoss());
			values.put("query_bill", bank.getQuery_bill());
			values.put("query_limit", bank.getQuery_limit());
			values.put("query_credit", bank.getQuery_credit());
			db.insert("card_banks", null, values);
		}
		
	}
	
	@Override
	public void deleteAll() {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("card_banks", null, null);
	}
	@Override
	public DbBank getById(String id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_banks where _id=?", new String[]{id});
		DbBank bank = null;
		if(cursor.moveToFirst()){
			bank = new DbBank();
			bank.setId(cursor.getInt(0));
			bank.setName(cursor.getString(1));
			bank.setSpell_name(cursor.getString(2));
			bank.setLogo(cursor.getString(3));
			bank.setPhone(cursor.getString(4));
			bank.setCc_yd(cursor.getString(5));
			bank.setCc_lt(cursor.getString(6));
			bank.setHotline(cursor.getString(7));
			bank.setManual(cursor.getString(8));
			bank.setLoss(cursor.getString(9));
			bank.setQuery_bill(cursor.getString(10));
			bank.setQuery_limit(cursor.getString(11));
			bank.setQuery_credit(cursor.getString(12));
		}
		
		cursor.close();
		return bank;	
	}
	@Override
	public DbBank getByName(String name) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_banks where name=?", new String[]{name});
		DbBank bank = null;
		if(cursor.moveToFirst()){
			bank = new DbBank();
			bank.setId(cursor.getInt(0));
			bank.setName(cursor.getString(1));
			bank.setSpell_name(cursor.getString(2));
			bank.setLogo(cursor.getString(3));
			bank.setPhone(cursor.getString(4));
			bank.setCc_yd(cursor.getString(5));
			bank.setCc_lt(cursor.getString(6));
			bank.setHotline(cursor.getString(7));
			bank.setManual(cursor.getString(8));
			bank.setLoss(cursor.getString(9));
			bank.setQuery_bill(cursor.getString(10));
			bank.setQuery_limit(cursor.getString(11));
			bank.setQuery_credit(cursor.getString(12));
		}
		
		cursor.close();
		return bank;
	}

}
