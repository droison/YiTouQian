package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbCardToBills;
import com.yeetou.xinyongkaguanjia.db.dao.DbCardToBillInterface;

public class DbCardToBillService implements DbCardToBillInterface {

	private DBHelper dbHelper;

	private DbCardToBillService() {

	}

	public DbCardToBillService(Context mContext) {
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}

	private void deleteOne(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from card_to_bills where _id=?", new Integer[] { id });
	}

	private void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("card_to_bills", null, null);
		
	}

	@Override
	public List<DbCardToBills> getAll() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_to_bills order by created_at desc", new String[] {});
		List<DbCardToBills> dbCardToBills = new ArrayList<DbCardToBills>();
		while (cursor.moveToNext()) {
			DbCardToBills cardtobill = new DbCardToBills();
			cardtobill.setId(cursor.getInt(0));
			cardtobill.setBank_card_id(cursor.getInt(1));
			cardtobill.setBill_id(cursor.getInt(2));
			cardtobill.setCreated_at(cursor.getLong(3));
			cardtobill.setUpdated_at(cursor.getLong(4));
			dbCardToBills.add(cardtobill);
		}
		cursor.close();
		
		return dbCardToBills;
	}

	@Override
	public void synSave(List<DbCardToBills> cardtobills) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (DbCardToBills temp : cardtobills) {
			ContentValues values = new ContentValues();
			deleteOne(temp.getId());
			values.put("_id", temp.getId());
			values.put("bank_card_id", temp.getBank_card_id());
			values.put("bill_id", temp.getBill_id());
			values.put("created_at", temp.getCreated_at());
			values.put("updated_at", temp.getUpdated_at());
			db.insert("card_to_bills", null, values);
		}
		
	
	}

	@Override
	public List<Integer> getBillIdS(String bankCardId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select bill_id from card_to_bills where bank_card_id=? order by created_at desc", new String[] {String.valueOf(bankCardId)});
		List<Integer> result = new ArrayList<Integer>();
		while (cursor.moveToNext()) {
			
			result.add(cursor.getInt(0));
		}
		cursor.close();
		
		return result;
	}

	@Override
	public List<Integer> getCardIdS(int billId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select bank_card_id from card_to_bills where bill_id=?", new String[] {String.valueOf(billId)});
		List<Integer> result = new ArrayList<Integer>();
		while (cursor.moveToNext()) {
			result.add(cursor.getInt(0));
		}
		cursor.close();
		
		return result;
	}

	//若是没有账单，则返回null
	@Override
	public Integer getLastBillId(int bankCardId) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select bill_id from card_to_bills where bank_card_id=? order by created_at desc limit 1", new String[] {String.valueOf(bankCardId)});
		Integer result = null;
		if (cursor.moveToFirst()) {
			
			result = cursor.getInt(0);
		}
		cursor.close();
		
		return result;
	}

}
