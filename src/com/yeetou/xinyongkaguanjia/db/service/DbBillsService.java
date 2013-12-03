package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;
import com.yeetou.xinyongkaguanjia.db.dao.DbBillsInterface;

public class DbBillsService implements DbBillsInterface {

	private DBHelper dbHelper;

	private DbBillsService() {

	}

	public DbBillsService(Context mContext) {
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}

	private void deleteOne(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from card_bills where _id=?", new Integer[] { id });
	}

	private void delete() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete("card_bills", null, null);
		
	}

	@Override
	public List<DbCardBills> getAll(List<Integer> billIds) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<DbCardBills> bills = new ArrayList<DbCardBills>();
		Cursor cursor = null;
		for (Integer billid : billIds) {
			cursor = db.rawQuery("select * from card_bills where _id=? and bill_type='RMB' limit 1", new String[] { String.valueOf(billid) });

			if (cursor.moveToFirst()) {
				DbCardBills bill = new DbCardBills();
				bill.setId(cursor.getInt(0));
				bill.setBill_type(cursor.getString(1));
				bill.setYear(cursor.getString(2));
				bill.setMonth(cursor.getString(3));
				bill.setDue_date(cursor.getLong(4));
				bill.setBilling_date(cursor.getLong(5));
				bill.setNew_balance(cursor.getFloat(6));
				bill.setMin_payment(cursor.getFloat(7));
				bill.setPay_state(cursor.getInt(8));
				bill.setCreated_at(cursor.getLong(9));
				bill.setUpdated_at(cursor.getLong(10));
				bills.add(bill);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		
		return bills;
	}

	@Override
	public DbCardBills getOne(int billId) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DbCardBills bill = null;
		Cursor cursor = db.rawQuery("select * from card_bills where _id=? and bill_type='RMB' limit 1", new String[] { String.valueOf(billId) });

		if (cursor.moveToFirst()) {
			bill = new DbCardBills();
			bill.setId(cursor.getInt(0));
			bill.setBill_type(cursor.getString(1));
			bill.setYear(cursor.getString(2));
			bill.setMonth(cursor.getString(3));
			bill.setDue_date(cursor.getLong(4));
			bill.setBilling_date(cursor.getLong(5));
			bill.setNew_balance(cursor.getFloat(6));
			bill.setMin_payment(cursor.getFloat(7));
			bill.setPay_state(cursor.getInt(8));
			bill.setCreated_at(cursor.getLong(9));
			bill.setUpdated_at(cursor.getLong(10));
		}

		cursor.close();
		
		return bill;
	}

	public DbCardBills getLast(String bank_card_id) {
		String sql = "SELECT cb._id,cb.bill_type,cb.year,cb.month,cb.due_date,cb.billing_date,cb.new_balance,cb.min_payment,cb.pay_state,cb.created_at,cb.updated_at from card_bills cb left join card_to_bills ccb on cb._id = ccb.bill_id where ccb.bank_card_id =? and cb.bill_type = 'RMB' order by cb._id desc limit 1";

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		DbCardBills bill = null;
		Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(bank_card_id) });

		if (cursor.moveToFirst()) {
			bill = new DbCardBills();
			bill.setId(cursor.getInt(0));
			bill.setBill_type(cursor.getString(1));
			bill.setYear(cursor.getString(2));
			bill.setMonth(cursor.getString(3));
			bill.setDue_date(cursor.getLong(4));
			bill.setBilling_date(cursor.getLong(5));
			bill.setNew_balance(cursor.getFloat(6));
			bill.setMin_payment(cursor.getFloat(7));
			bill.setPay_state(cursor.getInt(8));
			bill.setCreated_at(cursor.getLong(9));
			bill.setUpdated_at(cursor.getLong(10));
		}

		cursor.close();
		
		return bill;

	}

	public void updateLast(String bank_card_id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cursor = db.rawQuery("select bill_id from card_to_bills where bank_card_id=? order by created_at desc", new String[] { String.valueOf(bank_card_id) });
		while (cursor.moveToNext()) {
			values.put("pay_state", 2);
			db.update("card_bills", values, "_id=?", new String[] { cursor.getString(0) });
		}
		if (cursor != null)
			cursor.close();
		
	}

	@Override
	public void synSave(List<DbCardBills> cardbills) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (DbCardBills temp : cardbills) {
			ContentValues values = new ContentValues();
			deleteOne(temp.getId());
			values.put("_id", temp.getId());
			values.put("bill_type", temp.getBill_type());
			values.put("year", temp.getYear());
			values.put("month", temp.getMonth());
			values.put("due_date", temp.getDue_date());
			values.put("billing_date", temp.getBilling_date());
			values.put("new_balance", temp.getNew_balance());
			values.put("min_payment", temp.getMin_payment());
			values.put("pay_state", temp.getPay_state());
			values.put("created_at", temp.getCreated_at());
			values.put("updated_at", temp.getUpdated_at());
			db.insert("card_bills", null, values);
		}
		

	}

	@Override
	public List<DbCardBills> getAll() {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		List<DbCardBills> bills = new ArrayList<DbCardBills>();
		Cursor cursor = db.rawQuery("select * from card_bills where bill_type='RMB'", new String[] {});

		while (cursor.moveToNext()) {
			DbCardBills bill = new DbCardBills();
			bill.setId(cursor.getInt(0));
			bill.setBill_type(cursor.getString(1));
			bill.setYear(cursor.getString(2));
			bill.setMonth(cursor.getString(3));
			bill.setDue_date(cursor.getLong(4));
			bill.setBilling_date(cursor.getLong(5));
			bill.setNew_balance(cursor.getFloat(6));
			bill.setMin_payment(cursor.getFloat(7));
			bill.setPay_state(cursor.getInt(8));
			bill.setCreated_at(cursor.getLong(9));
			bill.setUpdated_at(cursor.getLong(10));
			bills.add(bill);
		}

		cursor.close();
		
		return bills;
	}

}
