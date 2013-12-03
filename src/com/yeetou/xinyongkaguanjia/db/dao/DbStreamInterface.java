package com.yeetou.xinyongkaguanjia.db.dao;

import java.util.List;
import java.util.Map;

import com.yeetou.xinyongkaguanjia.db.base.DbStream;
import com.yeetou.xinyongkaguanjia.http.base.StreamBase;
import com.yeetou.xinyongkaguanjia.info.MonthPayments;
import com.yeetou.xinyongkaguanjia.info.StreamMonthInfo;
import com.yeetou.xinyongkaguanjia.info.YearMonthPayments;

public interface DbStreamInterface {

	// 获取月收支，按时间倒排，按年取数据
	public abstract List<MonthPayments> getMonthPayments(int year);

	// 获取月收支，按时间倒排
	public abstract List<YearMonthPayments> getMonthPayments();
	// 获取某月收支
	public abstract MonthPayments getMonthPayments(int year, int month);

	// 获取某月的支出，如果该类目支出为0，则不会返回该类目,若当月没有，则返回null
	public abstract Map<String, Float> getExpandByCategory(String thedate);

	// 获取某月的支出，如果该类目支出为0，则不会返回该类目,若当月没有，则返回null
	public abstract Map<String, Float> getExpandByCategory(int year, int month, String bankname, String number);

	// thedate:2013-9 取流水数据,若取所有时间，则传入thedate为"-1",若取所有类目，category传入参数为"-1" 注：收入部分默认类目均为 收入
	public abstract List<StreamMonthInfo> getStreams(String thedate, String category);

	public abstract void save(DbStream dbStream);
	
	public abstract void save(List<DbStream> dbStreams);
	
	public abstract List<String> getAllDate();
	
	public abstract void synSaveStreamBase(List<StreamBase> streamBases);
}
