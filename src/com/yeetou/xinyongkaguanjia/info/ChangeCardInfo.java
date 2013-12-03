package com.yeetou.xinyongkaguanjia.info;

public class ChangeCardInfo {

	private String ver;
	private String email;
	private String secret;
	private Card card;
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

	public Card getCard() {
		return card;
	}

	public void setCard(Card cardInfo) {
		this.card = cardInfo;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

}
