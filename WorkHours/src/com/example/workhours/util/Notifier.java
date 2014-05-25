package com.example.workhours.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.workhours.entities.Shift;

public class Notifier {
	private AlarmManager mgr;
	private PendingIntent pi;
	private Intent intent;
	private Shift s;
	
	public Notifier(Activity a, Context c, Shift s){
		this.s = s;
		mgr = (AlarmManager) a.getSystemService(Context.ALARM_SERVICE);
		intent = new Intent(c, ScheduleHandler.class);
		intent.putExtra("SHIFT_ID", s.getId());
		pi = PendingIntent.getService(c, 0, intent, 0);
	}
	
	/**
	 * Schedule a notification for a shift
	 * s.getMillisDone(-10000)
	 */
	public void schedule(){
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5000, pi);
	}
	
	/**
	 * Cancel a notification for a shift
	 */
	public void cancel(){
		mgr.cancel(pi);
	}
	

}
