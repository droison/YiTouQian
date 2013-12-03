package com.yeetou.xinyongkaguanjia.db.base;

public class DbLcBank {

	private String name;
	private String logo;
	private boolean is_choose = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public boolean isIs_choose() {
		return is_choose;
	}

	public void setIs_choose(boolean is_choose) {
		this.is_choose = is_choose;
	}
	public DbLcBank() {
		
	}
	public DbLcBank(String name, String logo) {
		this.name = name;
		this.logo = logo;
	}
}
