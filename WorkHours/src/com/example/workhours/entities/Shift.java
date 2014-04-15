package com.example.workhours.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Shift implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private Calendar from;
	private Calendar to;
	private long hours;
	private boolean notify;
	private boolean repeat;
	
	public Shift(Calendar from, Calendar to, boolean repeats, boolean notify){
		this.id = from.getTimeInMillis() + to.getTimeInMillis();
		this.from = from;
		this.to = to;
		this.notify = notify;
		this.repeat = repeats;
		this.hours = findHours();
	}
	
	public Shift() {}

	public long findHours(){
		return TimeUnit.MILLISECONDS.toHours(Math.abs
				(from.getTimeInMillis() - to.getTimeInMillis()));
	}
	
	public boolean isNotify() {
		return notify;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	public Calendar getFrom() {
		return from;
	}

	public Calendar getTo() {
		return to;
	}

	public double getHours() {
		return hours;
	}

	public long getId() {
		return id;
	}
	
	public void setFrom(Calendar from) {
		this.from = from;
	}

	public void setTo(Calendar to) {
		this.to = to;
	}

	public void setNotify(boolean notify) {
		this.notify = notify;
	}

	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
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
