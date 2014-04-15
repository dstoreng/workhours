package com.example.workhours;

import java.util.Calendar;
import java.util.TimeZone;

import com.example.workhours.entities.CalendarDAO;
import com.example.workhours.entities.CalendarDAOImpl;
import com.example.workhours.entities.Shift;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;

public class ShiftActivity extends Activity {
	private int day, month, year, fromHour, fromMin, toHour, toMin;
	private Calendar dateFrom, dateTo;
	private TimePicker from, to;
	private CheckBox notify, repeat;
	private CalendarDAO dao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shift);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shift, menu);
		return true;
	}

	public void saveShift(View v) {
		getHandles();
		retrieveData();

		Shift calEvent = new Shift(dateFrom, dateTo, repeat.isChecked(), notify.isChecked());
		dao.addCalendarEvent(calEvent);

		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("DATEFROM", dateFrom);
		intent.putExtra("DATETO", dateTo);
		startActivity(intent);
	}

	public void retrieveData() {
		// Retrieve data from windows
		Intent i = getIntent();
		// Date first
		day = (Integer) i.getSerializableExtra("DAY");
		month = (Integer) i.getSerializableExtra("MONTH");
		year = (Integer) i.getSerializableExtra("YEAR");

		// Time from
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();

		// time to
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();

		dateFrom = Calendar.getInstance();
		dateFrom.set(year, month, day, fromHour, fromMin);
		dateTo = Calendar.getInstance();
		dateTo.set(year, month, day, toHour, toMin);
		/*
		 * String dateFromNonLocalized = year+"-"+month+"-"+day +
		 * " "+fromHour+":"+fromMin; String dateToNonLocalized =
		 * year+"-"+month+"-"+day + " "+toHour+":"+toMin; SimpleDateFormat
		 * dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm",
		 * Locale.getDefault());
		 * 
		 * try { dateFrom = dateformat.parse(dateFromNonLocalized); dateTo =
		 * dateformat.parse(dateToNonLocalized); } catch (ParseException e) {
		 * Log.d("Parse error", "simpledateformat wtf"); }
		 */
	}

	public void getHandles() {
		from = (TimePicker) findViewById(R.id.shiftFrom);
		to = (TimePicker) findViewById(R.id.shiftTo);
		repeat = (CheckBox) findViewById(R.id.repeatsBox);
		notify = (CheckBox) findViewById(R.id.notifyBox);
		dao = new CalendarDAOImpl(getContentResolver());
	}
/*
	public void addCalendarEvent(Shift shift) {
		TimeZone timeZone = TimeZone.getDefault();

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setType("vnd.android.cursor.item/event");
		ContentResolver cr = getContentResolver();
		ContentValues values = new ContentValues();
		
		/*
		 * TODO Needs some work
		 
		values.put(Events.DTSTART, dateFrom.getTimeInMillis());
		values.put(Events.DTEND, dateTo.getTimeInMillis());
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
		long eventID = Long.parseLong(uri.getLastPathSegment());

	}
*/
}
