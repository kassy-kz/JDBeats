package com.androidtsubu.jdbeats.db;

public class JDBeatsEntity {

	private int id;
	private long   dateTime;
	private String value1;
	private String value2;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public String getValue1() {
		return value1;
	}
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	public String getValue2() {
		return value2;
	}
	public void setValue2(String value2) {
		this.value2 = value2;
	}
}
