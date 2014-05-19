package com.example.workhours.entities;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class Shift implements Serializable{

	private static final long serialVersionUID = 1L;
	private String dateFormat = "dd.MM.yy - hh:mm";

	private int id;
	private String uId;

	private long from;
	private long to;
	private boolean notify;
	private boolean repeat;
	private boolean repeatWeekly;
	private boolean repeatMonthly;
	
	public Shift(int id, String uId, long from, long to, boolean repeats, boolean notify, boolean repeatWeekly, boolean repeatMonthly){
		this.id = id;
		this.uId = uId;
		this.from = from;
		this.to = to;
		this.notify = notify;
		this.repeat = repeats;
		this.repeatWeekly = repeatWeekly;
		this.repeatMonthly = repeatMonthly;
	}
	
	/**
	 * True for new instance, false for DATABASE use
	 */
	public Shift(boolean isNewInstance) {
		if(isNewInstance){
			Calendar c = Calendar.getInstance();
			Long time = c.getTimeInMillis();
			this.id = time.intValue();
		}
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
	
	public boolean isRepeatWeekly() {
		return repeatWeekly;
	}
	
	public boolean isRepeatMonthly() {
		return repeatMonthly;
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
	
	public String getUId() {
		return uId;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setUId(String uId){
		this.uId = uId;
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
	
	public void setRepeatWeekly(boolean value){
		this.repeatWeekly = value;
	}
	
	public void setRepeatMonthly(boolean value){
		this.repeatMonthly = value;
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
