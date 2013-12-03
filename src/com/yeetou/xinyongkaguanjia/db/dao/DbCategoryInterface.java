package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

public interface DbCategoryInterface {

	// 获取所有类目
	public abstract List<String> getAllCategory();

	// 插入一个新的类目
	public abstract void save(List<String> categorys);
}
