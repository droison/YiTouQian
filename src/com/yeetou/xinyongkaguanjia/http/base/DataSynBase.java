package com.yeetou.xinyongkaguanjia.http.base;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;
import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;
import com.yeetou.xinyongkaguanjia.db.base.DbCardToBills;
import com.yeetou.xinyongkaguanjia.db.base.DbEmail;

public class DataSynBase {
	private Integer code;
	private String msg;
	private Data book_data;
	private String secret;

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

	public Data getBook_data() {
		return book_data;
	}

	public void setBook_data(Data book_data) {
		this.book_data = book_data;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public class Data {
		private List<DbEmail> card_emails;
		private List<DbBankCard> card_bank_cards;
		private List<StreamBase> card_streams;
		private List<DbCardToBills> card_card_bills;
		private List<DbCardBills> card_bills;

		public List<DbEmail> getCard_emails() {
			return card_emails;
		}

		public void setCard_emails(List<DbEmail> card_emails) {
			this.card_emails = card_emails;
		}

		public List<DbBankCard> getCard_bank_cards() {
			return card_bank_cards;
		}

		public void setCard_bank_cards(List<DbBankCard> card_bank_cards) {
			this.card_bank_cards = card_bank_cards;
		}

		public List<StreamBase> getCard_streams() {
			return card_streams;
		}

		public void setCard_streams(List<StreamBase> card_streams) {
			this.card_streams = card_streams;
		}

		public List<DbCardToBills> getCard_card_bills() {
			return card_card_bills;
		}

		public void setCard_card_bills(List<DbCardToBills> card_card_bills) {
			this.card_card_bills = card_card_bills;
		}

		public List<DbCardBills> getCard_bills() {
			return card_bills;
		}

		public void setCard_bills(List<DbCardBills> card_bills) {
			this.card_bills = card_bills;
		}

	}
}
