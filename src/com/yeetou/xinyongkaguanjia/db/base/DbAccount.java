package com.yeetou.xinyongkaguanjia.db.base;

public class DbAccount {

	private Integer id;
	private String phone;
	private String email;
	private String secret;
	private String token;
	private long msgscan_at;
	private long syn_at;
	private String iv;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getMsgscan_at() {
		return msgscan_at;
	}

	public void setMsgscan_at(long msgscan_at) {
		this.msgscan_at = msgscan_at;
	}

	public long getSyn_at() {
		return syn_at;
	}

	public void setSyn_at(long syn_at) {
		this.syn_at = syn_at;
	}

	public boolean check() {
		return syn_at != 0 && msgscan_at != 0 && phone != null && email != null && token != null;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

}
