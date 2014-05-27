package com.example.workhours.entities;

import java.util.List;

import org.joda.time.DateTime;

import android.util.Log;

public class Calculations {
	List<Shift> shifts;
	User u;
	
	public Calculations(User user, List<Shift> list){
		u = user;
		shifts = list;
	}
	
	/**
	 * Returns previous months salary, only hours confirmed are included
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
				total += s.getMinutes();
				Log.d("TOTAL - Prev MONTH", total + "");
			}
		}
		
		int hours = total / 60;
		int minutes = total & 60;
		
		return (hours + (minutes / 60)) * salary;
	}
	
	/**
	 * Returns this months salary
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
				Log.d("SHift info", s.getFromFormatted() + " " + s.getToFormatted()+ " Hours: " + s.getHours()+ " Min: " + s.getMinutes() );
				Log.d("TOTAL - next MONTH", total + "");
			}
		}
		
		int hours = total / 60;
		int minutes = total & 60;
		
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
