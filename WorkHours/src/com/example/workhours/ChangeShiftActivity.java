package com.example.workhours;

import org.joda.time.DateTime;
import org.joda.time.Period;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.Shift;

public class ChangeShiftActivity extends Activity {
	private int fromHour, fromMin, toHour, toMin, shiftId;
	private Shift shift;
	private ShiftDAO shiftDao;
	private DateTime date;

	private RadioButton weekly, monthly;
	private RadioGroup radioGroup;
	private CheckBox repeat, notify;
	private TimePicker from, to;
	private boolean showVisible;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_shift);
		shiftDao = new ShiftDAOImpl(getApplicationContext());

		getHandles();

		Intent sender = getIntent();
		shiftId = (Integer) sender.getSerializableExtra("SHIFT_ID");
		Log.d("Change shift", "ID = " + shiftId);
		shiftDao.open();
		shift = shiftDao.getShift(shiftId);
		shiftDao.close();
		Log.d("Change shift", " retrieved shift from DB, ID = " + shift.getId());
		
		// Notify equals notification, cancel it.
		if(shift.isNotify()){
			Notifier n = new Notifier(this, this, shift);
			n.cancel();
		}

		if (shift != null) {
			date = shift.getFrom();

			// Hour from
			int hf = shift.getFrom().getHourOfDay();
			int mf = shift.getFrom().getMinuteOfHour();
			from.setCurrentHour(hf);
			from.setCurrentMinute(mf);

			// Hour to
			int ht = shift.getTo().getHourOfDay();
			int mt = shift.getTo().getMinuteOfHour();
			to.setCurrentHour(ht);
			to.setCurrentMinute(mt);

			boolean rep = shift.isRepeat();
			repeat.setChecked(rep);
			if (rep) {
				radioGroup.setVisibility(View.VISIBLE);
				showVisible = true;
				
				if(shift.isRepeatWeekly())
					weekly.setChecked(shift.isRepeatWeekly());
				else
					monthly.setChecked(shift.isRepeatMonthly());
			}
			notify.setChecked(shift.isNotify());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_shift, menu);
		return true;
	}

	public void Save_Click(View v) {
		// time from/to
		fromHour = from.getCurrentHour();
		fromMin = from.getCurrentMinute();
		toHour = to.getCurrentHour();
		toMin = to.getCurrentMinute();
		
		DateTime f = date.withHourOfDay(fromHour).withMinuteOfHour(fromMin);
		DateTime t = date.withHourOfDay(toHour).withMinuteOfHour(toMin);
		Period p = new Period(f, t);
		
		if(p.getHours() < 0)
			t = t.plusDays(1);

		shift.setFrom(f);
		shift.setTo(t);

		// Schedule notification
		boolean notif = notify.isChecked();
		if(notif){
			shift.setNotify(true);
			
			Notifier n = new Notifier(this, this, shift);
			n.schedule();
		}else{
			shift.setNotify(false);
		}
		shift.setRepeat(repeat.isChecked());

		// repeat = true
		if (showVisible) {
			shift.setRepeatWeekly(weekly.isChecked());
			shift.setRepeatMonthly(monthly.isChecked());
		}

		/*
		 * Publish changes to DB
		 */
		shiftDao.open();
		shiftDao.updateShift(shift.getId(), shift);
		shiftDao.close();
		
		shift = null;

		finish();
	}

	public void Cancel_Click(View v) {
		finish();
	}

	public void Repeat_Click(View v) {
		if (!showVisible) {
			radioGroup.setVisibility(View.VISIBLE);
			showVisible = true;
		} else {
			radioGroup.setVisibility(View.INVISIBLE);
			showVisible = false;
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
	}

}
