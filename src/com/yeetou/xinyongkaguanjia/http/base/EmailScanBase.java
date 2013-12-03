package com.yeetou.xinyongkaguanjia.http.base;

public class EmailScanBase {
	private Integer code;
	private String msg;
	private Integer bill_cnt;
	private Integer card_cnt;
	private String secret;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getBill_cnt() {
		return bill_cnt;
	}

	public void setBill_cnt(Integer bill_cnt) {
		this.bill_cnt = bill_cnt;
	}

	public Integer getCard_cnt() {
		return card_cnt;
	}

	public void setCard_cnt(Integer card_cnt) {
		this.card_cnt = card_cnt;
	}

}
