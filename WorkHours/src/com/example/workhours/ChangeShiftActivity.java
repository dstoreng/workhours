package com.example.workhours;

import java.util.Calendar;
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
import com.example.workhours.entities.Shift;

public class ChangeShiftActivity extends Activity {

	private final String OBJECT_ID = "OBJECT_ID";
	private int day, month, year, fromHour, fromMin, toHour, toMin, shiftId;
	private Shift shift;
	private ShiftDAO shiftDao;
	private long date;
	private Calendar dateFrom, dateTo;

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
		shiftId = (Integer) sender.getSerializableExtra(OBJECT_ID);

		shiftDao.open();
		shift = shiftDao.getShift(shiftId);
		Log.d("RECIEVE OBJECT IS WEEKLY", shift.isRepeatWeekly() + "");
		Log.d("RECIEVE OBJECT IS MONTHLY", shift.isRepeatMonthly() + "");
		shiftDao.close();

		if (shift != null) {
			date = shift.getFrom();

			// Hour from
			int hf = shift.getDateSpecialFormat(shift.getFrom(), "hh");
			int mf = shift.getDateSpecialFormat(shift.getFrom(), "mm");
			from.setCurrentHour(hf);
			from.setCurrentMinute(mf);

			// Hour to
			int ht = shift.getDateSpecialFormat(shift.getTo(), "hh");
			int mt = shift.getDateSpecialFormat(shift.getTo(), "mm");
			Log.d("Trying to set to", ht + ":" + mt);
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

	public void saveShift(View v) {
		Calendar dateObject = Calendar.getInstance();
		dateObject.setTimeInMillis(date);

		// time from
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
		// Set event from property
		shift.setFrom(dateFrom.getTimeInMillis());

		dateTo = dateObject;
		dateTo.set(Calendar.HOUR_OF_DAY, toHour);
		dateTo.set(Calendar.MINUTE, toMin);
		dateTo.set(Calendar.SECOND, 0);
		dateTo.set(Calendar.MILLISECOND, 0);

		// Ensure that the times are "even" before comparing further
		if (dateFrom.getTimeInMillis() > dateTo.getTimeInMillis()) {
			Log.d("DateFROM is after dateTO", "Adding a day to dateTO");
			dateTo.set(year, month, day + 1, toHour, toMin);
		}
		// Set event to property
		shift.setTo(dateTo.getTimeInMillis());

		// Get notification and repeat info
		shift.setNotify(notify.isChecked());
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
		Log.d("Repeat weekly", shift.isRepeatWeekly() + "");
		Log.d("Repeat monthly", shift.isRepeatMonthly()+"");
		shiftDao.close();

		finish();
	}

	public void Cancel_Click(View v) {
		finish();
	}

	public void Repeat_Click(View v) {
		if (!showVisible) {
			radioGroup.setVisibility(v.VISIBLE);
			showVisible = true;
		} else {
			radioGroup.setVisibility(v.INVISIBLE);
			showVisible = false;
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
	}

}
