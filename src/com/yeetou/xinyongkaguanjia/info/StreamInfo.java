package com.yeetou.xinyongkaguanjia.info;

public class StreamInfo {

	private long trade_time;
	private String category;// 若为收入，则此处显示 收入
	private float amount;
	private String des; // 描述文字
	private String card_num; // 卡后四位
	private String bank; // 所属银行名，例如 “兴业银行” 
	private String bank_logo; // 该银行对应的资源位置
	private int bank_id; //卡在数据库中的ID
	
	
	public int getBank_id() {
		return bank_id;
	}

	public void setBank_id(int bank_id) {
		this.bank_id = bank_id;
	}



	public long getTrade_time() {
		return trade_time;
	}

	public void setTrade_time(long trade_time) {
		this.trade_time = trade_time;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public String getCard_num() {
		return card_num;
	}

	public void setCard_num(String card_num) {
		this.card_num = card_num;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBank_logo() {
		return bank_logo;
	}

	public void setBank_logo(String bank_logo) {
		this.bank_logo = bank_logo;
	}

}
