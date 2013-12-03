package com.yeetou.xinyongkaguanjia.db.base;

public class DbStream {

	private Integer id;
	private long thedate;
	private int year;
	private int month;
	private int day;
	private String description;
	private float amount;
	private String category;
	private int message_id;
	private int bank_card_id;
	private int bill_id;
	private int isPay;
	private String currency;
	private int state;
	private String card_number;
	private String bank_logo;
	private String bank_name;

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getBank_logo() {
		return bank_logo;
	}

	public void setBank_logo(String bank_logo) {
		this.bank_logo = bank_logo;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public long getThedate() {
		return thedate;
	}

	public void setThedate(long thedate) {
		this.thedate = thedate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	public int getMessage_id() {
		return message_id;
	}

	public void setMessage_id(int message_id) {
		this.message_id = message_id;
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

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
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

}
