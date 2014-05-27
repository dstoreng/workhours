package com.example.workhours.fragments;

import com.example.workhours.R;
import com.example.workhours.util.ConfirmService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

public class ConfirmDialog extends DialogFragment {
	int id;
	
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
		
		LayoutInflater inf = getActivity().getLayoutInflater();
		final View v = inf.inflate(R.layout.confirm_layout, null);
		id = getArguments().getInt("num");
		
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Confirm shift")
				.setView(inf.inflate(R.layout.confirm_layout, null))
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					
							public void onClick(DialogInterface dialog, int id) {
								ToggleButton toggle = (ToggleButton) v.findViewById(R.id.toggleConfirm);
								boolean confirm = toggle.isChecked();
								if(confirm){
									Intent intent = new Intent(getActivity(),
											ConfirmService.class);
									intent.putExtra("SHIFT_ID", id);
									intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
									getActivity().startService(intent);
								}
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
