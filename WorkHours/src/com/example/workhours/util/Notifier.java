package com.example.workhours.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.workhours.entities.Shift;

public class Notifier {
	private AlarmManager mgr;
	private PendingIntent pi;
	private Intent i;
	
	public Notifier(Activity a, Context c, Shift shift){
		mgr = (AlarmManager) a.getSystemService(Context.ALARM_SERVICE);
		i = new Intent(c, ScheduleHandler.class);
		i.putExtra("SHIFT_ID", shift.getId());
		pi = PendingIntent.getService(c, 0, i, 0);
		
		Log.d("Notifier created for SHIFT_ID", "ID = " + shift.getId());
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
