package com.yeetou.xinyongkaguanjia.http.base;

import java.util.Calendar;
import java.util.Date;

import com.yeetou.xinyongkaguanjia.db.base.DbStream;

public class StreamBase {

	private Integer id;
	private boolean pay_flag;
	private long trade_date;
	private long post_date;
	private String description;
	private float amount;
	private String category;
	private int bank_card_id;
	private int bill_id;
	private int message_id;
	private String currency;
	private int state;
	private long created_at;
	private long updated_at;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isPay_flag() {
		return pay_flag;
	}

	public void setPay_flag(boolean pay_flag) {
		this.pay_flag = pay_flag;
	}

	public long getTrade_date() {
		return trade_date;
	}

	public void setTrade_date(long trade_date) {
		this.trade_date = trade_date;
	}

	public long getPost_date() {
		return post_date;
	}

	public void setPost_date(long post_date) {
		this.post_date = post_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getBank_card_id() {
		return bank_card_id;
	}

	public void setBank_card_id(int bank_card_id) {
		this.bank_card_id = bank_card_id;
	}

	public int getBill_id() {
		return bill_id;
	}

	public void setBill_id(int bill_id) {
		this.bill_id = bill_id;
	}

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public long getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(long updated_at) {
		this.updated_at = updated_at;
	}

	public DbStream toDbStream() {

		DbStream dbStream = new DbStream();
		Calendar cal = Calendar.getInstance();  
		cal.setTime(new Date(trade_date));
		dbStream.setId(id);
		dbStream.setThedate(trade_date);
		dbStream.setYear(cal.get(Calendar.YEAR));
		dbStream.setMonth(cal.get(Calendar.MONTH)+1);
		dbStream.setDay(cal.get(Calendar.DATE));
		dbStream.setDescription(description);
		dbStream.setAmount(amount);
		dbStream.setCategory(category);
		dbStream.setMessage_id(message_id);
		dbStream.setBank_card_id(bank_card_id);
		dbStream.setBill_id(bill_id);
		dbStream.setIsPay(pay_flag ? 1 : 0);
		dbStream.setCurrency(currency);
		dbStream.setState(state);
		return dbStream;
	}

}
