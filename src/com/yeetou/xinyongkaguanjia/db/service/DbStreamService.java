package com.yeetou.xinyongkaguanjia.db.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yeetou.xinyongkaguanjia.R;
import com.yeetou.xinyongkaguanjia.db.DBHelper;
import com.yeetou.xinyongkaguanjia.db.base.DbBank;
import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbStream;
import com.yeetou.xinyongkaguanjia.db.dao.DbStreamInterface;
import com.yeetou.xinyongkaguanjia.http.base.StreamBase;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.StreamDayInfo;
import com.yeetou.xinyongkaguanjia.info.StreamInfo;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;
import com.yeetou.xinyongkaguanjia.info.YearMonthPayments;

public class DbStreamService implements DbStreamInterface {

	private DBHelper dbHelper;
	private Context mContext;

	private DbStreamService() {

	}

	public DbStreamService(Context mContext) {
		DBHelper.init(mContext);
		this.mContext = mContext;
		this.dbHelper = DBHelper.dbHelper();
	}

	@Override
	public List<MonthPayments> getMonthPayments(int year) {
		List<MonthPayments> result = new ArrayList<MonthPayments>();
		for (int i = 12; i > 0; i--) {
			MonthPayments temp = getMonthPayments(year, i);
			if (temp.getIncome() == 0 && temp.getExpand() == 0) {
				continue;
			} else {
				result.add(temp);
			}
		}
		return result;
	}

