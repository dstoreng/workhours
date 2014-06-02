package com.example.workhours;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.Shift;

public class Bootloader extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Notifier n;
		ShiftDAO sdao = new ShiftDAOImpl(context);
		sdao.open();
		
		for(Shift event : sdao.getShifts()){
			if(event.getMillisDone() > 0){
				n = new Notifier(context, event, null);
				n.shiftNotify();
			}
		}
		
		sdao.close();
	}

}