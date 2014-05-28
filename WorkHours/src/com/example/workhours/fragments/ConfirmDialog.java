package com.example.workhours.fragments;

import java.util.List;

import org.joda.time.DateTime;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.entities.Shift;
import com.example.workhours.util.ConfirmService;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ToggleButton;

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
						List<Shift> shiftsWorked = dao.getShifts();
						dao.close();
						Shift tmp = null;
						for(Shift s: shiftsWorked){
							if(s.getId() == sId){
								tmp = s;
							}
						}
						
						if(tmp != null){
							DateTime dateEnd = tmp.getTo();
							DateTime now;
						}
						Intent intent = new Intent(c, ConfirmService.class);
						intent.putExtra("SHIFT_ID", sId);
						intent.putExtra("IS_WORKED", confirm);
						c.startService(intent);
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
