package com.yeetou.xinyongkaguanjia.info;

import java.util.List;

public class MsgUploadInfo {

	private String ver;
	private String email;
	private String secret;
	private List<MsgInfo> messages;
	private Device device;

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

	public List<MsgInfo> getMessages() {
		return messages;
	}

	public void setMessages(List<MsgInfo> messages) {
		this.messages = messages;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
