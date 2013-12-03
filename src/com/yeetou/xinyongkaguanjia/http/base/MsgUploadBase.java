package com.yeetou.xinyongkaguanjia.http.base;

import java.util.List;

public class MsgUploadBase {

	private int code;
	private List<MsgDataBase> data;
	private String email;
	private String secret;
	private int sms_cnt;
	private int card_cnt;
	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<MsgDataBase> getData() {
		return data;
	}

	public void setData(List<MsgDataBase> data) {
		this.data = data;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getSms_cnt() {
		return sms_cnt;
	}

	public void setSms_cnt(int sms_cnt) {
		this.sms_cnt = sms_cnt;
	}

	public int getCard_cnt() {
		return card_cnt;
	}

	public void setCard_cnt(int card_cnt) {
		this.card_cnt = card_cnt;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
