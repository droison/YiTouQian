package com.yeetou.xinyongkaguanjia.db.base;

public class DbCardBills {

	private Integer id;
	private String bill_type;
	private String year;
	private String month;
	private long due_date;
	private long billing_date;
	private float new_balance;
	private float min_payment;
	private Integer pay_state;
	private long created_at;
	private long updated_at;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBill_type() {
		return bill_type;
	}

	public void setBill_type(String bill_type) {
		this.bill_type = bill_type;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public long getDue_date() {
		return due_date;
	}

	public void setDue_date(long due_date) {
		this.due_date = due_date;
	}

	public long getBilling_date() {
		return billing_date;
	}

	public void setBilling_date(long billing_date) {
		this.billing_date = billing_date;
	}

	public float getNew_balance() {
		return new_balance;
	}

	public void setNew_balance(float new_balance) {
		this.new_balance = new_balance;
	}

	public float getMin_payment() {
		return min_payment;
	}

	public void setMin_payment(float min_payment) {
		this.min_payment = min_payment;
	}

	public Integer getPay_state() {
		return pay_state;
	}

	public void setPay_state(Integer pay_state) {
		this.pay_state = pay_state;
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

}
