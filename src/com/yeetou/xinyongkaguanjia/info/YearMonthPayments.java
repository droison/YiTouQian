package com.yeetou.xinyongkaguanjia.info;

import java.util.List;

public class YearMonthPayments {

	private int year;
	private List<MonthPayments> monthPayments;
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public List<MonthPayments> getMonthPayments() {
		return monthPayments;
	}
	public void setMonthPayments(List<MonthPayments> monthPayments) {
		this.monthPayments = monthPayments;
	}
	
}
