package com.example.workhours.entities;

import android.app.Activity;
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
	
	public Notifier(Activity a, Context c, Shift shift, User user){
		mgr = (AlarmManager) a.getSystemService(Context.ALARM_SERVICE);
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
	
	public void dueDateNotify(){
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 10000, pi);
	}
	
	/**
	 * Cancel a notification for a shift
	 */
	public void cancel(){
		mgr.cancel(pi);
	}
	

}
