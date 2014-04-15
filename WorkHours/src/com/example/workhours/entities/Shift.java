package com.example.workhours.entities;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class Shift{
	private long id;
	private Calendar from;
	private Calendar to;
	private double wage;
	private long hours;
	private boolean weekly;
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

	public double getWage() {
		return wage;
	}

	public double getHours() {
		return hours;
	}

	public boolean isWeekly() {
		return weekly;
	}

	public long getId() {
		return id;
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
