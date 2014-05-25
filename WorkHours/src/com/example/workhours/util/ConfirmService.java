package com.example.workhours.util;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;

public class ConfirmService extends IntentService{

	ShiftDAO dao;
	Shift s;
	
	public ConfirmService() {
		super("Confirm shift");
	}

	/*
	 * Background service for setting a shift to worked
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		int shiftId = intent.getIntExtra("OBJECT_ID", 0);
		
		if(shiftId != 0){
			dao = new ShiftDAOImpl(getApplicationContext());
			dao.open();
			
			s = dao.getShift(shiftId);
			s.setWorked(true);
			dao.updateShift(shiftId, s);
			
			dao.close();
			
			Log.d("SUCCESFULLY CONFIRMED A SHIFT", s.toString());
		}	
	}

}
