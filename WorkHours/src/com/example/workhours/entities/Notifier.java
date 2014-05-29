package com.example.workhours.entities;

import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.example.workhours.util.ScheduleHandler;

public class Notifier {
	private AlarmManager mgr;
	private PendingIntent pi;
	private Intent i;
	private Shift s;
	
	public Notifier(Activity a, Context c, Shift shift){
		mgr = (AlarmManager) a.getSystemService(Context.ALARM_SERVICE);
		i = new Intent(c, ScheduleHandler.class);
		s = shift;
		i.putExtra("SHIFT_ID", s.getId());
		pi = PendingIntent.getService(c, s.getId(), i, 0);
		
		Log.d("Notifier created for SHIFT_ID", "ID = " + s.getId() + " time=" + String.format("%d sec", TimeUnit.MILLISECONDS.toSeconds(s.getMillisDone())));
	}
	
	/**
	 * Schedule a notification for a shift
	 * s.getMillisDone(-10000)
	 */
	public void schedule(){
		mgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + s.getMillisDone() , pi);
	}
	
	/**
	 * Cancel a notification for a shift
	 */
	public void cancel(){
		mgr.cancel(pi);
	}
	

}