	@Override
	public MonthPayments getMonthPayments(int year, int month) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select sum(a.amount) as amount from card_streams as a inner join card_bank_cards as b on a.bank_card_id=b._id where b.card_type='credit' and a.year=? and a.month=? and a.ispay=1", new String[] { String.valueOf(year), String.valueOf(month) });
		MonthPayments monthPayments = new MonthPayments();
		monthPayments.setYear(year);
		monthPayments.setMonth(month);
		monthPayments.setIncome(0);
		monthPayments.setExpand(0);
		if (cursor.moveToFirst()) {
			monthPayments.setExpand(cursor.getFloat(0));
			monthPayments.setIncome(-cursor.getFloat(0));
		}
		cursor = db.rawQuery("select sum(a.amount) as amount from card_streams as a inner join card_bank_cards as b on a.bank_card_id=b._id where b.card_type='debit' and a.year=? and a.month=? and a.ispay=0", new String[] { String.valueOf(year), String.valueOf(month) });
		if (cursor.moveToFirst()) {
			monthPayments.setIncome(-cursor.getFloat(0));
		}
		cursor.close();
		return monthPayments;
	}

	public MonthPayments getCurMonthPayments(String bank_card_id) {
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		if (bank_card_id != null) {
			cursor = db.rawQuery("select sum(case ispay when 1 then amount else 0 end) as expend,sum(case ispay when 0 then amount else 0 end) as income from card_streams where year=? and month=? and bank_card_id=?", new String[] { String.valueOf(year), String.valueOf(month), bank_card_id });
		} else {
			cursor = db.rawQuery("select sum(case ispay when 1 then amount else 0 end) as expend,sum(case ispay when 0 then amount else 0 end) as income from card_streams where year=? and month=?", new String[] { String.valueOf(year), String.valueOf(month) });
		}
		MonthPayments monthPayments = new MonthPayments();
		monthPayments.setYear(year);
		monthPayments.setMonth(month);
		monthPayments.setIncome(0);
		monthPayments.setExpand(0);
		if (cursor.moveToFirst()) {
			monthPayments.setExpand(cursor.getFloat(0));
			monthPayments.setIncome(-cursor.getFloat(1));
		}
		cursor.close();
		return monthPayments;
	}

	public Float getBalanceByCard(String bank_card_id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		if (bank_card_id != null) {
			cursor = db.rawQuery("select sum(case ispay when 1 then amount else 0 end) as expend,sum(case ispay when 0 then amount else 0 end) as income from card_streams where bank_card_id=?", new String[] { bank_card_id });
		} else {
			cursor = db.rawQuery("select sum(case ispay when 1 then amount else 0 end) as expend,sum(case ispay when 0 then amount else 0 end) as income from card_streams", new String[] {});
		}
		float result = 0;
		if (cursor.moveToFirst()) {
			result = cursor.getFloat(0) + cursor.getFloat(1);
			result = -result;
		}
		cursor.close();

		return result;
	}

	@Override
	public Map<String, Float> getExpandByCategory(String thedate) {
		int year = 0, month = 0;
		String[] dates = thedate.split("-");
		year = Integer.valueOf(dates[0]);
		month = Integer.valueOf(dates[1]);
		Map<String, Float> map = new HashMap<String, Float>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select category,sum(amount) as amount from card_streams as a inner join card_bank_cards as b on a.bank_card_id=b._id where b.card_type='credit' and a.year=? and a.month=? and a.ispay=1 group by a.category", new String[] { String.valueOf(year), String.valueOf(month) });
		while (cursor.moveToNext()) {
			map.put(cursor.getString(0), cursor.getFloat(1));

		}
		cursor.close();
		dbHelper.close();
		return map;

	}

	@Override
	public List<StreamMonthInfo> getStreams(String thedate, String category) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		int year = 0, month = 0;
		if (thedate.equals("全部")) {
			year = -1;
			month = -1;
		} else {
			String[] times = thedate.split("-");
			year = Integer.valueOf(times[0]);
			month = Integer.valueOf(times[1]);
		}
		if (category.equals("全部")) {
			category = "-1";
		}
		
		if (year == -1 || month == -1) {
			if (category.equals("-1")) {
				cursor = db.rawQuery("select * from card_streams where state=0 and currency='RMB' and isPay=1 and message_id=0 and category in ('吃喝','购物','网购','出行','生活','玩乐','爱车')order by year desc,month desc,day desc", new String[] {});
			} else {
				cursor = db.rawQuery("select * from card_streams where category=? and state=0 and currency='RMB' and isPay=1 and message_id=0 and category in ('吃喝','购物','网购','出行','生活','玩乐','爱车')order by year desc,month desc,day desc", new String[] { category });
			}
		} else {
			if (category.equals("-1")) {
				cursor = db.rawQuery("select * from card_streams where year=? and month=? and state=0 and currency='RMB' and isPay=1 and message_id=0 and category in ('吃喝','购物','网购','出行','生活','玩乐','爱车')order by year desc,month desc,day desc", new String[] { String.valueOf(year), String.valueOf(month) });
			} else {
				cursor = db.rawQuery("select * from card_streams where year=? and month=? and category=? and state=0 and currency='RMB' and isPay=1 and message_id=0 and category in ('吃喝','购物','网购','出行','生活','玩乐','爱车')order by year desc,month desc,day desc", new String[] { String.valueOf(year), String.valueOf(month), category });
			}
		}
		List<StreamMonthInfo> result = cursorToList(cursor);
		cursor.close();

		return result;
	}

	public List<StreamMonthInfo> getStreams(String bank_card_id, boolean isCredit) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		if (isCredit) {
			cursor = db.rawQuery("select * from card_streams where state=0 and currency='RMB' and isPay=1 and message_id=0 and bank_card_id=? and category in ('吃喝','购物','网购','出行','生活','玩乐','爱车')order by year desc,month desc,day desc", new String[] { bank_card_id });

		} else {
			cursor = db.rawQuery("select * from card_streams where state=0 and currency='RMB' and bank_card_id=? order by year desc,month desc,day desc", new String[] { bank_card_id });

		}

		List<StreamMonthInfo> result = cursorToList(cursor);
		cursor.close();

		return result;
	}

	@Override
	public void save(DbStream dbStream) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put("_id", dbStream.getId());
		values.put("thedate", dbStream.getThedate());
		values.put("year", dbStream.getYear());
		values.put("month", dbStream.getMonth());
		values.put("day", dbStream.getDay());
		values.put("description", dbStream.getDescription());
		values.put("amount", dbStream.getAmount());
		values.put("category", dbStream.getCategory());
		values.put("message_id", dbStream.getMessage_id());
		values.put("bank_card_id", dbStream.getBank_card_id());
		values.put("bill_id", dbStream.getBill_id());
		values.put("state", dbStream.getState());
		values.put("currency", dbStream.getCurrency());
		values.put("isPay", dbStream.getIsPay());

		db.insert("card_streams", null, values);

	}

	@Override
	public Map<String, Float> getExpandByCategory(int year, int month, String bankname, String number) {

		Map<String, Float> map = new HashMap<String, Float>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.rawQuery("select category,sum(amount) as amount from card_streams where year=? and month=? and bank_name=? and card_number=? and ispay=1 group by category", new String[] { String.valueOf(year), String.valueOf(month), bankname, number });
		while (cursor.moveToNext()) {
			map.put(cursor.getString(0), cursor.getFloat(1));

		}
		cursor.close();
		dbHelper.close();
		if (map.size() == 0)
			return null;
		else
			return map;

	}

	private List<StreamMonthInfo> cursorToList(Cursor cursor) {
		List<StreamMonthInfo> result = new ArrayList<StreamMonthInfo>();
		List<StreamDayInfo> streamdays = new ArrayList<StreamDayInfo>();
		List<StreamInfo> streams = new ArrayList<StreamInfo>();

		StreamMonthInfo currMonth = null;
		StreamDayInfo currDay = null;
		int currd = 0;
		int curry = 0;
		int currm = 0;

		while (cursor.moveToNext()) {
			long thedate = cursor.getLong(1);
			int year = cursor.getInt(2);
			int month = cursor.getInt(3);
			int day = cursor.getInt(4);
			String description = cursor.getString(5);
			float amount = cursor.getFloat(6);
			String category = null;
			int bank_card_id = cursor.getInt(9);
			int isPay = cursor.getInt(11);

			String card_number = cursor.getString(14);
			String bank_logo = cursor.getString(15);
			String bank_name = cursor.getString(16);

			if (isPay != 0) {
				category = cursor.getString(7);
			} else {
				category = "收入";
				amount = 0 - amount;
			}
			StreamInfo streamInfo = new StreamInfo();
			streamInfo.setAmount(amount);
			streamInfo.setBank(bank_name);
			streamInfo.setBank_logo(bank_logo);
			streamInfo.setCard_num(card_number);
			streamInfo.setCategory(category);
			streamInfo.setDes(description);
			streamInfo.setTrade_time(thedate);
			streamInfo.setBank_id(bank_card_id);

			if (curry == year && currm == month && currd == day) {
				streams.add(streamInfo);
			} else if (curry == year && currm == month && currd != day) {
				currd = day;
				currDay.setStreams(streams);
				streamdays.add(currDay.clone());
				streams.clear();
				currDay = new StreamDayInfo();
				currDay.setDay(day);
				currDay.setMonth(month);
				currDay.setYear(year);
				streams.add(streamInfo);
			} else if (curry != year || currm != month) {
				if (currMonth != null) {
					currDay.setStreams(streams);
					streamdays.add(currDay.clone());
					streams.clear();
					currMonth.setStreamdays(streamdays);
					result.add(currMonth.clone());
					streamdays.clear();
				}
				curry = year;
				currm = month;
				currd = day;
				currMonth = new StreamMonthInfo();
				currMonth.setMonth(month);
				currMonth.setYear(year);
				currDay = new StreamDayInfo();
				currDay.setDay(day);
				currDay.setMonth(month);
				currDay.setYear(year);
				streams.add(streamInfo);
			}

		}
		if (currMonth != null) {
			currDay.setStreams(streams);
			streamdays.add(currDay);
			currMonth.setStreamdays(streamdays);
			result.add(currMonth);
		}
		return result;
	}

	// 支出只取信用卡，收入只取储蓄卡
	@Override
	public List<YearMonthPayments> getMonthPayments() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select year from card_streams group by year", new String[] {});

		List<Integer> years = new ArrayList<Integer>();
		while (cursor.moveToNext()) {
			years.add(cursor.getInt(0));
		}
		List<YearMonthPayments> result = new ArrayList<YearMonthPayments>();
		for (int i = 0; i < years.size(); i++) {
			List<MonthPayments> temp = getMonthPayments(years.get(i));
			if (temp.size() != 0) {
				YearMonthPayments ymp = new YearMonthPayments();
				ymp.setMonthPayments(temp);
				ymp.setYear(years.get(i));
				result.add(ymp);
			}
		}
		return result;
	}

	public List<MonthPayments> getMonthPaymentsByCard(String bank_card_id, boolean isCredit) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = null;
		if (isCredit) {
			cursor = db.rawQuery("select sum(amount) as amount,ispay,year,month from card_streams where state=0 and currency='RMB' and isPay=1 and message_id=0 and bank_card_id=? group by year,month order by year desc,month desc", new String[] { bank_card_id });
		} else {
			cursor = db.rawQuery("select sum(amount) as amount,ispay,year,month from card_streams where state=0 and currency='RMB' and bank_card_id=? group by year,month order by year desc,month desc", new String[] { bank_card_id });
		}
		List<MonthPayments> result = new ArrayList<MonthPayments>();
		MonthPayments monthPayments = new MonthPayments();
		monthPayments.setYear(0);
		monthPayments.setMonth(0);
		while (cursor.moveToNext()) {
			int year = cursor.getInt(2);
			int month = cursor.getInt(3);
			if (year == monthPayments.getYear() && month == monthPayments.getMonth()) {
				if (cursor.getInt(1) == 1) {
					monthPayments.setExpand(cursor.getFloat(0));
				} else {
					monthPayments.setIncome(-cursor.getFloat(0));
				}
				result.add(monthPayments);
				monthPayments = new MonthPayments();
				monthPayments.setYear(0);
				monthPayments.setMonth(0);
			} else {
				if (monthPayments.getExpand() != 0 || monthPayments.getIncome() != 0) {
					result.add(monthPayments);
				}
				monthPayments = new MonthPayments();
				monthPayments.setYear(year);
				monthPayments.setMonth(month);
				if (cursor.getInt(1) == 1) {
					monthPayments.setExpand(cursor.getFloat(0));
				} else {
					monthPayments.setIncome(-cursor.getFloat(0));
				}
			}
		}
		if (monthPayments.getExpand() != 0 || monthPayments.getIncome() != 0) {
			result.add(monthPayments);
		}
		cursor.close();

		return result;
	}

	@Override
	public void save(List<DbStream> dbStreams) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		Cursor cur = null;
		for (DbStream dbStream : dbStreams) {
			delete(dbStream.getId());
			values.put("_id", dbStream.getId());
			values.put("thedate", dbStream.getThedate());
			values.put("year", dbStream.getYear());
			values.put("month", dbStream.getMonth());
			values.put("day", dbStream.getDay());
			values.put("description", dbStream.getDescription());
			values.put("amount", dbStream.getAmount());
			values.put("category", dbStream.getCategory());
			values.put("message_id", dbStream.getMessage_id());
			values.put("bank_card_id", dbStream.getBank_card_id());
			values.put("bill_id", dbStream.getBill_id());
			values.put("state", dbStream.getState());
			values.put("currency", dbStream.getCurrency());
			values.put("isPay", dbStream.getIsPay());

			cur = db.rawQuery("select number,bank_logo,bank_name from card_bank_cards where _id=? limit 1", new String[] { dbStream.getBank_card_id() + "" });
			if (cur.moveToFirst()) {
				values.put("card_number", cur.getString(0));
				values.put("bank_logo", cur.getString(1));
				values.put("bank_name", cur.getString(2));
			}

			db.insert("card_streams", null, values);
		}
		cur.close();

	}

	public boolean isNull() {

		boolean result = true;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from card_streams limit 1", new String[] {});
		if (cursor.moveToFirst())
			result = false;

		return result;
	}

	@Override
	public List<String> getAllDate() {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select year,month from card_streams as a inner join card_bank_cards as b on a.bank_card_id=b._id where b.card_type='credit' group by year,month", new String[] {});
		List<String> result = new ArrayList<String>();
		while (cursor.moveToNext()) {
			result.add(cursor.getInt(0) + "-" + cursor.getInt(1));
		}
		cursor.close();

		return result;
	}

	@Override
	public void synSaveStreamBase(List<StreamBase> streamBases) {
		List<DbStream> dbStreams = new ArrayList<DbStream>();
		for (StreamBase sb : streamBases) {
			DbStream sbs = sb.toDbStream();
			dbStreams.add(sbs);
		}
		save(dbStreams);
	}

	private void delete(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL("delete from card_streams where _id=?", new Integer[] { id });
	}

}
