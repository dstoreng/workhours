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
			pi = PendingIntent.getService(c, 0, i, 0);
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
		DateTime then;
		
		//If day is invalid, set it to the last day of the month
		try{
			then = new DateTime().withDayOfMonth(u.getScheduleDue());
		}catch(Exception e){
			then = new DateTime().dayOfMonth().withMaximumValue();
		}
		then = then.withHourOfDay(14);
		
		// Now is after then, add one month.
		if(DateTime.now().compareTo(then) > 0)
			then.plusMonths(1);
		
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 
				SystemClock.elapsedRealtime() + (then.getMillis() - DateTime.now().getMillis()), pi);
	}
	
	/**
	 * Cancel a notification for a shift
	 */
	public void cancelShiftNotify(){
		mgr.cancel(pi);
	}
	

}
