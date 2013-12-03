package com.yeetou.xinyongkaguanjia.http.base;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbBank;

public class BankSynBase {
	private Integer code;
	private String msg;
	private String secret;
	private List<DbBank> data;

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

	public List<DbBank> getData() {
		return data;
	}

	public void setData(List<DbBank> data) {
		this.data = data;
	}

}
