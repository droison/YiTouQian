package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;

import com.yeetou.xinyongkaguanjia.info.MsgInfo;

public interface DbSMSInterface {

	// 获取所有类目
	public abstract List<MsgInfo> getAllSMS();

	// 插入一个新的类目
	public abstract void save(List<MsgInfo> msgs);
	
	public abstract void deleteAll();
}
