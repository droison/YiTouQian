package com.yeetou.xinyongkaguanjia.db.base;

import java.io.Serializable;

public class DbBankCard {

	/**
	 * 
	 */
	private Integer id;
	private String card_type; // credit
	private int bank_id;
	private String name; // yonghuming
	private String sex;
	private String number;
	private Integer credit_limit;
	private Integer cash_limit;
	private String source_from;
	private long created_at;
	private long updated_at;
	private String bank_name;
	private String bank_logo;

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_logo() {
		return bank_logo;
	}

	public void setBank_logo(String bank_logo) {
		this.bank_logo = bank_logo;
	}

	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getCredit_limit() {
		return credit_limit;
	}

	public void setCredit_limit(Integer credit_limit) {
		this.credit_limit = credit_limit;
	}

	public Integer getCash_limit() {
		return cash_limit;
	}

	public void setCash_limit(Integer cash_limit) {
		this.cash_limit = cash_limit;
	}

	public String getSource_from() {
		return source_from;
	}

	public void setSource_from(String source_from) {
		this.source_from = source_from;
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
