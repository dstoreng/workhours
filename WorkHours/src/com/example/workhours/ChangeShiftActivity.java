package com.example.workhours;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.workhours.entities.CalendarDAO;
import com.example.workhours.entities.CalendarDAOImpl;
import com.example.workhours.entities.Shift;

public class ChangeShiftActivity extends Activity {

	private final String OBJECT_ID = "OBJECT_ID";
	private final String OBJECT_STRING = "OBJECT_STRING";
	private int shiftId, fHour, fMin, tHour, tMin;
	private String shiftString;
	private Intent target;
	private Shift shift;
	
	private CheckBox repeatBox, notifyBox;
	private TimePicker timePickerFrom, timePickerTo;
	//
	private List<Shift> list;
	//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_change_shift);
		
		repeatBox = (CheckBox) findViewById(R.id.changeRepeats);
		notifyBox = (CheckBox) findViewById(R.id.changeNotify);
		timePickerFrom = (TimePicker) findViewById(R.id.changeFrom);
		timePickerTo = (TimePicker) findViewById(R.id.changeTo);
		
		Intent sender = getIntent();
		shiftId = (Integer) sender.getSerializableExtra(OBJECT_ID);
		
		/*
		 * TODO These are just for testing, delete plOx
		 */
		CalendarDAO dao = new CalendarDAOImpl(getContentResolver());
		list = dao.getAddedEvents();
		shift = null;
		
		for(Shift s : list){
			int id = s.getId();
			if(id == shiftId){
				shift = s;
				Log.d("MATCH FOUND", shift.getId() + "");
			}else{
				Log.d("NO MATCH FOUND", id + " - " + shiftId);
			}
		}
		/* * * * * * * * * * * * ********* ****************************/
		
		if(shift != null){			
			Calendar cal = Calendar.getInstance();
			
			cal.setTimeInMillis(shift.getFrom());		
			timePickerFrom.setCurrentHour(cal.HOUR_OF_DAY);
			timePickerFrom.setCurrentMinute(cal.MINUTE);
			
			cal.setTimeInMillis(shift.getTo());
			timePickerFrom.setCurrentHour(cal.HOUR_OF_DAY);
			timePickerFrom.setCurrentMinute(cal.MINUTE);
			
			repeatBox.setChecked(shift.isRepeat());
			notifyBox.setChecked(shift.isNotify());
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.change_shift, menu);
		return true;
	}
	
	public void Save_Click(View v){
		finish();
	}
	
	public void Cancel_Click(View v){
		finish();
	}

}
