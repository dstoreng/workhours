package com.example.workhours.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.workhours.R;

public class ProfileFragment extends Fragment {
	
	//Fields
	private TextView name, email, employer_email, hourly_wage, tax;
	//Labels
	private TextView user_label, email_label, hourly_wage_label, tax_label;
	private String emp_email; 
	private Double hour_wage, tax_value;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_profile, container, false);
	}
	
	public void saveDetails(View v){
		
		employer_email = (TextView) getView().findViewById(R.id.employer_email);
		emp_email = employer_email.getText().toString();
		
		hourly_wage = (TextView) getView().findViewById(R.id.hourly_wage);
		hour_wage = Double.parseDouble(hourly_wage.getText().toString());
		
		tax = (TextView) getView().findViewById(R.id.tax);
		tax_value = Double.parseDouble(tax.getText().toString());
		
		Log.d("FINISH UPDATING VALUES", " WOOOOOOOOOOP");
		showOnlyDetails();
		
		
		
	}
	
	private void showOnlyDetails(){
		
		Log.d("INSIDE SHOW ONLY DETAILS", " WOOOOOOOOOOP");
		user_label = (TextView) getView().findViewById(R.id.profileNameLabel);
		user_label.setVisibility(View.VISIBLE);
		
		email_label = (TextView) getView().findViewById(R.id.employer_email_label);
		email_label.setVisibility(View.VISIBLE);
		
		hourly_wage = (TextView) getView().findViewById(R.id.hourly_wage_label);
		hourly_wage.setVisibility(View.VISIBLE);
		
		tax_label = (TextView) getView().findViewById(R.id.tax_label);
		tax_label.setVisibility(View.VISIBLE);
		
		FragmentManager frag = getFragmentManager();
		FragmentTransaction trans = frag.beginTransaction();
		ProfileFragment profile = (ProfileFragment) frag.findFragmentById(R.id.profile);
		trans.hide(profile);
		trans.commit();
	}

}
