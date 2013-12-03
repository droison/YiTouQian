package com.yeetou.xinyongkaguanjia.http.base;

import java.io.Serializable;
import java.util.List;

public class LccpBase implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6368927308897387850L;
	private Integer code;
	private String msg;
	private String secret;
	private List<Lccp> lccps;

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

	public List<Lccp> getLccps() {
		return lccps;
	}

	public void setLccps(List<Lccp> lccps) {
		this.lccps = lccps;
	}

	public class Lccp implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8765464106151947041L;
		private String id;
		private String name;
		private Float profit;
		private String bank;
		private String bank_id;
		private Integer period;
		private Integer amt;
		private String website;
		private String profit_type;
		private String hot_line;
		private String sale_state;
		private List<String> advise;
		private List<String> feature;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Float getProfit() {
			return profit;
		}

		public void setProfit(Float profit) {
			this.profit = profit;
		}

		public String getBank() {
			return bank;
		}

		public void setBank(String bank) {
			this.bank = bank;
		}

		public String getBank_id() {
			return bank_id;
		}

		public void setBank_id(String bank_id) {
			this.bank_id = bank_id;
		}

		public Integer getPeriod() {
			return period;
		}

		public void setPeriod(Integer period) {
			this.period = period;
		}

		public Integer getAmt() {
			return amt;
		}

		public void setAmt(Integer amt) {
			this.amt = amt;
		}

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}

		public String getProfit_type() {
			return profit_type;
		}

		public void setProfit_type(String profit_type) {
			this.profit_type = profit_type;
		}

		public String getHot_line() {
			return hot_line;
		}

		public void setHot_line(String hot_line) {
			this.hot_line = hot_line;
		}

		public String getSale_state() {
			return sale_state;
		}

		public void setSale_state(String sale_state) {
			this.sale_state = sale_state;
		}

		public List<String> getAdvise() {
			return advise;
		}

		public void setAdvise(List<String> advise) {
			this.advise = advise;
		}

		public List<String> getFeature() {
			return feature;
		}

		public void setFeature(List<String> feature) {
			this.feature = feature;
		}

	}
}
