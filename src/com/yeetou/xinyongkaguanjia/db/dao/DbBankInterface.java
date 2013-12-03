package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbBank;

public interface DbBankInterface {

	// 获取所有银行信息
	public abstract List<DbBank> getAllBank();

	// 插入新银行
	public abstract void synSave(List<DbBank> banks);

	// 删除所有银行
	public abstract void deleteAll();

	// 获取一个银行信息
	public abstract DbBank getById(String id);

	// 获取一个银行信息
	public abstract DbBank getByName(String name);
}
