package com.example.workhours;

import com.example.workhours.util.PageAssister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class SignUpOptionsActivity extends Activity {
	
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_options);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up_options, menu);
		return true;
	}
	
	public void buttonClick(View v) {
		
		intent = PageAssister.getPage(v);
		
		if(intent != null)
		    startActivity(intent);
	}

}
