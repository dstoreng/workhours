package com.example.workhours.util;

import com.example.workhours.LoginActivity;
import com.example.workhours.LoginOptionsActivity;
import com.example.workhours.R;
import com.example.workhours.SignUpEmailActivity;
import com.example.workhours.SignUpOptionsActivity;

import android.content.Intent;
import android.view.View;


/**
 * Auxilary class that helps navigate to correct view from button click
 * */
public class PageAssister {
	
	public static Intent getPage(View v) {
		
		Intent intent = null;
		
		if(v.getId() == R.id.sign_up_options)
			
			intent = new Intent(v.getContext(), SignUpOptionsActivity.class);
		
		if(v.getId() == R.id.login_options)
			
			intent = new Intent(v.getContext(), LoginOptionsActivity.class);
		
		if(v.getId() == R.id.sign_up_email)
			
			intent = new Intent(v.getContext(), SignUpEmailActivity.class);
		/*
		//Unimplemented
		if(v.getId() == R.id.sign_up_facebook)
			
			intent = null;
			*/
		//	intent = new Intent(v.getContext(), SignUpOptionsActivity.class);
		
		if(v.getId() == R.id.login_email)
			
			intent = new Intent(v.getContext(), LoginActivity.class);
		/*
		//Unimplemented
		if(v.getId() == R.id.login_facebook)
			
			intent = null;
		//	intent = new Intent(v.getContext(), LoginOptionsActivity.class);
		*/
		
		return intent;
	}

}
