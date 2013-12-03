package com.yeetou.xinyongkaguanjia.http.base;

public class UserLoginBase {
	private int code;
	private String msg;
	private String email;
	private String secret;
	private int existing;
	private String number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public int getExisting() {
		return existing;
	}

	public void setExisting(int existing) {
		this.existing = existing;
	}

}
