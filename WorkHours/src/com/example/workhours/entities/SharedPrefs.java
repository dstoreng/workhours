package com.example.workhours.entities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefs {
	private SharedPreferences prefs;
	private final String SHARED_PREFERENCES = "workhours";
	private final int context = Context.MODE_PRIVATE;
	private Editor editor;
	
	public SharedPrefs(Activity a){
		this.prefs = a.getSharedPreferences(SHARED_PREFERENCES, context);
		editor = prefs.edit();
	}
	
	public void addHours(double value){
		double prevValue = Double.longBitsToDouble(prefs.getLong("Hours", (long) 0d));	
		editor.putLong("Hours", Double.doubleToRawLongBits(value + prevValue));
		editor.commit();
	}
	
	public double getHours(){
		if(!prefs.contains("Hours"))
			return 0d;
		return Double.longBitsToDouble(prefs.getLong("Hours",(long) 0d));
	}
	
	public void clear(){
		editor.clear();
		editor.commit();
	}

}
