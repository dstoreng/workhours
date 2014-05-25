package com.example.workhours.util;

import java.util.List;

import android.app.IntentService;
import android.content.Intent;
import android.text.Html;
import android.widget.Toast;

import com.example.workhours.MainActivity;
import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;

public class EmailService extends IntentService{
	UserDAO uDao;
	ShiftDAO sDao;
	List<Shift> shifts;
	
	public EmailService() {
		super("Email Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String uid = intent.getStringExtra("USER_EMAIL");
		sDao = new ShiftDAOImpl(getApplicationContext());
		uDao = new UserDAOImpl(getApplicationContext());
		
		sDao.open();
		shifts = sDao.getShifts();
		sDao.close();
		
		uDao.open();
		//User u = uDao.getUser(uid);
		User u = new User("Daniel S.", "dan_tm_storeng@hotmail.com", "asdf");
		u.setEmployerEmail("danielstoreng@gmail.com");
		uDao.close();
		
		Intent i = new Intent(Intent.ACTION_SEND);
		//i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{u.getEmployerEmail()});
		i.putExtra(Intent.EXTRA_SUBJECT, "WORKHOURS - " + u.getName());
		
		
		StringBuilder content = new StringBuilder();
		for(Shift s : shifts){
			content.append("<p>" + s.getFromFormatted() +
							" - " + s.getToFormatted() + "</p>");
		}
		i.putExtra(
		         Intent.EXTRA_TEXT,
		         Html.fromHtml(content
		             .toString())
		         );
		
		try {
			startActivity(Intent.createChooser(i, "Send email...").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
		} catch (android.content.ActivityNotFoundException ex) {
		    Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
		
	}

}
