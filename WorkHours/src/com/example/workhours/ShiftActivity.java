package com.example.workhours;

import java.util.Calendar;
import org.joda.time.DateTime;
import org.joda.time.Period;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;
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
	private int fromHour, fromMin, toHour, toMin;
	private TimePicker from, to;
	private CheckBox notify, repeat;
	private RadioGroup radioGroup;
	private RadioButton weekly, monthly;
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

	public void Save_Click(View v) {
		showVisible = false;
		getHandles();
		retrieveData();
		
		shiftdao.open();
		shiftdao.addShift(calEvent);
		shiftdao.close();
		
		finish();
	}
	
	public void Cancel_Click(View v){
		finish();
	}

	public void retrieveData() {
		// Retrieve date data from fragment
		Intent i = getIntent();
		long longDate = (Long) i.getSerializableExtra("DATE");
		
		Calendar dateObject = Calendar.getInstance();
		dateObject.setTimeInMillis(longDate);
		
		// Time from/to
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();
		
		DateTime f = new DateTime(longDate).withHourOfDay(fromHour).withMinuteOfHour(fromMin);
		DateTime t = new DateTime(longDate).withHourOfDay(toHour).withMinuteOfHour(toMin);
		Period p = new Period(f, t);
		
		if(p.getHours() < 0){
			t = new DateTime(longDate).
					withDayOfMonth(f.getDayOfMonth()+1).
					withHourOfDay(toHour).
					withMinuteOfHour(toMin);
			//Dette kan fjernes
			p = new Period(f, t);
			Log.d("ORDNET TIMER DD:HH", p.getDays() + " " + p.getHours());
		}
		
		calEvent = new Shift(true);
		calEvent.setFrom(f);
		calEvent.setTo(t);
		
		//Now handle some other info, get email from shared prefs first.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String uid = prefs.getString("email", null);
		
		//Get notification and repeat info
		calEvent.setUId(uid);
		calEvent.setNotify(notify.isChecked());
		calEvent.setRepeat(repeat.isChecked());
		calEvent.setRepeatWeekly(weekly.isChecked());
		calEvent.setRepeatMonthly(monthly.isChecked());

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
	}
	
	public void Repeat_Click(View v){
		if(!showVisible){
			radioGroup.setVisibility(View.VISIBLE);
			showVisible = true;
		}else{
			radioGroup.setVisibility(View.INVISIBLE);
			showVisible = false;
		}
	}

}
