package com.example.workhours.fragments;

import java.util.Calendar;

import com.example.workhours.util.EmailService;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// Get user email so that we can retrieve user settings in email manager
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
				getActivity().getApplicationContext());
		Intent i = new Intent(getActivity(), EmailService.class);
		i.putExtra("USER_EMAIL", prefs.getString("email", ""));
		i.putExtra("YEAR", year);
		i.putExtra("MONTH", monthOfYear);
		view.getContext().
		startService(i);

	}

}
