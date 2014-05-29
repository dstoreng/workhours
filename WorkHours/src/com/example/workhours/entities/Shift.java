package com.example.workhours.entities;

import java.io.Serializable;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.DurationFieldType;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Shift implements Serializable, Comparable<Shift> {

	private static final long serialVersionUID = 1L;
	private String dateFormat = "dd/MM/yy - HH:mm";
	private Random r;

	private int id;
	private String uId;
	
	private Period period;
	private DateTime from;
	private DateTime to;
	private boolean worked;
	private boolean notify;
	private boolean repeat;
	private boolean repeatWeekly;
	private boolean repeatMonthly;
	private int repeatCount;

	/**
	 * True for new instance, false for DATABASE use
	 */
	public Shift(boolean isNewInstance) {
		if (isNewInstance) {
			r = new Random();
			this.id = r.nextInt();
		}
	}
	
	public boolean isWorked(){
		return worked;
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

	public DateTime getFrom() {
		return from;
	}

	public DateTime getTo() {
		return to;
	}

	public int getId() {
		return id;
	}

	public String getUId() {
		return uId;
	}
	
	public int getHours(){
		period = new Period(from, to);
		return period.getHours();
	}
	
	public int getRepeatCount(){
		return repeatCount;
	}
	
	/**
	 * Returns the ENTIRE TIMESPAN in MINUTES
	 */
	public int getMinutes(){
		PeriodType type = PeriodType.forFields(new DurationFieldType[] {
                DurationFieldType.minutes()
            });
		Period p = new Period(from, to, type);
		return p.getMinutes();
	}
	
	public void setWorked(boolean worked){
		this.worked = worked;
	}

	public void setTo(DateTime to) {
		this.to = to;
	}

	public void setFrom(DateTime from) {
		this.from = from;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUId(String uId) {
		this.uId = uId;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}

	public void setRepeatWeekly(boolean value) {
		this.repeatWeekly = value;
	}

	public void setRepeatMonthly(boolean value) {
		this.repeatMonthly = value;
	}
	
	public void setRepeatCount(int value){
		this.repeatCount = value;
	}
	
	/**
	 * Return from in a pre-specified format
	 * 		dd/MM/yy - HH:mm
	 */
	public String getFromFormatted(){
		return from.toString(dateFormat);
	}
	
	/**
	 * Return to in a pre-specified format
	 * 		dd/MM/yy - HH:mm
	 */
	public String getToFormatted(){
		return to.toString(dateFormat);
	}
	
	/**
	 *	Parses string into DateTime
	 *	Format must be dd/MM/yy - HH:mm
	 */
	public void parseFrom(String val){
		DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
		from = format.parseDateTime(val);
	}
	
	/**
	 *	Parses string into DateTime
	 *	Format must be dd/MM/yy - HH:mm
	 */
	public void parseTo(String val){
		DateTimeFormatter format = DateTimeFormat.forPattern(dateFormat);
		to = format.parseDateTime(val);
	}
	
	/**
	 * @returns time in milliseconds until shift is DONE.
	 */
	public long getMillisDone(){
		DateTime now = DateTime.now();
		return (to.getMillis() - now.getMillis());
	}

	public int compareTo(Shift s){
		return this.getFrom().compareTo(s.getFrom());
	}
	
	@Override
	public String toString(){
		return getFromFormatted() + " 	-	" + getToFormatted() + "	-	" + isWorked();
	}
}
