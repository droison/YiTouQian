package com.yeetou.xinyongkaguanjia.http.base;

public class EmailCheckBase {
	private int code;
	private String msg;
	private String c_email;
	private String secret;
	private String email;
	private int flag;
	private String bill_cnt;
	private String card_cnt;

	public String getBill_cnt() {
		return bill_cnt;
	}

	public void setBill_cnt(String bill_cnt) {
		this.bill_cnt = bill_cnt;
	}

	public String getCard_cnt() {
		return card_cnt;
	}

	public void setCard_cnt(String card_cnt) {
		this.card_cnt = card_cnt;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getC_email() {
		return c_email;
	}

	public void setC_email(String c_email) {
		this.c_email = c_email;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
