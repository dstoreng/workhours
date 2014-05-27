package com.example.workhours.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;

public class ConfirmService extends IntentService {
	ShiftDAO dao;
	Shift s;
	int shiftId;
	boolean confirmed;

	public ConfirmService() {
		super("Confirm shift");
	}

	/*
	 * Background service for setting a shift to worked
	 * There are two users of it
	 * Schedule handler does not supply a boolean extra
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		shiftId = intent.getIntExtra("SHIFT_ID", 0);
		confirmed = intent.getBooleanExtra("IS_WORKED", true);
		
		if (shiftId != 0) {
			dao = new ShiftDAOImpl(getApplicationContext());
			dao.open();

			s = dao.getShift(shiftId);
			s.setWorked(confirmed);
			dao.updateShift(shiftId, s);
			
			dao.close();
			
			// Remove attached notification
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(shiftId);
		}
	}

}
