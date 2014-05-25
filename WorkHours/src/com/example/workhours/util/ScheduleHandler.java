package com.example.workhours.util;

import com.example.workhours.ChangeShiftActivity;
import com.example.workhours.R;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ScheduleHandler extends IntentService {

	private final String TITLE = "Workhours - Confirm Shift";
	private final String CONTENT = "Awaiting confirmation of scheduled hours.";
	private ShiftDAO dao;
	private Shift s;
	
	public ScheduleHandler() {
		super("Shift handler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		dao = new ShiftDAOImpl(getApplicationContext());
		int shiftId = intent.getIntExtra("SHIFT_ID", 0);
		dao.open();
		s = dao.getShift(shiftId);
		dao.close();
		
		NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_action_share)
			.setContentTitle(TITLE)
			.setContentText(CONTENT + s.getFromFormatted() + " - " + s.getToFormatted());
		
		Intent change = new Intent(this, ChangeShiftActivity.class);
		change.putExtra("OBJECT_ID", shiftId);
		change.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent save = new Intent(this, ConfirmService.class);
		save.putExtra("OBJECT_ID", shiftId);
		
		PendingIntent piChange = PendingIntent.getActivity(this, 0, change, PendingIntent.FLAG_CANCEL_CURRENT);
		PendingIntent piSave = PendingIntent.getActivity(this, 0, save, PendingIntent.FLAG_CANCEL_CURRENT);
		
		nb.setAutoCancel(true);
		nb.addAction(R.drawable.action_save, "Confirm", piSave);
		nb.addAction(R.drawable.action_edit, "Change", piChange);
		
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.notify(shiftId, nb.build());
	}

}
