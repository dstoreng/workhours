package com.example.workhours.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Shift implements Serializable{

	private static final long serialVersionUID = 1L;
	private long id;
	private long hours;

	private long from;
	private long to;
	private boolean notify;
	private boolean repeat;
	
	public Shift(long from, long to, boolean repeats, boolean notify){
		this.id = from + to;
		this.from = from;
		this.to = to;
		this.notify = notify;
		this.repeat = repeats;
	}
	
	public Shift() {}

	private long findHours(){
		return TimeUnit.MILLISECONDS.toHours(Math.abs(from-to));
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

	public long getId() {
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
