package com.example.workhours.fragments;

import com.example.workhours.MainActivity;
import com.example.workhours.R;
import com.example.workhours.util.ConfirmService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ConfirmDialog extends DialogFragment{
	private int sId;
	private int toggle;

	public static ConfirmDialog newInstance(int num) {
		ConfirmDialog d = new ConfirmDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		d.setArguments(args);

		return d;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		sId = getArguments().getInt("num");
		final Context c = getActivity();
		final ToggleButton toggleButton = new ToggleButton(c);
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setView(toggleButton);
		builder.setMessage("Confirm shift")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						boolean confirm = toggleButton.isChecked();

						Log.d("DIALOG CHOISE", confirm + "");
						Intent intent = new Intent(c, ConfirmService.class);
						intent.putExtra("SHIFT_ID", sId);
						intent.putExtra("IS_WORKED", confirm);
						c.startService(intent);
					}

				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

}
