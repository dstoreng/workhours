package com.example.workhours.fragments;

import org.joda.time.DateTime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Notifier;
import com.example.workhours.entities.Shift;
import com.example.workhours.util.ConfirmService;

public class ConfirmDialog extends DialogFragment{
	private int sId;

	public static ConfirmDialog newInstance(int num) {
		ConfirmDialog d = new ConfirmDialog();

		// Supply id as init argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		d.setArguments(args);

		return d;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		sId = getArguments().getInt("num");
		final Context c = getActivity();
		ToggleButton tb = new ToggleButton(c);
		tb.setTextOff("NO");
		tb.setTextOn("YES");
		tb.setChecked(false);
		final ToggleButton toggleButton = tb;
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setView(toggleButton);
		builder.setTitle("Have you worked this shift?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) 
					{
						boolean confirm = toggleButton.isChecked();
						ShiftDAO dao = new ShiftDAOImpl(c);
						dao.open();
						
						try{
							Shift tmp = dao.getShift(sId);
										
							DateTime dateEnd = tmp.getTo();
							DateTime now = DateTime.now();
							if(now.isAfter(dateEnd)){
								Intent intent = new Intent(c, ConfirmService.class);
								intent.putExtra("SHIFT_ID", sId);
								intent.putExtra("IS_WORKED", confirm);
								c.startService(intent);
							}
						}catch(CursorIndexOutOfBoundsException e){
							Toast.makeText(c, "Shift no longer exists.", Toast.LENGTH_SHORT).show();
						}finally{
							dao.close();
						}
												
					}
				})
				.setNegativeButton("Delete",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								ShiftDAO dao = new ShiftDAOImpl(c);
								dao.open();
								
								try{
									//Need to cancel notification before we delete the shift, or bad things will happen
									Shift s = dao.getShift(sId);
									Notifier n = new Notifier(getActivity(), c, s);
									n.cancel();
									
									dao.deleteShift(sId);
									
									//Broadcast event to views that needs to be updated
									Intent update = new Intent("activity_listener");
									LocalBroadcastManager.getInstance(c).sendBroadcast(update);
									
								}catch(CursorIndexOutOfBoundsException e){
									Toast.makeText(c, "Shift no longer exists.", Toast.LENGTH_SHORT).show();
								}finally{
									dao.close();
								}
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
