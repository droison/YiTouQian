package com.yeetou.xinyongkaguanjia.info;

public class EmailCheckInfo {

	private String ver;
	private String email;
	private String secret;
	private String c_email;
	private String raw_pwd;
	private Device device;
	private String iv;
	
	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
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

	public String getC_email() {
		return c_email;
	}

	public void setC_email(String c_email) {
		this.c_email = c_email;
	}

	public String getRaw_pwd() {
		return raw_pwd;
	}

	public void setRaw_pwd(String raw_pwd) {
		this.raw_pwd = raw_pwd;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public String getIv() {
		return iv;
	}

	public void setIv(String iv) {
		this.iv = iv;
	}

	
}
