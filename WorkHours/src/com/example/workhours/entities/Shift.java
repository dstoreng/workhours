package com.example.workhours.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Shift implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private long hours;
	private String dateFormat = "dd.MM.yy - hh:mm";

	private long from;
	private long to;
	private boolean notify;
	private boolean repeat;
	
	public Shift(int id, long from, long to, boolean repeats, boolean notify){
		this.id = id;
		this.from = from;
		this.to = to;
		this.notify = notify;
		this.repeat = repeats;
	}
	
	public Shift() {
		Calendar c = Calendar.getInstance();
		Long time = c.getTimeInMillis();
		this.id = time.intValue();
	}

	private long findHours(){
		return TimeUnit.MILLISECONDS.toHours(Math.abs(to-from));
	}
	
	public boolean isNotify() {
		return notify;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	public long getFrom() {
		return from;
	}

	public long getTo() {
		return to;
	}

	public long getHours() {
		return findHours();
	}

	public int getId() {
		return id;
	}
	
	public void setFrom(long from) {
		
		this.from = from;
	}

	public void setTo(long to) {
		this.to = to;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	
	/**
	 * 
	 * Returns FROM date in a human readable format
	 */
	public String getFromFormatted(){
		return getDateString(from);
	}
	
	/**
	 * 
	 * Returns TO date in a human readable format
	 */
	public String getToFormatted(){
		return getDateString(to);
	}
	
	private String getDateString(long date){
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date);
		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Return FROM date in string format
	 */
	public String fromSQLFormat(){
		return Long.toString(from);
	}
	
	/**
	 * Return TO date in string format
	 */
	public String toSQLFormat(){
		return Long.toString(to);
	}
	/*
	public long getFromMilliseconds(){
		Calendar date = Calendar.getInstance();
		date.set(from.get, from.getMonth(), from.getDay(), from.getHours(), from.getMinutes());
		return date.getTimeInMillis();
	}
	
	public long getToMilliseconds(){
		Calendar date = Calendar.getInstance();
		date.set(to.getYear(), to.getMonth(), to.getDay(), to.getHours(), to.getMinutes());
		return date.getTimeInMillis();
	}
*/
}
