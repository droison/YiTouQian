package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbLcBank;
import com.yeetou.xinyongkaguanjia.db.dao.DbBankCardInterface;

public class DbBankCardService implements DbBankCardInterface {

	private DBHelper dbHelper;

	private DbBankCardService() {

	}

	public DbBankCardService(Context mContext) {
		DBHelper.init(mContext);
		this.dbHelper = DBHelper.dbHelper();
	}

	@Override
	public List<DbBankCard> getAllCreditCard() {

		return getCards(true);

	}

	private List<DbBankCard> getCards(boolean isCredit) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;

		if (isCredit) {
			cursor = db.rawQuery("select * from card_bank_cards where card_type='credit'", new String[] {});
		} else {
			cursor = db.rawQuery("select * from card_bank_cards where card_type<>'credit'", new String[] {});

		}

		List<DbBankCard> bankCards = new ArrayList<DbBankCard>();
		while (cursor.moveToNext()) {
			DbBankCard bankcard = new DbBankCard();

			bankcard.setId(cursor.getInt(0));
			bankcard.setCard_type(cursor.getString(1));
			bankcard.setBank_id(cursor.getInt(2));
			bankcard.setName(cursor.getString(3));
			bankcard.setSex(cursor.getString(4));
			bankcard.setNumber(cursor.getString(5));
			bankcard.setCredit_limit(cursor.getInt(6));
			bankcard.setCash_limit(cursor.getInt(7));

			bankcard.setSource_from(cursor.getString(8));
			bankcard.setCreated_at(cursor.getLong(9));
			bankcard.setUpdated_at(cursor.getLong(10));
			bankcard.setBank_logo(cursor.getString(11));
			bankcard.setBank_name(cursor.getString(12));
			bankCards.add(bankcard);
		}

