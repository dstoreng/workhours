package com.example.workhours;

import org.joda.time.DateTime;
import org.joda.time.Period;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
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
	private RelativeLayout actionBox;
	private TimePicker from, to;
	private Notifier notifier;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_shift);
		shiftDao = new ShiftDAOImpl(getApplicationContext());

		getHandles();

		Intent sender = getIntent();
		shiftId = (Integer) sender.getSerializableExtra("SHIFT_ID");
		shiftDao.open();
		shift = shiftDao.getShift(shiftId);
		shiftDao.close();
		
		// Notify equals notification, cancel it.
		if(shift.isNotify()){
			notifier = new Notifier(this, this, shift);
			notifier.cancel();
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

		/*
		 * Publish changes to DB
		 */
		shiftDao.open();
		shiftDao.updateShift(shift.getId(), shift);
		shiftDao.close();
		
		
		//Update notification manager
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(shift.getId());
		
		//Schedule notification if shifts hasnt already happened
		if(shift.getTo().isAfter(DateTime.now()))
		{
			notifier = new Notifier(this, this, shift);
			notifier.schedule();
		}
		
		/*
		 * Broadcast event to views that needs to be updated
		 */
		Intent update1 = new Intent("activity_listener");
		LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(update1);
		
		shift = null;
		finish();
	}

	public void Cancel_Click(View v) {
		finish();
	}

	public void getHandles() {
		from = (TimePicker) findViewById(R.id.shiftFrom);
			from.setIs24HourView(true);
		to = (TimePicker) findViewById(R.id.shiftTo);
			to.setIs24HourView(true);
		actionBox = (RelativeLayout) findViewById(R.id.actionBox);
			actionBox.setVisibility(View.INVISIBLE);
	}

}
