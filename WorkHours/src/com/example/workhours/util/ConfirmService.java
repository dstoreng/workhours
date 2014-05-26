package com.example.workhours.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;

public class ConfirmService extends IntentService {
	private Handler handler;
	ShiftDAO dao;
	Shift s;
	int shiftId;

	public ConfirmService() {
		super("Confirm shift");
	}

	/*
	 * Background service for setting a shift to worked
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		shiftId = intent.getIntExtra("SHIFT_ID", 0);

		if (shiftId != 0) {

			dao = new ShiftDAOImpl(getApplicationContext());
			dao.open();

			s = dao.getShift(shiftId);
			s.setWorked(true);
			dao.updateShift(shiftId, s);

			dao.close();

			// Remove notification
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(shiftId);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		handler = new Handler();
		return super.onStartCommand(intent, flags, startId);
	}

}
