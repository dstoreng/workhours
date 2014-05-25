package com.example.workhours.entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.tyczj.extendedcalendarview.CalendarProvider;
import com.tyczj.extendedcalendarview.Event;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.text.format.Time;
import android.util.Log;

public class CalendarDAOImpl implements CalendarDAO {
	private ContentResolver cr;
	private List<Long> list;
	
	public CalendarDAOImpl(ContentResolver cr){
		this.cr = cr;
		list = new ArrayList<Long>();
	}
	
	public List<Long> getAddedEventsId(){
		return list;
	}
	
	public List<Shift> getAddedEvents(){
		List<Shift> li = new ArrayList<Shift>();
		Cursor cursor = null;
		Uri uri = CalendarProvider.CONTENT_URI;
		final String[] projection = new String[] { 
				CalendarProvider.START, 		//0
				CalendarProvider.END,			//1
				CalendarProvider.DESCRIPTION, 	//2
				CalendarProvider.ID,			//3
				CalendarProvider.EVENT};		//4	
		/*
		String selection = CalendarProvider.DESCRIPTION + " = " + "'Working hours application..'";
		String[] selectionArgs = null;
		String sortOrder = null;	
		cursor = cr.query(uri, projection, selection, selectionArgs, sortOrder);
		*/
		cursor = cr.query(uri, projection, null, null, null);
		Log.d("CALENDARPROVIDER - before while", "");
		
		Shift tmp;
		while(cursor.moveToNext()){
			String start = null;
			String end = null;
			String desc = null;
			String id = null;
			String uid = null;
			
			start = cursor.getString(0);
			end = cursor.getString(1);
			desc = cursor.getString(2);
			id = cursor.getString(3);
			uid = cursor.getString(4);
			
			tmp = new Shift(false);
			tmp.parseFrom(start);
			tmp.parseTo(end);			
			li.add(tmp);
			Log.d("CALENDARPROVIDER", "Start: "+start+ " End: "+end+ "Description: "+desc);		
		}
			
		return li;
	}
	
	public void addCalendarEvent(Shift shift) {
		TimeZone timeZone = TimeZone.getDefault();

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		ContentValues values = new ContentValues();	
		/*
		 * TODO Needs some work
		 */
		values.put(Events.DTSTART, shift.getFromFormatted());
		values.put(Events.DTEND, shift.getToFormatted());
		values.put(Events.TITLE, "Working hours");
		values.put(Events.DESCRIPTION, "Working hours application..");
		if(shift.isNotify())
			values.put(Events.HAS_ALARM, shift.isNotify());
		if(shift.isRepeat())	
			values.put(Events.RRULE, "FREQ=WEEKLY;COUNT=5");
		values.put(Events.CALENDAR_ID, 1);
		values.put(Events.EVENT_TIMEZONE, timeZone.getID());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// Should save this somewhere..
		list.add(Long.parseLong(uri.getLastPathSegment()));		
	}
	
	public void addExtendedCalendarEvent(Shift shift){
	/*
		Calendar cal = Calendar.getInstance();
	    TimeZone tz = TimeZone.getDefault();
		ContentValues values = new ContentValues();
		
		values.put(CalendarProvider.COLOR, Event.COLOR_RED);
	    values.put(CalendarProvider.DESCRIPTION, "Workek");
	    values.put(CalendarProvider.LOCATION, "Some location");
	    values.put(CalendarProvider.EVENT, "Event name");   		
	    values.put(CalendarProvider.START, shift.getFrom());
	    values.put(CalendarProvider.ID, Integer.toString(shift.getId()));
	    values.put(CalendarProvider.EVENT, shift.getUId());
	    
	    //Calculate julian day, required by ExtendedCalendarView
	    int startDay = Time.getJulianDay(shift.getFrom(), TimeUnit.MILLISECONDS.toSeconds
	    		(tz.getOffset(cal.getTimeInMillis())));   
	    int endDay = Time.getJulianDay(shift.getTo(), TimeUnit.MILLISECONDS.toSeconds
	    		(tz.getOffset(cal.getTimeInMillis())));
	    
	    values.put(CalendarProvider.START_DAY, startDay);
	    values.put(CalendarProvider.END, shift.getTo());
	    values.put(CalendarProvider.END_DAY, endDay);

	    Uri uri = cr.insert(CalendarProvider.CONTENT_URI, values);
	    */
	}
	
}
