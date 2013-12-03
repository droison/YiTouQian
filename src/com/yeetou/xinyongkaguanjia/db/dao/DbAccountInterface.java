package com.yeetou.xinyongkaguanjia.db.dao;

import com.yeetou.xinyongkaguanjia.db.base.DbAccount;

public interface DbAccountInterface {

	// 获取所有类目
	public abstract DbAccount get();

	// 插入一个新的类目
	public abstract boolean saveOrUpdate(DbAccount account);

	// 获取所有类目
	public abstract void delete();

}
