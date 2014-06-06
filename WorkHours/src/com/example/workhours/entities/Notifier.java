package com.example.workhours.entities;

import org.joda.time.DateTime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.workhours.util.ScheduleHandler;

public class Notifier {
	private AlarmManager mgr;
	private PendingIntent pi;
	private Intent i;
	private Shift s;
	private User u;
	
	public Notifier(Context c, Shift shift, User user){
		mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
		i = new Intent(c, ScheduleHandler.class);
		
		// Schedule shift notifycation
		if(shift != null){
			s = shift;
			i.putExtra("SHIFT_ID", s.getId());
			pi = PendingIntent.getService(c, s.getId(), i, 0);
			
		//Schedule due date notification
		}else{
			u = user;
			i.putExtra("DUE_DATE", u.getEmail());
			
			//Generate a user id from email string, otherwise users will delete each others notifs.
			String str = u.getEmail();
			int strId = 0;
			for(char x : str.toCharArray()){
				strId += (int)x;
			}
			pi = PendingIntent.getService(c, strId, i, 0);
		}
		
	}
	
	/**
	 * Schedule a notification for a shift
	 * s.getMillisDone(-10000)
	 */
	public void shiftNotify(){
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + s.getMillisDone() , pi);
	}
	
	/**
	 * Schedules notification based on due date set in profile
	 */
	public void dueDateNotify(){		
		DateTime next;
		
		//If day is invalid, set it to the last day of the month
		try{
			next = new DateTime().withDayOfMonth(u.getScheduleDue());
		}catch(Exception e){
			next = new DateTime().dayOfMonth().withMaximumValue();
		}
		next = next.withHourOfDay(15);
		
		// Now is after then, add one month or week
		if(DateTime.now().compareTo(next) > 0){
			if(u.getPerPay().equals("weekly")){
				do
				{
					next = next.plusDays(7);
				}while(DateTime.now().compareTo(next) > 0);					
			}else{
				next = next.plusMonths(1);
			}
			next = next.plusMonths(1);
		}
		long first = next.getMillis() - DateTime.now().getMillis();
		
		if(u.getPerPay().equals("weekly"))
		{
			DateTime week2 = next.plusDays(7);
			long interval = week2.getMillis() - next.getMillis();
			mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + first, interval, pi);
		}else{
			DateTime month2 = next.plusMonths(1);
			long interval = month2.getMillis() - next.getMillis();	
			mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + first, interval, pi);
		}
	}
	
	/**
	 * Cancel a notification for a shift
	 */
	public void cancelShiftNotify(){
		mgr.cancel(pi);
	}
	

}
