package com.example.workhours.util;

import java.util.List;

import org.joda.time.DateTime;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.widget.Toast;

import com.example.workhours.dao.ShiftDAO;
import com.example.workhours.dao.ShiftDAOImpl;
import com.example.workhours.dao.UserDAO;
import com.example.workhours.dao.UserDAOImpl;
import com.example.workhours.entities.Shift;
import com.example.workhours.entities.User;

public class EmailService extends IntentService {
	private Handler handler;
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

		uDao.open();
		User u = uDao.getUser(uid);
		uDao.close();
		
		if (u.isValidEmployerEmail()) {
			sDao.open();
			shifts = sDao.getShifts(uid);
			sDao.close();
			
			/*
			 * Get a pointer to the previous month
			 */
			DateTime dt = new DateTime();
			dt = dt.minusMonths(1);
			int month = intent.getIntExtra("MONTH",0);
			int year = intent.getIntExtra("YEAR",0);
			month = (month==0) ? dt.getMonthOfYear() : (month+=1); 
			
			StringBuilder content = new StringBuilder();
			for (Shift s : shifts) {
				//Find confirmed shifts from last month and add to the HTML string builder
				if (s.isWorked() && (s.getFrom().getYear() == year) && 
						(s.getFrom().getMonthOfYear() == month))
				{
					content.append("<p>" + s.getFromFormatted() + " - "
							+ s.getToFormatted() + "</p>");
				}
			}
			//Build the intent
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL, new String[] { u.getEmployerEmail() });
			i.putExtra(Intent.EXTRA_SUBJECT, "WORKHOURS - " + u.getName());
			i.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(content.toString()));

			try {
				startActivity(Intent.createChooser(i, "Send email...")
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
			} catch (android.content.ActivityNotFoundException ex) {
				handler.post(new Runnable() {
					  @Override
					   public void run() {
					      Toast.makeText(getApplicationContext(), "Please install an email client to use this feature.", Toast.LENGTH_SHORT).show();
					   }
				});
			}
		}else{
			handler.post(new Runnable() {
				  @Override
				   public void run() {
				      Toast.makeText(getApplicationContext(), "Please register a valid Employer Email.", Toast.LENGTH_SHORT).show();
				   }
			});
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	   handler = new Handler();
	   return super.onStartCommand(intent, flags, startId);
	}

}
