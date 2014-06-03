package com.example.workhours.util;

import com.example.workhours.ChangeShiftActivity;
import com.example.workhours.R;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class ScheduleHandler extends IntentService {

	private final String TITLE_CONFIRM = "Workhours - Confirm Shift";
	private final String TITLE_REMINDER = "Workhours";
	private final String CONTENT_REMINDER1 = "Reminder for ";
	private final String CONTENT_REMINDER2 = ", have you emailed your hours?";
	private ShiftDAO dao;
	private UserDAO uDao;
	
	public ScheduleHandler() {
		super("Shift handler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		int shiftId = intent.getIntExtra("SHIFT_ID", 0);
		String mail = intent.getStringExtra("DUE_DATE");
		
		//The notification is a shift confirmation
		if(mail == null)
		{
			dao = new ShiftDAOImpl(getApplicationContext());
			
			dao.open();
			Shift s = dao.getShift(shiftId);
			dao.close();
				
			//Build the notification
			NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_action_share)
				.setContentTitle(TITLE_CONFIRM)
				.setContentText(s.getFromFormatted() + " to " + s.getToFormatted());
			
			//Build action change
			Intent change = new Intent(this, ChangeShiftActivity.class);
			change.putExtra("SHIFT_ID", shiftId);
			change.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			
			//Build action confirm
			Intent save = new Intent(this, ConfirmService.class);
			save.putExtra("SHIFT_ID", shiftId);
			
			PendingIntent piChange = PendingIntent.getActivity(this, 0, change, PendingIntent.FLAG_CANCEL_CURRENT);
			PendingIntent piSave = PendingIntent.getService(this, 0, save, PendingIntent.FLAG_CANCEL_CURRENT);
			
			nb.setAutoCancel(true);
			nb.addAction(R.drawable.action_save, "Confirm", piSave);
			nb.addAction(R.drawable.action_edit, "Change", piChange);
			
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.notify(shiftId, nb.build());
		
		//The notification is a reminder to email worked hours
		}else{
			uDao = new UserDAOImpl(getApplicationContext());
			uDao.open();
			User u = uDao.getUser(mail);
			uDao.close();
			
			NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_action_share)
				.setContentTitle(TITLE_REMINDER)
				.setContentText(CONTENT_REMINDER1 + u.getName() + CONTENT_REMINDER2)
				.setAutoCancel(true)
				.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0,  new Intent() 
					, PendingIntent.FLAG_CANCEL_CURRENT));
				
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			nm.notify(0, nb.build());
		}
	}

}
