package com.example.workhours;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.example.workhours.entities.CalendarDAO;
import com.example.workhours.entities.CalendarDAOImpl;
import com.example.workhours.entities.CustomObject;
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
	private Shift calEvent;
	
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

		dao.addCalendarEvent(calEvent);

		Intent intent = new Intent(this, MainActivity.class);	
		startActivity(intent);
	}

	public void retrieveData() {
		// Retrieve data from fragment
		Intent i = getIntent();
		// Date first
		long longDate = (Long) i.getSerializableExtra("DATE");
		Calendar dateObject = Calendar.getInstance();
		dateObject.setTimeInMillis(longDate);	
		calEvent = new Shift();
		
		// Time from
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();

		// time to
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();

		dateFrom = dateObject;
		dateFrom.set(Calendar.HOUR, fromHour);
		dateFrom.set(Calendar.MINUTE, fromMin);
		calEvent.setFrom(dateFrom);
		
		dateTo = dateObject;
		dateTo.set(Calendar.HOUR, toHour);
		dateTo.set(Calendar.MINUTE, toMin);
		calEvent.setTo(dateTo);
				
		//Ensure that the times are "even" before comparing further
		dateFrom.set(Calendar.SECOND, 0);
		dateFrom.set(Calendar.MILLISECOND, 0);
		dateTo.set(Calendar.SECOND, 0);
		dateTo.set(Calendar.MILLISECOND, 0);
		
		if(dateFrom.getTimeInMillis() > dateTo.getTimeInMillis()){
			Log.d("DateFROM is after dateTO", "Adding a day to dateTO");
			dateTo.set(year, month, day+1, toHour, toMin);
		}
		
		Log.d("Date from.:,.-'¨'-.,", dateFrom.toString());
		Log.d("Date to.:,.-'¨'-.,", dateTo.toString());
		//calEvent = new Shift(dateFrom, dateTo, repeat.isChecked(), notify.isChecked());
	}

	public void getHandles() {
		from = (TimePicker) findViewById(R.id.shiftFrom);
		to = (TimePicker) findViewById(R.id.shiftTo);
		repeat = (CheckBox) findViewById(R.id.repeatsBox);
		notify = (CheckBox) findViewById(R.id.notifyBox);
		dao = new CalendarDAOImpl(getContentResolver());
	}

}
