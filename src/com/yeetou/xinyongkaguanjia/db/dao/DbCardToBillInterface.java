package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbCardToBills;

public interface DbCardToBillInterface {

	// 获取所有类目
	public abstract List<DbCardToBills> getAll();

	// 插入一个新的类目
	public abstract void synSave(List<DbCardToBills> cardtobills);
	
	public abstract List<Integer> getBillIdS(String bankCardId);
	public abstract List<Integer> getCardIdS(int billId);
	public abstract Integer getLastBillId(int bankCardId);
}
