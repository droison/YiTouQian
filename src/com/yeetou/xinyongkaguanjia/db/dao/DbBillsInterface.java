package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbCardBills;

public interface DbBillsInterface {

	// 获取所有类目
	public abstract List<DbCardBills> getAll();

	// 插入
	public abstract void synSave(List<DbCardBills> cardbills);
	
	// 获取所有类目
	public abstract List<DbCardBills> getAll(List<Integer> billIds);
	
	public abstract DbCardBills getOne(int billId);
}
