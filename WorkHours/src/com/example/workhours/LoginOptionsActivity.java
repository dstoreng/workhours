package com.example.workhours;

import java.util.Arrays;

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
import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

public class LoginOptionsActivity extends Activity {

	private Intent intent;
	private UiLifecycleHelper uiHelper;
    private LoginButton loginButton;
    private GraphUser user;
    private String email;
	private static User account = null;
	private UserDAO dao;
	
	private SharedPreferences preferences;
    
    private Session.StatusCallback callback = new Session.StatusCallback() {
        
    	@Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login_options);
		
		dao = new UserDAOImpl(this);
		
		loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions(Arrays.asList("email "));
		
		loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            
			@Override
            public void onUserInfoFetched(GraphUser user) {
            	LoginOptionsActivity.this.user = user;
                updateUI();
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.login_options, menu);
		
		return true;
	}
	
	public void buttonClick(View v) {
		
		intent = PageAssister.getPage(v);
		
		if(intent != null)
		    startActivity(intent);
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
        dao.open();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);

        updateUI();
    }
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
  
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }
    
    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", "Success!");
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
        dao.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
       
		if (state.isOpened()) {
	        
			Log.i("DEBUG", "Logged in...");
			
			updateUI();

	    } else if (state.isClosed()) {
	        Log.i("DEBUG", "Logged out...");
	    }	
    }
    
    private void updateUI() {
        
    	Session session = Session.getActiveSession();
        boolean enableButtons = (session != null && session.isOpened());
        
        if (enableButtons && user != null) {
        	 
        	 Log.d("user fname", user.getFirstName());
             Log.d("user lname", user.getLastName());
             Log.d("user username", user.getUsername());
             Log.d("user email", (String) user.getProperty("email"));
             
            email = (String) user.getProperty("email");
            
            boolean isExisitingAccount = false;
            dao.open();
            
            try {
            	
            	dao.getUser(email);
            	isExisitingAccount = true;
            	
            } catch(Exception e) {}
 			
 			if(isExisitingAccount) {
 				
	           //Stores user in shared preferences 
	 			preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	 			SharedPreferences.Editor editor = preferences.edit();
	 			
	 			editor.putString("email", (String) user.getProperty("email"));
				editor.commit();
	             
	             intent = new Intent(getBaseContext(), MainActivity.class);
	             startActivity(intent);
	             
 			} else {
 			
 				preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	 			SharedPreferences.Editor editor = preferences.edit();
	 			
	 			account = new User(user.getUsername(), (String) user.getProperty("email"), "default8");
	 			dao.open();
   				dao.addUser(account);
   				
	 			
   				editor.putString("email", (String) user.getProperty("email"));
				editor.commit();
				
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
            	startActivity(intent);
 			}
        } else {}
    }
}
