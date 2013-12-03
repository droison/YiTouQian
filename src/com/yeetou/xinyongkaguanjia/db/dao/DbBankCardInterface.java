package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbBankCard;

public interface DbBankCardInterface {

	// 获取所有储蓄卡信息
	public abstract List<DbBankCard> getAllCreditCard();

	// 获取所有信用卡信息
	public abstract List<DbBankCard> getAllDebitCard();

	// 插入一个新的银行卡
	public abstract void synSave(List<DbBankCard> bankcards);
}
