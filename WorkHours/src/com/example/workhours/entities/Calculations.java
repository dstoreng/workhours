package com.example.workhours.entities;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Interval;
import org.joda.time.Period;

public class Calculations {
	List<Shift> shifts;
	User u;
	
	public Calculations(User user, List<Shift> list){
		u = user;
		shifts = list;
	}
	
	/**
	 * Returns previous month salary, only hours confirmed are included
	 */
	public double getPrevMonthSalary(){
		DateTime time = new DateTime();
		time = time.minusMonths(1);
		int year = time.getYear();
		int month = time.getMonthOfYear();
		
		double salary = u.getHourlyWage();
		int total = 0;
		
		for(Shift s : shifts){
			if( (s.getFrom().getYear() == year) && 
					(s.getFrom().getMonthOfYear() == month))
			{
				if(s.isWorked())
					total += s.getMinutes();
			}
		}
		
		int hours = total / 60;
		int minutes = total % 60;
		
		return (hours + (minutes / 60)) * salary;
	}
	
	/**
	 * Returns this month salary, all hours are included
	 */
	public double getNextMonthSalary(){
		DateTime time = new DateTime();
		int year = time.getYear();
		int month = time.getMonthOfYear();
		
		double salary = u.getHourlyWage();
		int total = 0;
		
		for(Shift s : shifts){
			if( (s.getFrom().getYear() == year) && 
					(s.getFrom().getMonthOfYear() == month))
			{
				
				total += s.getMinutes();
			}
		}
		
		int hours = total / 60;
		int minutes = total % 60;
		
		return (hours + (minutes / 60)) * salary;
	}
	
	/**
	 * Returns this weeks salary, all hours are included
	 * */
	public double getNextWeeksSalary() {
		DateTime time = new DateTime();	
		
		DateTime start = time.withDayOfWeek(DateTimeConstants.MONDAY);
		DateTime end = time.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		int total = 0;
		for(Shift s : shifts){
			if(new Interval(start, end).contains(s.getFrom()))
				total += s.getMinutes();
		}
		
		double salary = u.getHourlyWage();
		
		int hours = total / 60;
		int minutes = total % 60;
		
		return (hours + (minutes / 60)) * salary;
	}
	
	
	/**
	 * Returns last weeks salary, only confirmed hours are included
	 * */
	public double getPrevWeeksSalary() {		
		DateTime time = new DateTime();
		time = time.minusWeeks(1);		
		
		DateTime start = time.withDayOfWeek(DateTimeConstants.MONDAY);
		DateTime end = time.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		int total = 0;
		for(Shift s : shifts){
			if(new Interval(start, end).contains(s.getFrom()))
				total += s.getMinutes();
		}
		
		double salary = u.getHourlyWage();
		
		int hours = total / 60;
		int minutes = total % 60;
		
		return (hours + (minutes / 60)) * salary;
	}
	
	public String getScheduledHours(){
		int total = 0;
		for(Shift s : shifts){
			if ((!s.isWorked()) && (s.getFrom().isAfter(DateTime.now()))) {
				total += s.getMinutes();
			}
		}	
		int hours = total / 60;
		int minutes = total & 60;
		return hours + "." + minutes;
	}

}
