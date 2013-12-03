package com.yeetou.xinyongkaguanjia.db.base;

public class DbCardToBills {

	private Integer id;
	private Integer bank_card_id;
	private Integer bill_id;
	private long created_at;
	private long updated_at;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBank_card_id() {
		return bank_card_id;
	}

	public void setBank_card_id(Integer bank_card_id) {
		this.bank_card_id = bank_card_id;
	}

	public Integer getBill_id() {
		return bill_id;
	}

	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
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
