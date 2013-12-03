package com.yeetou.xinyongkaguanjia.info;

import java.util.ArrayList;
import java.util.List;

public class StreamMonthInfo implements Cloneable{

	private int year;
	private int month;
	private List<StreamDayInfo> streamdays;

	@Override
	public StreamMonthInfo clone(){
		List<StreamDayInfo> streamdays = new ArrayList<StreamDayInfo>(this.streamdays);
		StreamMonthInfo newOb = new StreamMonthInfo();
		newOb.setMonth(this.month);
		newOb.setYear(this.year);
		newOb.setStreamdays(streamdays);
		return newOb;
	}
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

	public List<StreamDayInfo> getStreamdays() {
		return streamdays;
	}

	public void setStreamdays(List<StreamDayInfo> streamdays) {
		this.streamdays = streamdays;
	}
}
