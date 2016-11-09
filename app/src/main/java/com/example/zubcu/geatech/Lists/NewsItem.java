package com.example.zubcu.geatech.Lists;

public class NewsItem {

	private int icClient;
	private int icCommand;
	private int icLocation;
	private String client;
	private String command;
	private String location;
	private String day;
	private String month;


	public int getIcClient() {
		return icClient;
	}

	public void setIcClient(int icClient){
		this.icClient = icClient;
	}

	public int getIcCommand() {
		return icCommand;
	}

	public void setIcCommand(int icCommand){
		this.icCommand = icCommand;
	}

	public int getIcLocation() {
		return icLocation;
	}

	public void setIcLocation(int icLocation){
		this.icLocation = icLocation;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

//	@Override
//	public String toString() {
//		return "[ headline=" + headline + ", reporter Name=" +
//				reporterName + " , date=" + date + "]";
//	}
}
