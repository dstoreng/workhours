package com.example.workhours.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract.Events;

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
	
	public void addCalendarEvent(Shift shift) {
		TimeZone timeZone = TimeZone.getDefault();

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		ContentValues values = new ContentValues();	
		/*
		 * TODO Needs some work
		 */
		values.put(Events.DTSTART, shift.getFrom().getTimeInMillis());
		values.put(Events.DTEND, shift.getTo().getTimeInMillis());
		values.put(Events.TITLE, "Virker den lol?");
		values.put(Events.DESCRIPTION, "Working hours application..");
		//values.put(Events.HAS_ALARM, shift.isNotify());
		if(shift.isRepeat())
		{
			values.put(Events.RRULE, "FREQ=DAILY;COUNT=10");
		}
		values.put(Events.CALENDAR_ID, 1);
		values.put(Events.EVENT_TIMEZONE, timeZone.getID());
		Uri uri = cr.insert(Events.CONTENT_URI, values);

		// Should save this somewhere..
		list.add(Long.parseLong(uri.getLastPathSegment()));		
	}
	
	

}
