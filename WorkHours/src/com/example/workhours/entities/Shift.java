package com.example.workhours.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Shift {
	private Date start;
	private double wage;
	private double hours;
		private boolean weekly;

	public Shift(Date date, double hours, double wage, boolean weekly){
		this.start = date;
		this.hours = hours;
		this.wage = wage;
		this.weekly = weekly;
	}
	
	public void slett(){
		/*
		String dd = Integer.toString(date.getDayOfMonth());
        String dm = Integer.toString(date.getMonth());
        String dy = Integer.toString(date.getYear());
        String th = Integer.toString(time.getCurrentHour());
        String tm = Integer.toString(time.getCurrentMinute());
        
        String dateNonLocalized = dy+"-"+dm+"-"+dd + " "+th+":"+tm;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        
        Date dateLocalized;
        String dateString;
		try {
			dateLocalized = sdf.parse(dateNonLocalized);
			dateString = dateLocalized.toString();
				
		} catch (ParseException e) {
			dateString = th + ":" + tm + " "+ dd + "/"+ dm + "/" + dy;
		}
		*/
	}
	

}
