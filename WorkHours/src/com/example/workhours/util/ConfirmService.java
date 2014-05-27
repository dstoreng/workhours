package com.example.workhours.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.workhours.R;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;

public class ConfirmService extends IntentService {
	private Handler handler;
	ShiftDAO dao;
	Shift s;
	int shiftId;
	boolean confirmed;

	public ConfirmService() {
		super("Confirm shift");
	}

	/*
	 * Background service for setting a shift to worked
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		shiftId = intent.getIntExtra("SHIFT_ID", 0);
		confirmed = intent.getBooleanExtra("IS_WORKED", true);
		
		handler.post(new Runnable() {
			  @Override
			   public void run() {
			      Toast.makeText(getApplicationContext(), confirmed + "", Toast.LENGTH_SHORT).show();
			   }
		});
		
		if (shiftId != 0) {

			dao = new ShiftDAOImpl(getApplicationContext());
			dao.open();

			s = dao.getShift(shiftId);
			s.setWorked(confirmed);
			dao.updateShift(shiftId, s);
			
			handler.post(new Runnable() {
				  @Override
				   public void run() {
				      Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();
				   }
			});
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
