package com.example.workhours;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.Shift;

public class ShiftActivity extends Activity {
	private int fromHour, fromMin, toHour, toMin;
	private TimePicker from, to;
	private CheckBox notify, repeat;
	private RadioGroup radioGroup;
	private RadioButton weekly, monthly;
	private LinearLayout repeatLayout;
	private EditText repeatCount;
	private ShiftDAO shiftdao;
	private Shift calEvent;
	private boolean showVisible;
	private List<Shift> list;

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

	public void Save_Click(View v) {
		showVisible = false;
		getHandles();
		retrieveData();

		Notifier notifier;
		shiftdao.open();
		
		for(Shift t : list)
		{
			shiftdao.addShift(t);
			notifier = new Notifier(this, this, t);
			notifier.schedule();
		}
		shiftdao.close();

		finish();
	}

	public void Cancel_Click(View v) {
		finish();
	}

	public void retrieveData() {
		// Retrieve date from fragment, and make a calendarobject to represent
		// it
		Intent i = getIntent();
		long longDate = (Long) i.getSerializableExtra("DATE");
		Calendar dateObject = Calendar.getInstance();
		dateObject.setTimeInMillis(longDate);

		// Time from/to chosen in view
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();

		// Use Joda Time to build Date Object from the info
		DateTime f = new DateTime(longDate).withHourOfDay(fromHour)
				.withMinuteOfHour(fromMin);
		DateTime t = new DateTime(longDate).withHourOfDay(toHour)
				.withMinuteOfHour(toMin);
		Period p = new Period(f, t);

		// If end is after start, add 1 day
		if (p.getHours() < 0)
			t = t.plusDays(1);

		// Start to build the event object
		calEvent = new Shift(true);
		calEvent.setFrom(f);
		calEvent.setTo(t);

		// Now handle some other info, get email from shared prefs first.
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String uid = prefs.getString("email", null);

		boolean isWeekly = weekly.isChecked();
		// Get notification and repeat info
		calEvent.setUId(uid);
		calEvent.setRepeat(repeat.isChecked());
		calEvent.setRepeatWeekly(isWeekly);
		calEvent.setRepeatMonthly(!isWeekly);
		calEvent.setWorked(false);
		calEvent.setRepeatCount(0);
		calEvent.setNotify(true);
		
		list = new ArrayList<Shift>();
		
		// Make copy cats
		int count = 0;
		if (repeat.isChecked()) 
		{	
			try {
				count = Integer.parseInt(repeatCount.getText().toString());
			} catch (Exception e) { Log.d("NUmber exeption", ""); }
		}
		
		//Need to update count before we add shift
		if (count > 0) {
			calEvent.setRepeatCount(count);
			list.add(calEvent);
			
			Shift tmp;
			for (int x = 0; x < count; x++) 
			{
				tmp = new Shift(true);
				if (isWeekly) {
					tmp.setFrom(calEvent.getFrom().plusDays(7 * (x + 1)));
					tmp.setTo(calEvent.getTo().plusDays(7 * (x + 1)));
				} else {
					tmp.setFrom(calEvent.getFrom().plusMonths(x + 1));
					tmp.setTo(calEvent.getTo().plusMonths(x + 1));
				}
				tmp.setUId(uid);
				tmp.setRepeat(false);
				tmp.setRepeatWeekly(false);
				tmp.setRepeatMonthly(false);
				tmp.setNotify(true);
				tmp.setWorked(false);
				tmp.setRepeatCount(0);

				list.add(tmp);
			}
		}else{
			list.add(calEvent);
		}
	}

	public void getHandles() {
		from = (TimePicker) findViewById(R.id.shiftFrom);
		from.setIs24HourView(true);
		to = (TimePicker) findViewById(R.id.shiftTo);
		to.setIs24HourView(true);
		repeat = (CheckBox) findViewById(R.id.repeatsBox);
		notify = (CheckBox) findViewById(R.id.notifyBox);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		weekly = (RadioButton) findViewById(R.id.radioWeekly);
		monthly = (RadioButton) findViewById(R.id.radioMonthly);
		shiftdao = new ShiftDAOImpl(getApplicationContext());
		repeatLayout = (LinearLayout) findViewById(R.id.repeatLayout);
		repeatCount = (EditText) findViewById(R.id.repeatCount);
	}

	public void Repeat_Click(View v) {
		if (!showVisible) {
			repeatLayout.setVisibility(View.VISIBLE);
			showVisible = true;
		} else {
			repeatLayout.setVisibility(View.INVISIBLE);
			showVisible = false;
		}
	}

}
