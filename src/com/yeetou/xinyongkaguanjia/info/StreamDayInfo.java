package com.yeetou.xinyongkaguanjia.info;

import java.util.ArrayList;
import java.util.List;

public class StreamDayInfo implements Cloneable{

	private int year;
	private int month;
	private int day;
	private List<StreamInfo> streams;   //该List时间倒排
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public List<StreamInfo> getStreams() {
		return streams;
	}
	public void setStreams(List<StreamInfo> streams) {
		this.streams = streams;
	}
	
	@Override
	public StreamDayInfo clone(){
		StreamDayInfo newOb = new StreamDayInfo();
		List<StreamInfo> streams = new ArrayList<StreamInfo>(this.streams);
		newOb.setDay(this.day);
		newOb.setMonth(this.month);
		newOb.setStreams(streams);
		newOb.setYear(this.year);
		return newOb;
	}
}