		cursor.close();
		return bankCards;

	}

	@Override
	public List<DbBankCard> getAllDebitCard() {

		return getCards(false);

	}

	private void delete(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from card_bank_cards where _id=?", new Integer[] { id });
	}

	@Override
	public void synSave(List<DbBankCard> bankcards) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		for (DbBankCard bankcard : bankcards) {
			ContentValues values = new ContentValues();
			delete(bankcard.getId());
			values.put("_id", bankcard.getId());
			values.put("card_type", bankcard.getCard_type());
			values.put("bank_id", bankcard.getBank_id());
			values.put("name", bankcard.getName());
			values.put("sex", bankcard.getSex());
			values.put("number", bankcard.getNumber());
			values.put("credit_limit", bankcard.getCredit_limit());
			values.put("cash_limit", bankcard.getCash_limit());
			values.put("source_from", bankcard.getSource_from());
			values.put("created_at", bankcard.getCreated_at());
			values.put("updated_at", bankcard.getUpdated_at());
			Cursor cur = db.rawQuery("select name,logo from card_banks where _id=?", new String[] { bankcard.getBank_id() + "" });
			if (cur.moveToFirst()) {
				values.put("bank_logo", cur.getString(1));
				values.put("bank_name", cur.getString(0));
				setLcBank(cur.getString(0),cur.getString(1),true);
			}
			db.insert("card_bank_cards", null, values);
		}

	}

	public DbBankCard getById(String id) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_bank_cards where _id=?", new String[] { id });

		DbBankCard bankcard = new DbBankCard();
		if (cursor.moveToFirst()) {

			bankcard.setId(cursor.getInt(0));
			bankcard.setCard_type(cursor.getString(1));
			bankcard.setBank_id(cursor.getInt(2));
			bankcard.setName(cursor.getString(3));
			bankcard.setSex(cursor.getString(4));
			bankcard.setNumber(cursor.getString(5));
			bankcard.setCredit_limit(cursor.getInt(6));
			bankcard.setCash_limit(cursor.getInt(7));

			bankcard.setSource_from(cursor.getString(8));
			bankcard.setCreated_at(cursor.getLong(9));
			bankcard.setUpdated_at(cursor.getLong(10));
			bankcard.setBank_logo(cursor.getString(11));
			bankcard.setBank_name(cursor.getString(12));
		}
		cursor.close();
		return bankcard;

	}

	public Map<String, Boolean> getLcBanks() {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select bank_name from lccp", new String[] { });
		while (cursor.moveToNext()) {
			if (!result.containsKey(cursor.getString(0))) {
				result.put(cursor.getString(0), true);
			}
		}
		cursor.close();
		return result;
	}

	public List<DbLcBank> getAllLcBanks() {
		List<DbLcBank> result = new ArrayList<DbLcBank>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from lccp", new String[] { });
		while (cursor.moveToNext()) {
			DbLcBank newOne = new DbLcBank(cursor.getString(1),cursor.getString(2));
			newOne.setIs_choose(true);
			result.add(newOne);
		}
		cursor.close();
		return result;
	}
	
	public long getLcChangeTime() {
		Long result = System.currentTimeMillis();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select max(thedate) from lccp", new String[] { });
		while (cursor.moveToNext()) {
			result = cursor.getLong(0);
		}
		cursor.close();
		return result;
	}
	
	public List<DbLcBank> getHaveBanks() {
		List<DbLcBank> result = new ArrayList<DbLcBank>();
		Map<String, Boolean> choose = getLcBanks();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select bank_name,bank_logo from card_bank_cards group by bank_name", new String[] {});
		while (cursor.moveToNext()) {
			DbLcBank temp = new DbLcBank();
			temp.setLogo(cursor.getString(1));
			temp.setName(cursor.getString(0));
			if (choose.containsKey(cursor.getString(0))) {
				temp.setIs_choose(true);
			}
			result.add(temp);
		}
		cursor.close();
		return result;
	}

	public List<DbLcBank> getBigBanks() {
		Map<String, DbLcBank> bigBanks = new HashMap<String, DbLcBank>();
		bigBanks.put("中国银行", new DbLcBank("中国银行", "zgyh.png"));
		bigBanks.put("农业银行", new DbLcBank("农业银行", "nyyh.png"));
		bigBanks.put("工商银行", new DbLcBank("工商银行", "gsyh.png"));
		bigBanks.put("建设银行", new DbLcBank("建设银行", "jsyh.png"));
		bigBanks.put("交通银行", new DbLcBank("交通银行", "jtyh.png"));
		bigBanks.put("民生银行", new DbLcBank("民生银行", "msyh.png"));
		bigBanks.put("兴业银行", new DbLcBank("兴业银行", "xyyh.png"));
		bigBanks.put("招商银行", new DbLcBank("招商银行", "zsyh.png"));
		bigBanks.put("华夏银行", new DbLcBank("华夏银行", "hxyh.png"));
		bigBanks.put("中信银行", new DbLcBank("中信银行", "zxyh.png"));

		List<DbLcBank> have = getHaveBanks();
		for (DbLcBank temp : have) {
			bigBanks.remove(temp.getName());
		}
		List<DbLcBank> result = new ArrayList<DbLcBank>();
		Map<String, Boolean> choose = getLcBanks();
		for (DbLcBank newOne : bigBanks.values()) {
			if (choose.containsKey(newOne.getName())) {
				newOne.setIs_choose(true);
			}
			result.add(newOne);
		}
		return result;
	}

	public List<DbLcBank> getOtherBanks() {
		List<DbLcBank> result = new ArrayList<DbLcBank>();
		Map<String, Boolean> choose = getLcBanks();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select name,logo from card_banks where name not in (select bank_name from card_bank_cards) and name not in ('中国银行','农业银行','工商银行','建设银行', '交通银行', '民生银行', '兴业银行', '招商银行', '华夏银行', '中信银行') order by logo", new String[] {});
		while (cursor.moveToNext()) {
			DbLcBank temp = new DbLcBank();
			temp.setLogo(cursor.getString(1));
			temp.setName(cursor.getString(0));
			if (choose.containsKey(cursor.getString(0))) {
				temp.setIs_choose(true);
			}
			result.add(temp);
		}
		cursor.close();
		return result;
	}

	public void deleteOneLc(String bankname){

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from lccp where bank_name=?", new String[] { bankname });
	
	}
	
	public void setLcBank(String bankname,String logo,boolean isCheck){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		deleteOneLc(bankname);
		if(isCheck){
			ContentValues values = new ContentValues();
			values.put("bank_name", bankname);
			values.put("bank_logo", logo);
			values.put("is_choose", true);
			values.put("thedate", System.currentTimeMillis());
			db.insert("lccp", null, values);
		}
	}
}
