package com.example.workhours;

import java.util.List;

import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.User;
import com.example.workhours.util.PageAssister;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class InitScreenActivity extends Activity {
	
	private Intent intent;
	private UserDAO dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_init_screen);
	
		/*Debug purposes*/
		dao = new UserDAOImpl(this);
		dao.open();
		List<User> users = dao.getUsers();
		for(User u : users) {
			
			Log.d("USER", u.toString());
		}
		
		dao.dropUser();
		/*
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit(); 
		*/
	//  editor.putString("user", user.getName());
	//	editor.clear();
	//	editor.putString("user", null);
	//	editor.commit();
		
		/**
		 * If the user has an existing account
		 * and hasn't logged out since the last session
		 * The user is navigated to the main page 
		 * */
		SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String existingAccount = preferences2.getString("email", null);
		
		if(existingAccount != null){
			
			Log.d("account", "exists");
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
	

}
