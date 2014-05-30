package com.example.workhours.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.example.workhours.ProfileActivity;
import com.example.workhours.R;

public class ProfileFragment extends Fragment {
	
	private EditText employer_email_value, hourly_wage_value;
	private SeekBar dueDateSelector;
	private TextView due;
	private Switch sw;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_profile, container, false);
		
		employer_email_value = (EditText) view.findViewById(R.id.change_employer_email);
		hourly_wage_value    = (EditText) view.findViewById(R.id.change_hourly_wage);
		due                  = (TextView) view.findViewById(R.id.due_date_displayer);
		dueDateSelector      = (SeekBar) view.findViewById(R.id.due_day);
		sw                   = (Switch) view.findViewById(R.id.payment_mode);
		
		employer_email_value.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			boolean isFirstTimeFocus = true;
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus && isFirstTimeFocus) {
					
					employer_email_value.setText("");
					isFirstTimeFocus = false;
			    }
				
			}
		});
		
		hourly_wage_value.setOnFocusChangeListener(new OnFocusChangeListener() {
			boolean isFirstTimeFocus = true;
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				if(hasFocus && isFirstTimeFocus) {
					
					hourly_wage_value.setText("");
					isFirstTimeFocus = false;
			    }
			}
		});
		
		dueDateSelector.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		
					due.setText("Day of the month: " + Integer.toString(progress));
					ProfileActivity.dateDue = progress;
				}
		
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {}
		
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {}
				
			});
		
		ProfileActivity.payment_M = "monthly";
		sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
		    
			if(isChecked) {
		        //do stuff when Switch is ON -- weekly payments
				ProfileActivity.payment_M = "weekly";
		    } else {
		        //do stuff when Switch if OFF -- monthly payments
		    	ProfileActivity.payment_M = "monthly";
		    }
				
			}
			
		});

		return view;
	}
}
