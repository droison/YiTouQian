package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.db.base.DbEmail;

public interface DbEmailInterface {

	// 获取所有类目
	public abstract List<DbEmail> getAllEmail();

	// 插入一个新的类目
	public abstract boolean save(DbEmail email);
	
	public abstract void delete();
}
