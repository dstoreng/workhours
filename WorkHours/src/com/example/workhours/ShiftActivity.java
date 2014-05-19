package com.example.workhours;

import java.util.Calendar;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.CalendarDAO;
import com.example.workhours.entities.CalendarDAOImpl;
import com.example.workhours.entities.Shift;

import android.opengl.Visibility;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

public class ShiftActivity extends Activity {
	private int day, month, year, fromHour, fromMin, toHour, toMin;
	private Calendar dateFrom, dateTo;
	private TimePicker from, to;
	private CheckBox notify, repeat;
	private RadioGroup radioGroup;
	private RadioButton weekly, monthly;
	private CalendarDAO caldao;
	private ShiftDAO shiftdao;
	private Shift calEvent;
	private boolean showVisible;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shift);
		
		getHandles();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shift, menu);
		return true;
	}

	public void saveShift(View v) {
		showVisible = false;
		getHandles();
		retrieveData();
		
		shiftdao.open();
		shiftdao.addShift(calEvent);
		
		/*
		dao.addCalendarEvent(calEvent);
		dao.addExtendedCalendarEvent(calEvent);
		
		Intent intent = new Intent(this, MainActivity.class);	
		startActivity(intent);
		*/
		finish();
	}

	public void retrieveData() {
		// Retrieve data from fragment
		Intent i = getIntent();
		// Date first
		long longDate = (Long) i.getSerializableExtra("DATE");
		Calendar dateObject = Calendar.getInstance();
		dateObject.setTimeInMillis(longDate);	
		calEvent = new Shift(true);
		
		// Time from
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();

		// time to
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();

		/*
		 * Calendar uses static variables, set object properties one at a time.
		 */
		dateFrom = dateObject;
		dateFrom.set(Calendar.HOUR_OF_DAY, fromHour);
		dateFrom.set(Calendar.MINUTE, fromMin);
		dateFrom.set(Calendar.SECOND, 0);
		dateFrom.set(Calendar.MILLISECOND, 0);
		//Set event from property
		calEvent.setFrom(dateFrom.getTimeInMillis());
		
		dateTo = dateObject;
		dateTo.set(Calendar.HOUR_OF_DAY, toHour);
		dateTo.set(Calendar.MINUTE, toMin);
		dateTo.set(Calendar.SECOND, 0);
		dateTo.set(Calendar.MILLISECOND, 0);
				
		//Ensure that the times are "even" before comparing further	
		if(dateFrom.getTimeInMillis() > dateTo.getTimeInMillis()){
			Log.d("DateFROM is after dateTO", "Adding a day to dateTO");
			dateTo.set(year, month, day+1, toHour, toMin);
		}
		//Set event to property
		calEvent.setTo(dateTo.getTimeInMillis());
		
		//Now handle some other info, get email from shared prefs first.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String uid = prefs.getString("email", null);
		//Get notification and repeat info
		calEvent.setUId(uid);
		calEvent.setNotify(notify.isChecked());
		calEvent.setRepeat(repeat.isChecked());
		//repeat = true
		if(showVisible){
			calEvent.setRepeatWeekly(weekly.isChecked());
			calEvent.setRepeatMonthly(monthly.isChecked());
		}

	}

	public void getHandles() {
		from = (TimePicker) findViewById(R.id.shiftFrom);
		to = (TimePicker) findViewById(R.id.shiftTo);
		repeat = (CheckBox) findViewById(R.id.repeatsBox);
		notify = (CheckBox) findViewById(R.id.notifyBox);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		weekly = (RadioButton) findViewById(R.id.radioWeekly);
		monthly = (RadioButton) findViewById(R.id.radioMonthly);
		caldao = new CalendarDAOImpl(getContentResolver());
		shiftdao = new ShiftDAOImpl(getApplicationContext());
	}
	
	public void Repeat_Click(View v){
		if(!showVisible){
			radioGroup.setVisibility(v.VISIBLE);
			showVisible = true;
		}else{
			radioGroup.setVisibility(v.INVISIBLE);
			showVisible = false;
		}
	}

}
