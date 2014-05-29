package com.example.workhours.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.workhours.R;
import com.example.workhours.entities.User;

public class DetailsAdapter extends ArrayAdapter<User> {
	
	private final Context context;
	private List<User> user;
	private int layoutId;
	
	private TextView header, detail;
	
	public DetailsAdapter(Context context, int layoutId, List<User> user) {
		super(context, layoutId, user);
		
		this.context  = context;
		this.layoutId = layoutId;
		this.user     = user;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			
			// inflate the layout
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(layoutId, parent, false);
    	}
		
		User u = user.get(0);
		
		try {
		
			switch(position) {
		
				case 0:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Username");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(u.getName());
					break;
				case 1:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Email address");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(u.getEmail());
					break;
				case 2:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Employer email address");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(u.getEmployerEmail());
					break;
				case 3:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Hourly wage");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(Double.toString(u.getHourlyWage()));
					break;
				case 4:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Tax");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(Double.toString(u.getTax()));
					break;
				case 5:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Schedule due date");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(Integer.toString(u.getScheduleDue()));
					break;
				case 6:
					header = (TextView) convertView.findViewById(R.id.details_header);
					header.setText("Payment basis");
					detail = (TextView) convertView.findViewById(R.id.details_detail);
					detail.setText(u.getPerPay());
					break;
				default:
					break;
			}
		} catch(Exception e) {
			
			Log.d("Error", e.getMessage());
		}
	      
        return convertView;
	}
	
	public void setUser(List<User> user) {
		
		this.user = user;
	}
}
