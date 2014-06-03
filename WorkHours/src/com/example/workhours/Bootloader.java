package com.example.workhours;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;

public class Bootloader extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		//Receive last login email and get user
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String email = prefs.getString("email", null);
		UserDAO udao = new UserDAOImpl(context);
	
		if (email != null) {
			udao.open();
			User u = udao.getUser(email);
			udao.close();
			
			//Notification for due date
			Notifier n = new Notifier(context, null, u);
			n.dueDateNotify();
			
			ShiftDAO sdao = new ShiftDAOImpl(context);
			sdao.open();
			
			//Notifications for shifts that are scheduled
			for (Shift event : sdao.getShifts(email)) {
				if (event.getMillisDone() > 0) {
					n = new Notifier(context, event, null);
					n.shiftNotify();
				}
			}
			
			sdao.close();
		}

		
	}

}