package com.example.workhours;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.util.PageAssister;

public class InitScreenActivity extends Activity {
	
	private Intent intent;
	private UserDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_init_screen);
			
		dao = new UserDAOImpl(this);
		dao.open();
		
		for(User u: dao.getUsers())
			Log.d("USER IN DB", u.toString());
		/**
		 * If the user has an existing account
		 * and hasn't logged out since the last session
		 * The user is navigated to the main page 
		 * */
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String existingAccount = preferences.getString("email", null);
	//	SharedPreferences.Editor editor = preferences.edit(); 
	//	editor.clear();
//		editor.commit();
//		dao.dropUser();
		
		if(existingAccount != null){
			
			Log.d("ACCOUNT", "exists");
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.init_screen, menu);
		
		return true;
	}
	
	public void buttonClick(View v) {
		
		intent = PageAssister.getPage(v);
		
		if(intent != null)
		    startActivity(intent);
	}
	
	  @Override
	  protected void onResume() {
	    dao.open();
	    super.onResume();
	  }

	  @Override
	  protected void onPause() {
	    dao.close();
	    super.onPause();
	  }
	  
	@Override
	public void onBackPressed() {

		  moveTaskToBack(true);
	  }
}
