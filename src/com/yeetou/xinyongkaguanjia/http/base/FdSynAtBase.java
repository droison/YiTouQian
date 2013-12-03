package com.yeetou.xinyongkaguanjia.http.base;

import java.util.List;

public class FdSynAtBase {
	private String Ver;
	private String email;
	private Id_list Id_list;
	private String secret;

	public String getVer() {
		return Ver;
	}

	public void setVer(String ver) {
		Ver = ver;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Id_list getId_list() {
		return Id_list;
	}

	public void setId_list(Id_list id_list) {
		Id_list = id_list;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public class Id_list {
		private List<Integer> card_emails;
		private List<Integer> card_bank_cards;
		private List<Integer> card_streams;
		private List<Integer> card_card_bills;
		private List<Integer> card_bills;
		
		
		
		public List<Integer> getCard_emails() {
			return card_emails;
		}
		public void setCard_emails(List<Integer> card_emails) {
			this.card_emails = card_emails;
		}
		public List<Integer> getCard_bank_cards() {
			return card_bank_cards;
		}
		public void setCard_bank_cards(List<Integer> card_bank_cards) {
			this.card_bank_cards = card_bank_cards;
		}
		public List<Integer> getCard_streams() {
			return card_streams;
		}
		public void setCard_streams(List<Integer> card_streams) {
			this.card_streams = card_streams;
		}
		public List<Integer> getCard_card_bills() {
			return card_card_bills;
		}
		public void setCard_card_bills(List<Integer> card_card_bills) {
			this.card_card_bills = card_card_bills;
		}
		public List<Integer> getCard_bills() {
			return card_bills;
		}
		public void setCard_bills(List<Integer> card_bills) {
			this.card_bills = card_bills;
		}
		

	}
}
